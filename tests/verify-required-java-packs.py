"""Exercise real Java role I/O, unfinished tasks and independent legal models."""
import argparse
import datetime
import hashlib
import itertools
import json
import os
from pathlib import Path
import random
import signal
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
TASK = os.environ.get("CODEX_PARENT_TASK_ID", "silver-required-java-source-acceptance")
PACKS = [
    ("US25-Why-Did-the-Cow-Cross-the-Road-II", "Main.java", "maxcross.in", "maxcross.out", "maxcross.expected"),
    ("US21-Cow-Dance-Show", "Main.java", "cowdance.in", "cowdance.out", "cowdance.expected"),
    ("US38-Redistributing-Gifts", "src/main/java/Main.java", "input.txt", None, "sample.expected"),
]
PROCESSES = 0


def now():
    return datetime.datetime.now(datetime.timezone.utc).isoformat()


def run(command, cwd, data=None, expected_exit=0, timeout=12):
    global PROCESSES
    start = now()
    process = subprocess.Popen(command, cwd=cwd, stdin=subprocess.PIPE,
                               stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                               start_new_session=True)
    PROCESSES += 1
    cleanup = "not required"
    try:
        out, err = process.communicate(data, timeout=timeout)
        if process.returncode != expected_exit:
            raise AssertionError((command, process.returncode, expected_exit, err.decode()[:1000]))
        return out, err
    except BaseException:
        cleanup = "process group terminated"
        try:
            os.killpg(process.pid, signal.SIGTERM)
        except ProcessLookupError:
            pass
        try:
            process.communicate(timeout=2)
        except subprocess.TimeoutExpired:
            os.killpg(process.pid, signal.SIGKILL)
            process.communicate()
        raise
    finally:
        print(json.dumps(dict(parentTaskId=TASK, cwd=str(cwd), command=command,
                              pid=process.pid, start=start, end=now(),
                              exitCode=process.returncode, timeoutSeconds=timeout,
                              childProcessGroupCleanup=cleanup)), flush=True)


def preserved_originals():
    manifest = json.loads((ROOT / "tests/fixtures/required-java-originals.json").read_text())
    assert len(manifest["files"]) == 10
    for row in manifest["files"]:
        raw = (ROOT / row["archive"]).read_bytes()
        assert hashlib.sha256(raw).hexdigest() == row["sha256"], row
        blob = b"blob " + str(len(raw)).encode() + b"\0" + raw
        assert hashlib.sha1(blob).hexdigest() == row["gitBlob"], row
        if row["path"].endswith("Main.java") or row["path"].endswith("pom.xml"):
            expected = raw.decode()
            if row["path"] == "US21-Cow-Dance-Show/solution/Main.java":
                expected = expected.replace(
                    "// binary search between 1 and N to find the max possible value of K",
                    "// binary search between 1 and N to find the minimum feasible value of K",
                ).replace(
                    "// priority queues are queues where the elements are inserted with some ordering. takes O(logn) time for insertion and O(1) time for polling",
                    "// The min-heap stores finish times; insertion and polling each take O(log K) time.",
                )
            assert (ROOT / row["path"]).read_text() == expected, row["path"]
    return manifest["baseline"]


def cross_case(n, k, broken):
    text = f"{n} {k} {len(broken)}\n" + "".join(f"{v}\n" for v in broken)
    answer = min(sum(v in broken for v in range(start, start + k))
                 for start in range(1, n - k + 2))
    return text, f"{answer}\n"


def dance_case(durations, limit):
    # Independent oracle: enumerate K and scan an ordinary slot array.
    for k in range(1, len(durations) + 1):
        slots = [0] * k
        for duration in durations:
            slot = min(range(k), key=lambda i: slots[i])
            slots[slot] += duration
        if max(slots) <= limit:
            return f"{len(durations)} {limit}\n" + "".join(f"{d}\n" for d in durations), f"{k}\n"
    raise AssertionError("Case violates guaranteed feasibility.")


def gifts_case(preferences):
    # Independent oracle: complete legal permutations, best per cow separately.
    n = len(preferences)
    ranks = [{gift: rank for rank, gift in enumerate(row)} for row in preferences]
    best = list(range(1, n + 1))
    for assignment in itertools.permutations(range(1, n + 1)):
        if any(ranks[cow][gift] > ranks[cow][cow + 1] for cow, gift in enumerate(assignment)):
            continue
        for cow, gift in enumerate(assignment):
            if ranks[cow][gift] < ranks[cow][best[cow]]:
                best[cow] = gift
    return str(n) + "\n" + "".join(" ".join(map(str, row)) + "\n" for row in preferences), "".join(f"{gift}\n" for gift in best)


