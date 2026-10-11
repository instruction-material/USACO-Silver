# Why Did the Cow Cross the Road II

Required Silver assignment: use prefix counts or a sliding window to find the
cheapest contiguous block. Earlier prefix-sum practice supplies the tool; this
project applies it to a complete contest problem. Begin with `starter/`, retain
the attempt, then compare the separate `solution/`. Predictions, invariants and
independent checks below also provide instructor pause points.

## Contract

[Official problem: February 2017 Silver, problem 2](https://usaco.org/index.php?page=viewproblem2&cpid=715).
Signals are numbered 1 through N; exactly B distinct signals are broken.
Repair as few as possible to obtain at least K consecutive working signals.
It suffices to check blocks of exactly K, since every longer block contains one.

- Read `maxcross.in` from the process's working directory: N, K, B, then B broken
  signal IDs. Their input order is irrelevant.
- Limits: 1 <= N <= 100000, 1 <= K,B <= N; IDs are distinct and in 1..N.
- Write the minimum repair count and a newline to `maxcross.out`. Zero is a
  valid answer even though the input has at least one broken signal.

The supplied reader builds a boolean array indexed by signal ID, with zero
unused. It rejects missing/extra integers, duplicate/out-of-range IDs and inputs
outside the limits before trying to write an answer.

## Predict first

Both roles contain the official `maxcross.in`:

```text
10 6 5
2
10
1
5
9
```

Blocks 1..6, 2..7, 3..8, 4..9 and 5..10 need 3, 2, 1, 2 and 3 repairs.
The answer is `1`: block 3..8 requires only signal 5 to be repaired.
Before running code, explain these additional cases:

| N, K, B | Broken IDs | Answer | Boundary |
| --- | --- | --- | --- |
| 1, 1, 1 | 1 | 1 | Single signal. |
| 5, 3, 2 | 2, 1 | 0 | Last block already works. |
| 4, 2, 4 | 4, 2, 1, 3 | 2 | All signals broken. |
| 5, 5, 1 | 3 | 1 | Only one possible block. |

## Learner tasks

Complete `minimumRepairs(InputData input)` in `starter/Main.java`.

1. State why legal starts are 1 through N-K+1, including both end blocks.
2. Choose prefix counts or a rolling count. If P[t] counts broken signals in
   1..t and P[0]=0, block l..r has P[r]-P[l-1] broken signals. State that
   invariant before filling the array. A rolling count removes the departing
   signal and adds the entering one exactly once.
3. Evaluate every K-length block and retain its smallest broken count. Explain
   why this count equals the necessary repair count for that block.
4. Target O(N) time and O(N) memory with the supplied boolean array. Recounting
   every entire block costs O(NK), which is too slow at the maximum size.

Untouched learner work throws an unfinished-task exception and exits 2 with a
standard-error diagnostic. It creates no answer and preserves an existing
`maxcross.out`. The answer file opens only after parsing and learner work succeed.
A returned result is checked only for its possible range, not its optimality.

## Native workflow

Use Java 17 or newer. From the chosen `starter/` directory with `maxcross.in`
beside `Main.java`:

```sh
mkdir -p target/classes
javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d target/classes Main.java &&
  java -ea -cp target/classes Main
```

Run the same commands separately in `solution/` to compare the reference. Its
`maxcross.expected` contains the official sample answer; compare the new output
with `diff -u maxcross.expected maxcross.out`. Keep source/classes for the two
roles separate. The working directory supplies the native file; a browser Input
panel does not. When a site import is configured, export the whole selected
role before native execution. Source acceptance alone does not prove that site
import is configured or accepted.

## Independent checks and transfer

For small N, independently list all blocks and count their broken IDs directly.
Compare against the learner using unsorted IDs, K=1, K=N, all signals broken
and a last-block optimum. At N=100000, K=N and all signals broken require
100000 repairs. The verifier also checks a near-full alternating pattern.

After preserving the attempt, read the reference: it sorts broken IDs and builds
prefix counts, costing O(B log B + N) time and O(N+B) memory. Its code is
unchanged. Match its prefix entries to the learner invariant, then construct a
new input whose best block lies at the opposite end of the road.

Original [guide](legacy/2026-10-11/README.md),
[starter placeholder](legacy/2026-10-11/starter-README.md) and
[reference bytes](legacy/2026-10-11/reference.java.txt) remain preserved.
From the repository root run
`python3 tests/verify-required-java-packs.py --java java --javac javac`.
