# USACO Silver

Source material for the Silver course. The following C++20 projects provide separate learner and completed-reference packs with file fixtures and assignment guides:

- [Counting Haybales](US18-Counting-Haybales/README.md): core binary-search interval counting with inclusive endpoints.
- [Priority queues](US21-Priority-Queues/README.md): core heap practice with stable arrival-order ties.
- [Prefix sums](US22-Prefix-Sums/README.md): optional practice with a zero sentinel and half-open index ranges.

Each guide explains the contract, predictions, learner tasks, examples, independent checks, and native build command. Start with the `starter/` pack, retain the learner attempt, and compare the separate `solution/` reference after an attempt. The other course folders retain their existing material.

Repository acceptance: `python3 tests/verify-silver-packs.py --compiler c++ --mode ordinary`. Use `--mode sanitized` for address and undefined-behavior checks. The pinned hosted workflow repeats both modes with GCC and Clang. Checks compile both roles, execute actual file I/O, compare independent models, cover the advertised limits, and require untouched learner tasks to remain unfinished.