def reference_cases(index, sample, sample_answer):
    rng = random.Random(20261011 + index)
    yield "official-sample", sample, sample_answer, False
    if index == 0:
        for n in [1, 2, 5, 10, 25]:
            for k in sorted({1, n, max(1, n // 2)}):
                for broken in [list(range(1, n + 1)), rng.sample(range(1, n + 1), rng.randint(1, n))]:
                    yield "independent-window", *cross_case(n, k, broken), False
        n = 100000
        yield "all-broken-full-window", f"{n} {n} {n}\n" + "".join(f"{i}\n" for i in range(1, n + 1)), f"{n}\n", True
        yield "one-broken-full-window", f"{n} {n} 1\n50000\n", "1\n", True
        yield "alternating-near-full-window", f"{n} {n-1} {n//2}\n" + "".join(f"{i}\n" for i in range(2, n + 1, 2)), "49999\n", True
    elif index == 1:
        for durations, limit in [([7], 7), ([3, 3, 3, 3], 6), ([3, 3, 3, 3], 3), ([4, 1, 1], 6)]:
            yield "schedule-boundary", *dance_case(durations, limit), False
        for _ in range(24):
            durations = [rng.randint(1, 40) for _ in range(rng.randint(1, 9))]
            limit = rng.randint(max(durations), sum(durations))
            yield "independent-slot-model", *dance_case(durations, limit), False
        n = 10000
        yield "one-wave-max-durations", f"{n} 100000\n" + "100000\n" * n, f"{n}\n", True
        yield "ten-waves-max-durations", f"{n} 1000000\n" + "100000\n" * n, "1000\n", True
        yield "unit-durations", f"{n} 1000000\n" + "1\n" * n, "1\n", True
    else:
        repeated = gifts_case([[2, 3, 1], [1, 3, 2], [1, 2, 3]])
        assert repeated[1] == "2\n1\n1\n"
        yield "separate-best-gifts-can-repeat", *repeated, False
        for n in range(1, 7):
            for _ in range(5):
                yield "exhaustive-legal-assignments", *gifts_case([rng.sample(range(1, n + 1), n) for _ in range(n)]), False
        n = 500
        yield "common-ranking-forces-own", str(n) + "\n" + (" ".join(map(str, range(1, n + 1))) + "\n") * n, "".join(f"{i}\n" for i in range(1, n + 1)), True
        rows = []
        for cow in range(1, n + 1):
            nxt = cow % n + 1
            rows.append([nxt, cow] + [i for i in range(1, n + 1) if i not in [nxt, cow]])
        yield "all-first-choices-one-cycle", str(n) + "\n" + "".join(" ".join(map(str, row)) + "\n" for row in rows), "".join(f"{cow%n+1}\n" for cow in range(1, n + 1)), True


def invalid_inputs(index):
    if index == 0:
        return ["", "1 1", "1 1 0\n", "0 1 1\n1\n", "100001 1 1\n1\n", "3 4 1\n1\n", "3 1 4\n1\n", "3 1 2\n1\n1\n", "3 1 1\n0\n", "3 1 1\n4\n", "3 1 1\nx\n", "1 1 1\n1\n2\n"]
    if index == 1:
        return ["", "1", "0 10\n", "10001 10\n", "1 0\n1\n", "1 1000001\n1\n", "1 10\n0\n", "1 1000000\n100001\n", "1 5\n6\n", "2 5\n1\n", "1 5\nx\n", "1 5\n1\n2\n"]
    return ["", "0\n", "501\n", "2\n1 2\n", "2\n1 1\n1 2\n", "2\n0 2\n1 2\n", "2\n3 2\n1 2\n", "1\nx\n", "1\n1\n2\n"]


def compile_role(args, folder, role, source, classes, workspace):
    classes.mkdir(parents=True)
    run([args.javac, "--release", "17", "-encoding", "UTF-8", "-Xlint:all", "-Werror",
         "-d", str(classes), str(ROOT / folder / role / source)], workspace, timeout=30)
    return [args.java, "-ea", "-Xmx256m", "-cp", str(classes), "Main"]


def check_refusal(command, workspace, input_name, output_name, data, existing):
    if output_name:
        (workspace / input_name).write_text(data)
        output = workspace / output_name
        if existing:
            output.write_bytes(b"previous answer\n")
        else:
            output.unlink(missing_ok=True)
        out, err = run(command, workspace, expected_exit=2)
        assert out == b"" and b"No answer written:" in err
        assert output.read_bytes() == b"previous answer\n" if existing else not output.exists()
    else:
        # Follow the documented shell semantics: redirect to temporary output,
        # and replace the prior answer only when the Java command succeeds.
        answer = workspace / "answer.txt"
        if existing:
            answer.write_bytes(b"previous answer\n")
        else:
            answer.unlink(missing_ok=True)
        out, err = run(command, workspace, data.encode(), expected_exit=2)
        (workspace / "answer.tmp").write_bytes(out)
        assert out == b"" and b"No answer written:" in err
        (workspace / "answer.tmp").unlink()
        assert answer.read_bytes() == b"previous answer\n" if existing else not answer.exists()


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--java", default="java")
    parser.add_argument("--javac", default="javac")
    parser.add_argument("--receipt", type=Path)
    args = parser.parse_args()
    baseline = preserved_originals()
    results = []
    with tempfile.TemporaryDirectory(prefix="silver-required-java-") as temp:
        scratch = Path(temp)
        version, stderr = run([args.java, "-version"], scratch)
        for index, (folder, source, input_name, output_name, expected_name) in enumerate(PACKS):
            workspace = scratch / folder
            workspace.mkdir()
            sample = (ROOT / folder / "solution" / input_name).read_text()
            assert (ROOT / folder / "starter" / input_name).read_text() == sample
            expected = (ROOT / folder / "solution" / expected_name).read_text()
            reference = compile_role(args, folder, "solution", source, workspace / "reference-classes", workspace)
            learner = compile_role(args, folder, "starter", source, workspace / "learner-classes", workspace)
            accepted = []
            refusal_count = 0
            for name, text, answer, full in reference_cases(index, sample, expected):
                if output_name:
                    (workspace / input_name).write_text(text)
                    (workspace / output_name).unlink(missing_ok=True)
                out, _ = run(reference, workspace, None if output_name else text.encode())
                actual = (workspace / output_name).read_text() if output_name else out.decode()
                assert actual == answer, (folder, name, answer[:200], actual[:200])
                accepted.append(dict(name=name, atMaximumN=full,
                                     inputSha256=hashlib.sha256(text.encode()).hexdigest(),
                                     expectedSha256=hashlib.sha256(answer.encode()).hexdigest()))
                if name == "official-sample" or full:
                    for existing in [False, True]:
                        check_refusal(learner, workspace, input_name, output_name, text, existing)
                        refusal_count += 1
            for text in invalid_inputs(index):
                check_refusal(learner, workspace, input_name, output_name, text, True)
                refusal_count += 1
            if output_name:
                (workspace / input_name).unlink()
                out, err = run(learner, workspace, expected_exit=2)
                assert out == b"" and b"No answer written:" in err
                assert (workspace / output_name).read_bytes() == b"previous answer\n"
                refusal_count += 1
            probe = ROOT / "tests/fixtures/required-java" / (str(index) + "-LearnerProbe.java.txt")
            (workspace / "LearnerProbe.java").write_bytes(probe.read_bytes())
            run([args.javac, "--release", "17", "-encoding", "UTF-8", "-Xlint:all", "-Werror",
                 "-cp", str(workspace / "learner-classes"), "-d", str(workspace / "learner-classes"),
                 "LearnerProbe.java"], workspace, timeout=30)
            out, _ = run([*learner[:-1], "LearnerProbe"], workspace)
            assert out == b"Supplied helpers and unfinished tasks verified.\n"
            results.append(dict(folder=folder, sourcePath=source, nativeInput=input_name if output_name else "stdin",
                                nativeOutput=output_name or "stdout", referenceCases=accepted,
                                learnerRefusalCases=refusal_count, suppliedHelpersVerified=True,
                                referenceSha256=hashlib.sha256((ROOT / folder / "solution" / source).read_text().encode()).hexdigest(),
                                learnerSha256=hashlib.sha256((ROOT / folder / "starter" / source).read_text().encode()).hexdigest()))
        receipt = dict(baseline=baseline, runtime=(version + stderr).decode().strip(),
                       compiledRoles=6, originalFilesVerified=10, packs=results,
                       referenceCases=sum(len(r["referenceCases"]) for r in results),
                       officialSamples=3, maximumNCases=8,
                       learnerRefusalCases=sum(r["learnerRefusalCases"] for r in results),
                       processCount=PROCESSES, temporaryFilesCleaned=True,
                       verifiedAt=now(), scope="Native source-role acceptance; website import/export and production acceptance are separate.")
    if args.receipt:
        args.receipt.write_text(json.dumps(receipt, indent=2) + "\n")
    print(json.dumps(receipt), flush=True)


if __name__ == "__main__":
    main()
