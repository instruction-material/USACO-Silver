# Counting Haybales: inclusive interval counting

This core range-counting project follows the [official 2016 December Silver statement](https://usaco.org/index.php?page=viewproblem2&cpid=666). The new C++20 starter and reference replace an empty source wrapper; they are independently authored implementations.

## Contract and model

Read `haybales.in`: `N Q`, then N distinct positions, then Q inclusive intervals `A B`. N and Q are between 1 and 100,000; positions and endpoints are between 0 and 1,000,000,000, with A <= B. Write one count per line to `haybales.out`.

Sort positions once. A search result is an insertion index from 0 through N, rather than a haybale value. `firstAtLeast(A)` locates the first position >= A; `firstGreater(B)` locates the first position > B. Their difference counts positions in [A, B]. Searching for the first value strictly above B avoids having to form B+1.

## Guided implementation

1. Trace both boundaries on sorted positions `[2, 3, 5, 7]` for [2, 5], [4, 6], and [8, 10]. Explain why N is a valid return value.
2. Complete the two marked binary-search helpers in `starter/main.cpp`. Keep a half-open search region `[low, high)`; each update must shrink it.
3. Run the supplied sample. Its output is `2, 2, 3, 4, 1, 0`, each on a separate line.
4. Check singleton intervals on and between positions, both inclusive endpoints, and ranges entirely below or above the positions. Compare small cases with direct counting.
5. Explain O(N log N + Q log N) time and O(N + Q) space for this driver, including its buffered answers.

The file reader, sorting, count formula, and output driver are provided. The binary searches remain unfinished. `solution/` is a separate completed reference; copying it replaces the learning task. The course's optional Counting Haybales worksheet reuses this project for additional endpoint checks.

## Build and run

From `starter/` (or `solution/`), compile with `c++ -std=c++20 -Wall -Wextra -Wpedantic main.cpp -o haybales` and run `./haybales`. The program reads the `.in` file from the current directory and writes the `.out` file there. The untouched starter exits with a TODO message and creates no answer file. Delete stale `.out` files before checking another attempt.

After an attempt, compare the two search conditions with the reference and explain every boundary update. The repository acceptance check runs actual file I/O against an independent counting oracle, with empty-result, extreme-coordinate, random, and maximum-size cases.
