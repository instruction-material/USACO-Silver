# Reference pack: Exam and Bank Account Classes

This is the completed historical demonstration with its accepted overflow-safe numeric comparisons. Retain a learner attempt before comparison; the learner tasks below describe the separate unfinished pack.

This guide is included inside this role export; its contract, worked example, tasks, checks and native commands do not require a parent directory.

## Contract

This is a Java class-ordering demonstration with no input file or standard input. It sorts the four supplied Exam objects by increasing score, then increasing name for a score tie. It sorts the four BankAccount objects by decreasing balance, then increasing name for a balance tie. Names are non-null strings and use Java's case-sensitive String.compareTo order. Scores and balances are Java int values, including negative values, Integer.MIN_VALUE and Integer.MAX_VALUE. They are demonstration fields, not a specification of a financial account system.

A successful run prints exactly two lines: the sorted exam list and the sorted account array. Each object's supplied toString controls the displayed name and numeric field. The included sample.expected contains the exact output for the unmodified demonstration data. A different legal data set needs its own predicted output.

## Worked example

| Object | Values in the supplied demonstration | Predicted order |
| --- | --- | --- |
| Exam | Abdi 95; Beyonce 100; Camila 87; Diego 87 | Camila, Diego, Abdi, Beyonce |
| BankAccount | Emilia 1000; Farooq 1050; Gabrielle 1200; Hassan 1050 | Gabrielle, Farooq, Hassan, Emilia |

Camila and Diego have equal scores, so their names decide their relative order. Farooq and Hassan have equal balances, so their names decide that tie. For an exam with score -2147483648 and another with score 2147483647, the first belongs earlier. Subtracting those scores overflows an int and can reverse the comparison. For accounts, the greater balance belongs earlier, including at these same limits.

## Learner tasks

1. Predict both complete output lines before running. Mark the primary key, direction and tie rule separately.
2. Complete Exam.compareTo in the learner: increasing score followed by increasing name. A negative result means this exam comes before the other; zero means their ordering keys are equal. Only the sign matters.
3. Complete Main.compareBankAccounts in the learner: decreasing balance followed by increasing name. Keep the name direction increasing when the balance direction reverses. Use an overflow-safe numeric comparison such as Integer.compare rather than subtracting int fields.
4. Retain the constructors, display methods, supplied data and two sort calls. The unfinished methods throw named task exceptions; the unchanged learner exits 2 and prints no result.
5. Record a tied-value prediction and an integer-limit prediction, then implement and compare those results. Keep the learner attempt before inspecting the separate reference.

The reference uses Exam's natural Comparable ordering for Collections.sort and an inline comparator for Arrays.sort on BankAccount. The learner names the bank comparison method so each task is visible. Both forms specify the same ordering contract. This lesson teaches object keys and comparator contracts; later algorithm lessons can apply those keys when implementing their own sorting algorithms.

## Independent checks

For each result, verify every neighboring pair follows the primary key and then the tie key. Count objects before and after sorting to ensure none were removed or duplicated. Compare equal-key objects in both directions: both comparisons must have sign zero. For unequal keys, swapping the objects must reverse the comparison's sign.

Test equal scores, equal balances, equal names, already sorted and reversed data. Then replace numeric demonstration values in a copy with -1, 0, 1, Integer.MIN_VALUE and Integer.MAX_VALUE. Predict exam order using the pair (score, name), and account order using (-balance, name) in an independent model with unbounded integers. Do not negate Integer.MIN_VALUE in Java as a substitute for a safe comparison.

For an instructor walkthrough, stop after the prediction, trace one tied comparison, inspect one overflow example, and then run each completed task. The same steps form a standalone reading and implementation sequence. Optional practice can add a third tie key or reverse one direction; state the changed contract and expected order before editing.

## Native workflow

Use JDK 17 or newer. Run from the extracted role folder containing this README, Main.java and sample.expected. The learner and reference each compile into their own target/classes directory. No Maven resolution or input file is required. The site's teaching Java preview is not the native acceptance for this exercise.

```sh
mkdir -p target/classes
if javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d target/classes Main.java \
    && java -cp target/classes Main > answer.new \
    && diff -u sample.expected answer.new; then
    mv answer.new answer.txt
else
    status=$?
    rm -f answer.new
    exit "$status"
fi
```

The guard requires a successful current-source compilation before execution and replaces answer.txt only after successful execution and an exact match. An unfinished learner exits 2; a mismatch exits nonzero. Either preserves an earlier answer.txt. After changing demonstration data, predict and prepare a separate expected file rather than overwriting the supplied sample oracle. Keep compiled classes separate between roles so a previous reference build cannot accidentally run as a learner.
