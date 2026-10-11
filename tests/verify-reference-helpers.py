"""Check actual reference helpers at integer boundaries; preserve original blobs."""
import argparse
import hashlib
import importlib.util
import json
from pathlib import Path
import re
import tempfile

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location(
    "native", ROOT / "tests/verify-required-java-packs.py"
)
native = importlib.util.module_from_spec(spec)
spec.loader.exec_module(native)


def prime(number):
    if number < 2:
        return False
    divisor = 2
    while divisor * divisor <= number:
        if number % divisor == 0:
            return False
        divisor += 1
    return True


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--java", required=True)
    parser.add_argument("--javac", required=True)
    args = parser.parse_args()
    originals = json.loads(
        (ROOT / "tests/fixtures/reference-helper-originals.json").read_text()
    )
    assert len(originals["files"]) == 2
    for row in originals["files"]:
        raw = (ROOT / row["archive"]).read_bytes()
        assert hashlib.sha256(raw).hexdigest() == row["sha256"]
        blob = b"blob " + str(len(raw)).encode() + b"\0" + raw
        assert hashlib.sha1(blob).hexdigest() == row["gitBlob"]

    source_paths = [row["path"] for row in originals["files"]]
    source_hashes = {
        path: hashlib.sha256((ROOT / path).read_bytes()).hexdigest()
        for path in source_paths
    }
    with tempfile.TemporaryDirectory(prefix="silver-reference-helpers-") as temp:
        workspace = Path(temp)
        classes = workspace / "classes"
        classes.mkdir()

        def compile_source(source):
            (workspace / "Main.java").write_text(source)
            native.run(
                [args.javac, "--release", "17", "-encoding", "UTF-8",
                 "-Xlint:all", "-Werror", "-d", str(classes), "Main.java"],
                workspace, timeout=30
            )

        demo = (ROOT / source_paths[0]).read_text()
        exam_names = ["Abdi", "Beyonce", "Camila", "Diego"]
        bank_names = ["Emilia", "Farooq", "Gabrielle", "Hassan"]
        cases = [
            ([95, 100, 87, 87], [1000, 1050, 1200, 1050]),
            ([-2147483648, 2147483647, 0, 2147483647],
             [-2147483648, 2147483647, 0, 2147483647]),
            ([0, 0, 0, 0], [-2147483648] * 4),
            ([-1, 0, 1, -1], [-1, 0, 1, -1]),
        ]
        for scores, balances in cases:
            variant = demo
            for kind, names, values in [
                ("Exam", exam_names, scores),
                ("BankAccount", bank_names, balances),
            ]:
                for name, value in zip(names, values):
                    pattern = rf'new {kind}\("{name}", -?\d+\)'
                    variant, count = re.subn(
                        pattern, f'new {kind}("{name}", {value})', variant
                    )
                    assert count == 1
            # Change only the demonstration inputs in this temporary copy.
            # Python's arbitrary-precision sort is independent of Java subtraction.
            compile_source(variant)
            out, err = native.run(
                [args.java, "-ea", "-cp", str(classes), "Main"], workspace
            )
            exams = sorted(zip(exam_names, scores), key=lambda v: (v[1], v[0]))
            banks = sorted(zip(bank_names, balances), key=lambda v: (-v[1], v[0]))
            expected = "[" + ", ".join(
                f"Name: {name} | score: {score}" for name, score in exams
            ) + "]\n[" + ", ".join(
                f"Name: {name} | balance: {balance}" for name, balance in banks
            ) + "]\n"
            assert out.decode() == expected and err == b""

        source = (ROOT / source_paths[1]).read_text()
        compile_source(source + """
class PrimeReferenceProbe {
    public static void main(String[] args) {
        for (String value : args) {
            System.out.println(pprime.isPrime(Integer.parseInt(value)));
        }
    }
}
""")
        numbers = [-2147483648, -7, 0, 1, 2, 3, 4, 5, 9, 11, 25, 49,
                   97, 121, 2147395600, 2147483647, 2147483646]
        out, err = native.run(
            [args.java, "-ea", "-cp", str(classes), "PrimeReferenceProbe",
             *map(str, numbers)], workspace
        )
        expected = "".join(str(prime(n)).lower() + "\n" for n in numbers)
        assert out.decode() == expected and err == b""
        version, stderr = native.run([args.java, "-version"], workspace)
        runtime = (version + stderr).decode().strip()

    for path in source_paths:
        assert hashlib.sha256((ROOT / path).read_bytes()).hexdigest() == source_hashes[path]
    print(json.dumps(dict(
        event="verified-silver-reference-helpers", sourceBaseline=originals["baseline"],
        runtime=runtime, originalFilesVerified=2, strictJava17Compilation=True,
        sortingDemonstrations=len(cases), primalityHelperCases=len(numbers),
        originalDemonstrationUnchanged=True, referenceSourceUnchanged=True,
        sourceSha256=source_hashes, temporaryFilesCleaned=True,
        scope="Reference helper correction; learner packs and site workflows remain separate."
    )), flush=True)


if __name__ == "__main__":
    main()
