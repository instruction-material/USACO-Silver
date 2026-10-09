# Number Triangles: combine overlapping path choices

This supplemental classical-training project connects exhaustive path search to dynamic programming. Select it after array traversal and complexity analysis in Silver, or as preparation for Gold DP. It is practice rather than a required mock-contest problem.

The [USACO training task](https://train.usaco.org/usacoprob2?S=numtri) is reproduced in this [archive of the original statement](https://jvonk.github.io/usaco/2018/10/04/numtri.html). The programs here are newly authored teaching material; no missing legacy implementation was recovered.

## Contract and example

Read `numtri.in`: a row count from 1 through 1000, followed by one row of one integer, one row of two integers, and so on. Values are 0 through 100. Begin at the top cell, choose one of the two directly adjacent cells below at each step, and end in the last row. Write the largest path total to `numtri.out`.

The preserved five-row input has answer 30. One optimal route has values 7, 3, 8, 7, 5. A locally larger next value need not lead to the best complete path. The greatest possible answer at the advertised limits is 100,000.

## Predict, implement, explain

1. Draw all legal routes for a three-row triangle and compute their sums by hand. Identify which routes share a suffix.
2. Define a state for the best total beginning at a particular cell and ending at the base. The last row supplies the base cases.
3. Write the recurrence using exactly the two allowed children. Explain why any optimal route must choose one of them.
4. Complete `maximumPathSum` in `starter/main.cpp`. The parser, range checks and file writer are supplied. The untouched learner deliberately reports its TODO and writes no answer.
5. Process shorter suffixes before longer ones. If a single vector holds the next row's states, reason about which update direction preserves both child values.
6. Test one row, all zeros, ties, paths along both edges and a case where choosing the larger immediate child loses. Compare small cases with enumeration of every legal route.

There are quadratically many cells, so O(R squared) time is sufficient. The supplied input triangle occupies O(R squared) memory; a rolling state vector uses O(R) additional memory. An optional extension can stream rows and keep only O(R) total memory. State that distinction when describing the implementation.

## Run, check and submit

From either role directory, compile `c++ -std=c++20 -Wall -Wextra -Wpedantic main.cpp -o numtri`, then run `./numtri`. Files use the current working directory. In the site IDE, retain the input file and inspect the newly written output file after Run. Compare the separate reference only after an independent attempt.

Submit the source, the input cases used, their independently predicted outputs and a short explanation of the state, recurrence, update order and memory use. Keep earlier attempts available when opening an optional extension. Repository acceptance enumerates routes for small cases and checks 1000-row cases, malformed input and the untouched learner.
