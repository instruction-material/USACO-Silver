"""Verify actual role file I/O against independent exhaustive models."""
import argparse
import hashlib
import itertools
import json
import os
from pathlib import Path
import random
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]

PACK, BASENAME = "US9-Number-Triangles", "numtri"
LEGACY = PACK + "/solution/numtri.in"
ORACLE = "enumerate every legal binary path and sum its visited cells"
LARGE_CASES = 4


def path_total(triangle):
    totals = []
    for choices in itertools.product([0, 1], repeat=len(triangle) - 1):
        column, total = 0, triangle[0][0]
        for row, choice in enumerate(choices, start=1):
            column += choice
            total += triangle[row][column]
        totals.append(total)
    return max(totals)


def encode(triangle):
    return str(len(triangle)) + "\n" + "".join(" ".join(map(str, row)) + "\n" for row in triangle)


def fixtures():
    sample = [[7], [3, 8], [8, 1, 0], [2, 7, 4, 4], [4, 5, 2, 6, 5]]
    assert path_total(sample) == 30
    yield encode(sample), 30
    # Exhaust every 0/1 assignment for all triangles through three rows.
    for rows in range(1, 4):
        cells = rows * (rows + 1) // 2
        for values in itertools.product([0, 1], repeat=cells):
            triangle, start = [], 0
            for length in range(1, rows + 1):
                triangle.append(list(values[start:start + length]))
                start += length
            yield encode(triangle), path_total(triangle)
    for triangle in [
        [[0]], [[100]], [[1], [100, 99], [0, 0, 100]],
        [[1], [2, 2], [3, 3, 3]], [[1], [100, 0], [100, 0, 0]],
        [[1], [0, 100], [0, 0, 100]],
    ]:
        yield encode(triangle), path_total(triangle)
    rng = random.Random(1000)
    for _ in range(180):
        rows = rng.randint(1, 11)
        triangle = [[rng.randint(0, 100) for _ in range(length)] for length in range(1, rows + 1)]
        yield encode(triangle), path_total(triangle)
    yield encode([[100] * length for length in range(1, 1001)]), 100000
    yield encode([[0] * length for length in range(1, 1001)]), 0
    yield encode([[100] + [0] * (length - 1) for length in range(1, 1001)]), 100000
    yield encode([[0] * (length - 1) + [100] for length in range(1, 1001)]), 100000


def invalid_inputs():
    return ["0\n", "1001\n", "1\n-1\n", "1\n101\n", "2\n1\n2\n", "1\nword\n", "1\n1\nextra\n", "-1\n"]

def verify(compiler, mode):
    flags = ["-std=c++20", "-Wall", "-Wextra", "-Wpedantic", "-Werror", "-O1"]
    if mode == "sanitized":
        flags += ["-g", "-fsanitize=address,undefined", "-fno-omit-frame-pointer"]
        if os.uname().sysname == "Linux":
            flags += ["-fno-pie", "-no-pie"]
    environment = {
        **os.environ,
        "ASAN_OPTIONS": "detect_leaks=" + ("1" if os.uname().sysname == "Linux" else "0") + ":halt_on_error=1",
        "UBSAN_OPTIONS": "halt_on_error=1:print_stacktrace=1",
    }
    legacy = ROOT / LEGACY
    original = legacy.read_bytes()
    cases = list(fixtures())
    processes = 0
    with tempfile.TemporaryDirectory(prefix=BASENAME + "-native-") as directory:
        working = Path(directory)
        reference, learner = working / "reference", working / "learner"
        for role, binary in [("solution", reference), ("starter", learner)]:
            subprocess.run([compiler, *flags, str(ROOT / PACK / role / "main.cpp"), "-o", str(binary)], check=True, timeout=90)
        input_file, output_file = working / (BASENAME + ".in"), working / (BASENAME + ".out")
        def run(binary):
            nonlocal processes
            processes += 1
            return subprocess.run([str(binary)], cwd=working, env=environment, capture_output=True, text=True, timeout=20)
        for number, (data, expected) in enumerate(cases):
            input_file.write_text(data)
            output_file.unlink(missing_ok=True)
            process = run(reference)
            assert process.returncode == 0, (number, process.stderr)
            assert output_file.read_text().split() == [str(expected)], (number, expected, output_file.read_text())
        for data in invalid_inputs():
            input_file.write_text(data)
            output_file.write_text("previous successful attempt\n")
            process = run(reference)
            assert process.returncode == 2 and "Invalid " in process.stderr, process.stderr
            assert output_file.read_text() == "previous successful attempt\n"
        input_file.unlink()
        output_file.unlink()
        process = run(reference)
        assert process.returncode == 2 and "Invalid " in process.stderr and not output_file.exists()
        input_file.write_bytes((ROOT / PACK / "starter" / (BASENAME + ".in")).read_bytes())
        for prior_output in [None, "saved earlier output\n"]:
            if prior_output is None:
                output_file.unlink(missing_ok=True)
            else:
                output_file.write_text(prior_output)
            process = run(learner)
            assert process.returncode == 2 and "Complete " in process.stderr, process.stderr
            assert not output_file.exists() if prior_output is None else output_file.read_text() == prior_output
    assert legacy.read_bytes() == original
    print(json.dumps({
        "pack": PACK, "compiler": compiler, "mode": mode,
        "fileIOCases": len(cases), "nativeProcesses": processes,
        "independentOracle": ORACLE, "maximumSizeCases": LARGE_CASES,
        "unfinishedLearnerWritesNoAnswer": True, "failedRunPreservesPreviousOutput": True,
        "legacyByteSha256": hashlib.sha256(original).hexdigest(),
        "legacyBytesPreserved": True,
        "sourceHashes": {role: hashlib.sha256((ROOT / PACK / role / "main.cpp").read_bytes().replace(b"\r\n", b"\n")).hexdigest() for role in ["starter", "solution"]},
    }, sort_keys=True))


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--compiler", default="c++")
    parser.add_argument("--mode", choices=["ordinary", "sanitized"], default="ordinary")
    args = parser.parse_args()
    verify(args.compiler, args.mode)
