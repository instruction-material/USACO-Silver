# Mother's Milk

This pack contains a separate unfinished `starter/` and the unchanged historical program in `solution/`. Each role includes its full contract, worked example, tasks, independent checks, input, expected output and native commands. Original migration guides are retained in `legacy/2026-10-11/`.

Work in one role folder; do not combine their classes. Compile `Main.java` with Java 17 targeting and run `milk3`. Native execution reads `milk3.in` and writes `milk3.out`. A downloaded role is standalone.

## Contract and representation

Three buckets have integer capacities A, B and C in 1..20. Initially A and B are empty and C is full: `(0, 0, C)`. A move pours from one bucket into a different bucket until the source is empty or the destination is full. Milk is never added or lost. Find every reachable amount in C at a state where A is empty.

Read the three capacities from `milk3.in` in the process's working directory. Write the distinct possible C amounts in increasing order, separated by single spaces and followed by one newline, to `milk3.out`. Zero is a possible amount; do not omit it. This bounded teaching contract agrees with the preserved reference's 21-by-21-by-21 visited array. The historical USACO training statement is an additional resource and may require a training account; this guide supplies the complete working contract independently.

Each state is the three current volumes, not the capacities and not just C. Volumes satisfy `0 <= volume[i] <= capacity[i]` and their sum is the initial C capacity. Two states with equal C but different A and B can allow different next pours. The supplied `Node.pour` copies its input state and moves `min(sourceVolume, destinationCapacity-destinationVolume)` units. It leaves the third bucket and the original Node unchanged.

## Worked example and predictions

The included `milk3.in` is `8 9 10`. Its exact `sample.expected` is:

```text
1 2 8 9 10
```

Start at `(0,0,10)`, so 10 qualifies. C-to-B pours 9 and reaches `(0,9,1)`, so 1 qualifies. C-to-A then A-to-B reaches `(0,8,2)`, so 2 qualifies. A and B's capacities can also leave C at 8 or 9 with A empty. These examples establish possibilities, not completeness; graph exhaustion establishes that there are no others. An independent breadth-first enumeration reaches 12 different states for these capacities.

Before coding, trace every ordered source/destination pair from `(0,9,1)`. Identify the pairs that do not move milk and those that reach a previously visited state. For capacities `1 1 1`, predict `0 1`. For `20 20 20`, predict `0 20`. Do not assume all integers between the smallest and largest answer are reachable.

## Implementation tasks

1. Complete `milk3.reachableVolumes(int[] capacity)` in the separate learner. Begin with `(0,0,capacity[2])` and use a stack or queue to explore reachable states.
2. Define the visited key from all three volumes. Mark when adding a state to the worklist, or explicitly skip already visited states when removing them, so cycles cannot expand indefinitely.
3. Try all six ordered pours between distinct buckets with the supplied helper. A zero-transfer result is the same state and must not create another traversal task.
4. Whenever A is zero, collect C into a distinct-answer set or another deduplicated collection. Return an ArrayList of those amounts; the supplied driver sorts it before writing.
5. State the invariant: every discovered state is reachable, within capacity, and conserves the initial milk. Each newly discovered state is eventually explored. Explain why exhausting the worklist proves completeness.

The untouched learner throws `TASK: reachable bucket states`, exits 2 and never opens `milk3.out`. Parsing and capacity checks are supplied. The reference's historical DFS and Node helper remain unchanged. The driver does not establish that a returned answer is complete; the independent checks below are necessary.

## Independent checks and walkthrough

Compare against a separately written small breadth-first model that represents states as immutable triples and enumerates all ordered pours. Include equal capacities, a bucket that holds all initial milk, unequal capacities, zero in the answer, the supplied example and the maximum capacity. Check that output is sorted and distinct, every answer is reachable with A empty, and no qualifying reachable state is omitted.

Test the pour helper independently: volume sum and capacity bounds are preserved, the original state is unchanged, and a move stops at an empty source or a full destination. Swapping the names of initially empty A and B changes which bucket's emptiness is queried; do not assume the answer stays the same.

For an instructor walkthrough, pause after the state definition, trace two pours, identify a cycle, then justify visited handling before running. The same sequence works independently. Optional practice can replace DFS with BFS or change a capacity, predicting the complete new answer before execution. This is a finite-state search exercise, not a minimum-number-of-pours exercise; a shortest-path extension needs its own distance contract.
