# USACO Silver

Source material for the Silver course. The following C++20 projects provide separate learner and completed-reference packs with file fixtures and assignment guides:

- [Counting Haybales](US18-Counting-Haybales/README.md): core binary-search interval counting with inclusive endpoints.
- [Priority queues](US21-Priority-Queues/README.md): core heap practice with stable arrival-order ties.
- [Prefix sums](US22-Prefix-Sums/README.md): optional practice with a zero sentinel and half-open index ranges.

Each guide explains the contract, predictions, learner tasks, examples, independent checks, and native build command. Start with the `starter/` pack, retain the learner attempt, and compare the separate `solution/` reference after an attempt. The other course folders retain their existing material.

Repository acceptance: `python3 tests/verify-silver-packs.py --compiler c++ --mode ordinary`. Use `--mode sanitized` for address and undefined-behavior checks. The pinned hosted workflow repeats both modes with GCC and Clang. Checks compile both roles, execute actual file I/O, compare independent models, cover the advertised limits, and require untouched learner tasks to remain unfinished.

## Required Java contest packs

- [Why Did the Cow Cross the Road II](US25-Why-Did-the-Cow-Cross-the-Road-II/README.md): contiguous repair counts, native maxcross.in/out.
- [Cow Dance Show](US21-Cow-Dance-Show/README.md): ordered heap scheduling and the minimum feasible stage size, native cowdance.in/out.
- [Redistributing Gifts](US38-Redistributing-Gifts/README.md): acceptable exchange cycles and separate per-cow best possibilities, native stdin/stdout with the existing nested source layout.

Each has a separate unfinished Java 17+ learner, completed reference, official sample, independent checks, and complete assignment/run guide. Original migration/source bytes are retained under each pack’s legacy/2026-10-11 directory. The maintained Cross the Road II and Gifts references are unchanged; Cow Dance has two comment corrections and no algorithm changes.

Run `python3 tests/verify-required-java-packs.py --java java --javac javac`. The hosted workflow repeats the native acceptance on JDK 17 and 21. These are source packs; website imports/exports and production activation have separate acceptance gates.
