# Reference pack: Prime Palindromes

This is the preserved completed historical reference. Keep a learner attempt before comparing implementations.

## Contract and candidate structure

Read two integer bounds from `pprime.in`, with `5 <= lower <= upper <= 100000000`. Print every prime decimal palindrome in the inclusive interval, in increasing order, one number per line in `pprime.out`. An empty result is an empty output file. A prime is an integer at least 2 with no divisor other than 1 and itself; a decimal palindrome has the same digits in reverse order, without leading zeroes.

The preserved reference generates candidates with one through eight digits and an odd leading digit, then filters by the bounds and primality. These are odd-valued palindromes, not only odd-length palindromes. Every relevant prime is odd because the interval starts at 5. The only nine-digit value permitted by the upper bound is 100000000, which is not a palindrome. Thus these candidates cover the whole legal interval. The historical training statement is an additional account-dependent resource; this guide includes the complete working contract independently.

For `generatePalindromes(digits)`, where digits is in 1..8, return every odd-valued palindrome with exactly that many digits, once each and in increasing order. Include candidates such as 1 and 99 even though they are not prime; the separate filter handles primality and the requested interval. A digit-length call outside 1..8 is outside this helper contract.

## Worked example and predictions

The included `pprime.in` is `5 500`. Its exact output is:

```text
5
7
11
101
131
151
181
191
313
353
373
383
```

For three digits, seed 13 mirrors to 131 by appending the first digit; for four digits, seed 13 mirrors to 1331 by appending both seed digits in reverse. Do not duplicate the middle digit of an odd-length palindrome. A seed cannot start with zero, and an even leading digit produces an even palindrome that cannot be a prime in this interval.

Predict these inclusive boundary cases before running: `5 5` gives 5; `11 11` gives 11; `12 100` gives an empty file. Although every even-length decimal palindrome is divisible by 11, 11 itself is prime. The learner helper contract still asks for all odd-valued candidates at each length; filtering or an explicitly changed extension may apply that observation afterward.

## Implementation tasks

1. Complete the separate learner's `pprime.generatePalindromes(int digits)`. Choose nested digit loops or enumerate valid half-seeds and mirror them. Keep exactly the requested digit length.
2. For odd lengths, mirror all seed digits except the middle one. For even lengths, mirror the whole seed. Preserve zeroes inside the number; forbid a leading zero.
3. Generate candidates in increasing order and without duplicates. The supplied driver visits increasing digit lengths, so its combined candidate order is also increasing.
4. Retain the supplied `isPrime` helper and inclusive bound checks. The helper correctly rejects 0, 1 and negative integers. It tests divisors through the square root; it is not a palindrome generator.
5. Explain why candidate generation avoids checking every integer up to one hundred million. For length d there are `5 * 10^(ceil(d/2)-1)` candidates under this contract; the eight calls produce 11110 candidates in total.

The untouched learner throws `TASK: palindrome generation`, exits 2 and never opens `pprime.out`. The reference generator and accepted primality helper remain unchanged. The driver cannot prove that a returned candidate list is exhaustive, so the checks below are required.

## Independent checks and walkthrough

For small intervals, enumerate every integer independently, reverse its decimal digits, and test divisibility. Compare the full ordered result, including empty intervals and both endpoints. For each helper length check the candidate count, exact digit length, odd value, decimal symmetry, distinctness and increasing order. At small lengths, compare against direct enumeration rather than another copy of the generator.

Check a range crossing a digit-length boundary, 11, repeated middle zeroes, prime squares such as 121, and the largest allowed interval. Use a separately generated candidate set and independently computed prime divisors for the full interval. Distinguish a missing candidate from a wrong primality result before changing the code.

For an instructor walkthrough, pause after the interval prediction, build one odd-length and one even-length mirror, check 11, then implement and run. The same steps are a standalone reading sequence. Optional practice can compare the historical nested-loop generator with a half-seed generator, or use a prime-divisor table; retain the same interval and output contract when comparing them. This is structured candidate search; binary search, two pointers and coordinate compression need separate activities if assigned in the surrounding unit.

## Native workflow

Use JDK 17 or newer and run from this extracted role folder. `Main.java` contains the non-public entry class `pprime`; compile that filename and run that class name. No Maven dependency is needed. The site's teaching Java preview is not native execution of this file-based program.

The following guard compiles only current source into a fresh generated directory, runs in a temporary directory containing a copy of the input, compares with the included sample oracle, and publishes the native answer only after a match. A refused task, failed compilation, missing entry class or wrong answer preserves a prior `pprime.out`. It cleans up its temporary run and any unpublished output copy.

```sh
task_classes="$(pwd)/target/classes"
task_run=$(mktemp -d) || exit 1
trap 'rm -rf "$task_run"' EXIT
if rm -rf target/classes \
    && mkdir -p target/classes \
    && javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d target/classes Main.java \
    && cp pprime.in "$task_run/pprime.in" \
    && (cd "$task_run" && java -cp "$task_classes" pprime) \
    && diff -u sample.expected "$task_run/pprime.out" \
    && cp "$task_run/pprime.out" pprime.out.new \
    && mv pprime.out.new pprime.out; then
    printf 'Verified pprime.out\n'
else
    task_status=$?
    rm -f pprime.out.new
    exit "$task_status"
fi
```

After changing the input, predict its output in a separate expected file and replace `sample.expected` in the comparison command with that file's name. Retain the original sample oracle. Run each role from its own folder and keep generated classes separate. The preserved reference assumes legal problem input; the learner's supplied reader also rejects malformed or out-of-range input before writing. This guide, Main.java, the input file and expected file are all contained in this role export.
