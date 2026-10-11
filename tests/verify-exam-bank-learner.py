"""Verify the unfinished teaching pack and immutable reference without site claims."""
import argparse
import hashlib
import importlib.util
import json
from pathlib import Path
import shlex
import tempfile

ROOT = Path(__file__).resolve().parents[1]
PACK = ROOT / "US4-Exam-and-Bank-Account-Classes"
spec = importlib.util.spec_from_file_location("native", ROOT / "tests/verify-required-java-packs.py")
native = importlib.util.module_from_spec(spec)
spec.loader.exec_module(native)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--java", required=True)
    parser.add_argument("--javac", required=True)
    args = parser.parse_args()
    originals = json.loads((ROOT / "tests/fixtures/exam-bank-learner-originals.json").read_text())
    for row in originals["files"]:
        raw = (ROOT / row["archive"]).read_bytes()
        assert hashlib.sha256(raw).hexdigest() == row["sha256"]
        assert hashlib.sha1(b"blob " + str(len(raw)).encode() + b"\0" + raw).hexdigest() == row["gitBlob"]
    reference = originals["reference"]
    reference_raw, reference_error = native.run(["git", "show", "HEAD:" + reference["path"]], ROOT)
    assert reference_error == b""
    assert hashlib.sha256(reference_raw).hexdigest() == reference["sha256"]
    assert hashlib.sha1(b"blob " + str(len(reference_raw)).encode() + b"\0" + reference_raw).hexdigest() == reference["gitBlob"]
    # Working checkouts can have CRLF; compare their text to the exact accepted Git blob.
    assert (ROOT / reference["path"]).read_text() == reference_raw.decode().replace("\r\n", "\n")
    expected = (PACK / "solution/sample.expected").read_bytes()
    assert (PACK / "starter/sample.expected").read_bytes() == expected
    source_hashes = {role: hashlib.sha256((PACK / role / "Main.java").read_bytes()).hexdigest() for role in ["starter", "solution"]}
    for role in ["starter", "solution"]:
        guide = (PACK / role / "README.md").read_text()
        assert "../README.md" not in guide
        assert all(part in guide for part in ["## Contract", "## Worked example", "## Learner tasks", "## Independent checks", "## Native workflow", "javac --release 17", "Integer.MIN_VALUE", "Integer.MAX_VALUE"])

    with tempfile.TemporaryDirectory(prefix="silver-exam-bank-learner-") as temporary:
        workspace = Path(temporary)
        for role in ["starter", "solution"]:
            folder = workspace / role
            folder.mkdir()
            classes = folder / "target/classes"
            classes.mkdir(parents=True)
            (folder / "Main.java").write_bytes((PACK / role / "Main.java").read_bytes())
            native.run([args.javac, "--release", "17", "-encoding", "UTF-8", "-Xlint:all", "-Werror", "-d", str(classes), "Main.java"], folder, timeout=30)
            prior = b"An earlier attempt remains available.\n"
            (folder / "answer.txt").write_bytes(prior)
            out, err = native.run([args.java, "-cp", str(classes), "Main"], folder, expected_exit=2 if role == "starter" else 0)
            if role == "starter":
                assert out == b"" and b"TASK 1" in err
                # Exercise each task directly, including when the other task has not run.
                source = folder / "Main.java"
                source.write_text(source.read_text() + '''
class LearnerTaskProbe {
    public static void main(String[] args) {
        try {
            if (args[0].equals("exam")) {
                new Exam("A", Integer.MIN_VALUE).compareTo(new Exam("B", Integer.MAX_VALUE));
            } else {
                Main.compareBankAccounts(new BankAccount("A", Integer.MIN_VALUE), new BankAccount("B", Integer.MAX_VALUE));
            }
            throw new AssertionError("The learner task was unexpectedly completed.");
        } catch (UnsupportedOperationException unfinished) {
            System.err.println(unfinished.getMessage());
            System.exit(2);
        }
    }
}
''')
                # Keep package-private teaching classes and the probe in one source file.
                native.run([args.javac, "--release", "17", "-encoding", "UTF-8", "-Xlint:all", "-Werror", "-d", str(classes), "Main.java"], folder, timeout=30)
                for task, marker in [("exam", b"TASK 1"), ("bank", b"TASK 2")]:
                    task_out, task_err = native.run([args.java, "-cp", str(classes), "LearnerTaskProbe", task], folder, expected_exit=2)
                    assert task_out == b"" and marker in task_err
            else:
                assert out == expected and err == b""
            assert (folder / "answer.txt").read_bytes() == prior
            (folder / "sample.expected").write_bytes(expected)
            guide = (PACK / role / "README.md").read_text()
            commands = guide.split("```sh\n", 1)[1].split("```", 1)[0]
            assert commands.count("javac --release ") == commands.count("java -cp ") == 1
            commands = commands.replace("javac --release ", shlex.quote(args.javac) + " --release ")
            commands = commands.replace("java -cp ", shlex.quote(args.java) + " -cp ")
            native.run(["bash", "-c", commands], folder, expected_exit=2 if role == "starter" else 0, timeout=30)
            assert not (folder / "answer.new").exists()
            assert (folder / "answer.txt").read_bytes() == (prior if role == "starter" else expected)
            # Leave compiled classes present, then break the current source.
            # The printed command must fail before an older build can replace an answer.
            (folder / "Main.java").write_text("class Main { invalid source\n")
            (folder / "answer.txt").write_bytes(prior)
            failed_out, failed_err = native.run(["bash", "-c", commands], folder, expected_exit=1, timeout=30)
            assert failed_out == b"" and b"error:" in failed_err
            assert not (folder / "answer.new").exists()
            assert (folder / "answer.txt").read_bytes() == prior
        version, error = native.run([args.java, "-version"], workspace)
    for role, digest in source_hashes.items():
        assert hashlib.sha256((PACK / role / "Main.java").read_bytes()).hexdigest() == digest
    print(json.dumps(dict(event="verified-silver-exam-bank-learner", sourceBaseline=originals["baseline"], runtime=(version + error).decode().strip(), preservedMigrationGuides=2, referenceSourceUnchanged=True, strictJava17Compilation=True, refusalCases=3, documentedNativeGuards=2, failedCompilationGuards=2, referenceMatchesExpected=True, priorAnswerPreserved=True, sourceSha256=source_hashes, temporaryFilesCleaned=True, scope="Source roles only; website imports remain a separate acceptance.")), flush=True)


if __name__ == "__main__":
    main()
