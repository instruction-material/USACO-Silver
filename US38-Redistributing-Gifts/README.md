# Redistributing Gifts

Required Silver assignment: model acceptable exchanges as a directed graph and
use reachability to identify cycles. A cow wanting a gift does not alone make
it obtainable: some complete legal exchange must supply it. Begin with
`starter/`, retain the attempt, then compare `solution/`. Pause at predictions,
edge direction, the cycle argument and independent checks with an instructor.

## Contract and output meaning

[Official problem: February 2022 Silver, problem 1](https://usaco.org/index.php?page=viewproblem2&cpid=1206).
There are N cows and gifts, numbered 1..N. Cow i initially owns gift i.
Each cow lists a permutation of gift IDs, best first. A legal reassignment
uses each gift once and gives no cow a gift worse than its original one.
For each cow, output its best gift among all legal reassignments.

- Standard input: N followed by N rows of N preference integers.
- Limits: 1 <= N <= 500; each row is a permutation of 1..N.
- Standard output: N gift IDs, one per line, in cow order.
- Each line describes a separate best possibility. Output IDs may repeat and
  need not themselves form one legal reassignment. A candidate is possible
  only if some full legal reassignment gives that cow that gift.

The reader validates bounds, exact integer count and permutations. Arrays use
zero-based cow/rank indices and retain gift IDs in 1..N. The supplied `rankOf`
returns a zero-based rank; a smaller value means a better gift.

## Predict and distinguish

The supplied `input.txt` contains the official sample:

```text
4
1 2 3 4
1 3 2 4
1 2 3 4
1 2 3 4
```

The answer is `1`, `3`, `2`, `4` on separate lines. Cows 2 and 3 can exchange
gifts; cows 1 and 4 cannot improve. Explain why cow 3 wanting gift 1 does not
make it obtainable.

Also predict this teaching case:

```text
3
2 3 1
1 3 2
1 2 3
```

Its answer is `2`, `1`, `1`. Assignment (2,1,3) gives cow 2 its favorite;
assignment (2,3,1) gives cow 3 its favorite. Both are legal, but the reported
best possibilities (2,1,1) cannot occur together. Formatting must allow repeats.
For N=1 the answer is 1. If every cow ranks its own gift first, no cow improves.
If all first choices form one cycle, rotating gifts attains those first choices.

## Learner tasks and reasoning

Complete `reachability` and `bestGifts` in `starter/src/main/java/Main.java`.

1. Define an edge i -> j when cow i accepts gift j, including its own gift.
   Exclude gifts ranked after its own. Draw both teaching graphs first.
2. Gift j is possible for cow i when acceptable edge i -> j has a return path
   j -> i. Rotate gifts around that cycle and leave other cows unchanged.
   Conversely, every legal full reassignment decomposes into acceptable cycles.
   This establishes both directions of the criterion.
3. Compute transitive reachability with Floyd-Warshall's intermediate vertex
   as the outer loop. State which paths are represented after each iteration.
   N<=500 permits O(N³) time and O(N²) space.
4. For each cow scan its preferences, best first, through its own gift. Return
   the first acceptable gift with a return path. Its own gift ensures an answer
   exists. Preserve one edge direction and explain the ID/index conversions.
5. Keep answers in cow order. `formatAnswers` checks length, range and that
   each gift is no worse than that cow's original. It allows repeated IDs and
   does not grade reachability or optimality.

Untouched work exits 2 and prints only a diagnostic to standard error; the
entire answer is buffered before writing standard output. The unchanged
reference stores reversed acceptable edges, owner -> recipient. The cycle
criterion is equivalent, but restate its indices before comparing it.

## Native workflow and nested source

Use Java 17 or newer. Both roles retain `src/main/java/Main.java`. The reference
keeps its historical Maven file unchanged; the new learner needs only the JDK.
Direct compilation avoids resolving the unused historical dependency.
From the chosen `starter/` directory:

```sh
mkdir -p target/classes
javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d target/classes src/main/java/Main.java &&
  java -ea -cp target/classes Main < input.txt
```

Use the same commands in `solution/` for the reference; `sample.expected`
contains the official result. There is no native `gifts.in`/`gifts.out` contract.
To preserve an earlier answer file on unfinished/invalid input, replace it only
after a successful run:

```sh
if java -ea -cp target/classes Main < input.txt > answer.tmp; then
  mv answer.tmp answer.txt
else
  rm -f answer.tmp
fi
```

Direct `> answer.txt` redirection truncates an old answer before Java runs.
Use the guarded form when retaining attempts. Keep role sources/classes
separate. A configured site editor can export the whole selected role; source
acceptance does not prove a completed site import or browser Input execution.

## Independent checks and transfer

For N<=6 enumerate all N! assignments, reject any that leave a cow worse off,
and record each cow's best gift independently across the remaining assignments.
This oracle uses neither graph reachability nor Floyd-Warshall. Include the
repeated-output teaching case, own-first rankings, asymmetric wishes and cycles.

At N=500 with every row 1..500, cow 1 must keep gift 1, then cow 2 must keep
gift 2, and so on. All cows retain their own gifts. A single cycle of first
choices gives every cow its first choice. The verifier checks both constructions
and legal small cases. After comparing the reference, change one row and
explain which return paths disappear.

The original [guide](legacy/2026-10-11/README.md),
[placeholder](legacy/2026-10-11/starter-README.md),
[reference bytes](legacy/2026-10-11/reference.java.txt) and
[Maven file](legacy/2026-10-11/pom.xml.txt) are preserved. The maintained
reference/Maven file are unchanged. Repository acceptance:
`python3 tests/verify-required-java-packs.py --java java --javac javac`.
