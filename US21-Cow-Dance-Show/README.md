# Cow Dance Show

Required Silver assignment: simulate ordered arrivals with a min-heap, then
binary-search the smallest feasible stage size. Earlier priority-queue exercises
supply the tool; this task adds a schedule invariant and monotone predicate.
Begin with `starter/`, preserve the attempt, then compare `solution/`.
Predictions, invariants and checks provide instructor pause points as well as
complete independent reading.

## Contract

[Official problem: January 2017 Silver, problem 1](https://usaco.org/index.php?page=viewproblem2&cpid=690).
A stage holds K cows. The first K start at time zero; whenever one finishes,
the next waiting cow immediately starts. Preserve the given arrival order.
The show ends when its last cow finishes. Find the smallest K whose finish
time is at most Tmax, including equality.

- Read `cowdance.in`: N and Tmax, followed by N durations in order.
- Limits: 1 <= N <= 10000, 1 <= Tmax <= 1000000; durations are in 1..100000.
  K=N is guaranteed feasible, so every duration is at most Tmax.
- Write K and a newline to `cowdance.out`, with 1 <= K <= N.
- Do not sort durations or allow a cow to begin before a slot frees.

The reader validates the bounds, exact integer count and K=N guarantee.
These guards check input validity, not scheduling correctness.

## Predict a schedule

The supplied official `cowdance.in` is:

```text
5 8
4
7
8
6
4
```

For K=3, initial finish times are 4, 7 and 8. Cow 4 starts at 4 and finishes
at 10; cow 5 starts at 7 and finishes at 11. The show ends at 11 and fails.
For K=4, cow 5 starts at 4 and finishes at 8; all cows finish by 8.
K=1 takes 29 and K=2 takes 16, so the answer is `4`.

| Durations | Tmax | Minimum K | Boundary |
| --- | --- | --- | --- |
| 7 | 7 | 1 | One cow; exact deadline. |
| 3, 3, 3, 3 | 6 | 2 | Simultaneous departures; two waves. |
| 3, 3, 3, 3 | 3 | 4 | All cows must start immediately. |
| 4, 1, 1 | 6 | 1 | One slot meets the deadline exactly. |

## Learner tasks

Complete both tasks in `starter/Main.java`.

1. Implement `finishesWithin`: the heap holds active cows' finish times. Insert
   the first K durations. For each waiting cow, remove the earliest finish time
   and insert that time plus its duration. Explain why the minimum is the next
   available slot. Repeated equal minima allow simultaneous starts without delay.
2. At the end, compare the greatest remaining finish time with the deadline.
   Test this predicate on the schedules above before implementing the search.
3. Implement `minimumStageSize`. Increasing K cannot increase the finish time;
   feasibility changes from false to true, with possible equal times. Search
   for the first true value. Explain which half retains the answer after each
   midpoint result, and why K=N provides a feasible upper bound.
4. Heap insertion and polling each cost O(log K); `peek` costs O(1). One check
   costs O(N log K) for K>1 and O(N) for K=1. Binary search gives O(N log² N)
   worst-case time and O(N) total memory including input. The duration sum is
   at most 1000000000, so finish times fit a Java `int` for this contract.

Untouched work exits 2 with an unfinished-task diagnostic and preserves an
existing `cowdance.out`. The answer file opens only after parsing and learner
work succeed. The returned K is checked only for range, not feasibility or
minimality.

## Native workflow

Use Java 17 or newer. From the selected `starter/` directory:

```sh
mkdir -p target/classes
javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d target/classes Main.java &&
  java -ea -cp target/classes Main
```

After completing both tasks, inspect the new `cowdance.out`. Run the reference
with these commands in its own `solution/` directory. Compare against its
official `cowdance.expected` using `diff -u cowdance.expected cowdance.out`.
Retain separate sources and compiled classes for learner and reference.

Native execution reads the file in the process's working directory. Browser
Input text does not supply that file. A configured site editor can provide
whole-role export; source restoration does not establish an accepted site
import or native execution inside the browser.

## Independent verification and transfer

For small N, try every K. Use an ordinary array of K finish times; find the
smallest slot by scanning and add the next duration there. Compare the first
feasible K against the learner. This oracle uses neither heap nor binary search.
At N=10000 with all durations 100000, Tmax=100000 requires K=10000 and
Tmax=1000000 requires K=1000. With all durations 1 and Tmax=1000000, K=1
suffices. Include exact deadlines and tied finish times.

After preserving the attempt, compare the reference's search boundaries and
heap state with these invariants. Two misleading comments now correctly state
that the search finds the minimum feasible K and polling costs O(log K).
The algorithm is unchanged. Change only a deadline and predict how K changes.

The [original guide](legacy/2026-10-11/README.md),
[starter placeholder](legacy/2026-10-11/starter-README.md) and
[original reference](legacy/2026-10-11/reference.java.txt) are retained.
Repository acceptance:
`python3 tests/verify-required-java-packs.py --java java --javac javac`.
