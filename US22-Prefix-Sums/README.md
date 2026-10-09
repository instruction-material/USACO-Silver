# Prefix sums: a zero sentinel and half-open ranges

This optional range-sum worksheet reinforces the course's prefix-sum model. It is a course-authored exercise, with newly supplied C++20 starter and reference packs.

## Contract and model

Read `prefix.in`: `N Q`, N values, then Q index pairs `left right`. N and Q are between 0 and 100,000. Values are between -1,000,000,000 and 1,000,000,000. Each query obeys `0 <= left <= right <= N` and requests the sum of indices in `[left, right)`. Write one signed total per line to `prefix.out`.

Define `prefix[k]` as the sum of the first k values. Consequently `prefix[0] = 0`, the prefix array contains N+1 entries, and `prefix[i+1] = prefix[i] + values[i]`. Subtracting `prefix[left]` from `prefix[right]` removes the values before left. The right endpoint is excluded. Empty ranges return zero; `[0, N)` covers the entire array. Signed 64-bit totals safely accommodate the largest allowed magnitude, 100,000,000,000,000.

## Guided implementation

1. Construct the prefix table for `[3, -2, 7, 0, 4]` by hand. Explain what each of its six entries means.
2. Complete `makePrefix` and `rangeSum` in `starter/main.cpp`. Preserve the zero sentinel and the declared endpoint convention.
3. Run the sample: the five output lines are `0, -2, 12, 7, 0`.
4. Test an empty array, an empty range in the middle, a singleton, the full range, negative totals, and totals exceeding a 32-bit integer. For small arrays compare every allowed range with direct addition.
5. Explain O(N+Q) total time and O(N+Q) space for the provided driver. Query work is O(1) after O(N) preprocessing; input changes require rebuilding this static prefix table.

The supplied driver reads and validates intervals, then writes results. Both prefix helpers remain unfinished. `solution/` is a separate completed reference for checking the invariant after an attempt. This worksheet supplements the core range unit; it does not introduce dynamic point updates.

## Build and run

From `starter/` (or `solution/`), compile with `c++ -std=c++20 -Wall -Wextra -Wpedantic main.cpp -o prefix` and run `./prefix`. Use the supplied input in that directory. The untouched starter reports an unfinished helper and creates no answer file. Delete stale `prefix.out` before checking a new attempt.

The source acceptance check executes actual file I/O and compares outputs with independent slice sums, including empty, negative, exhaustive small-range, and maximum-size cases.
