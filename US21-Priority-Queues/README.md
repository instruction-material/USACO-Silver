# Priority queue practice: stable task scheduling

This course-authored practice studio implements a min-priority queue. It is a heap exercise, rather than a named USACO contest problem. The C++20 starter and reference supply the code previously missing from this folder.

## Contract and model

Read `priority.in`: a task count N (0 through 100,000), followed by N lines containing a signed 64-bit integer priority and a whitespace-free label. Repeated labels represent separate tasks. Write labels to `priority.out`, one per line: lower numerical priority first, and earlier input position first when priorities tie. Negative priorities are allowed.

A queue entry pairs the priority with the input index. The index preserves arrival order and locates the original label. Labels do not break ties. C++ `priority_queue` normally exposes the largest entry; a comparison that exposes the smallest pair supplies the needed min-queue. Calling `top()` or `pop()` requires a nonempty queue.

## Guided implementation

1. Predict the supplied sample order: `urgent`, `alpha`, `gamma`, `beta`, `beta`.
2. Complete `orderTasks` in `starter/main.cpp`: insert entries, repeatedly inspect and remove the minimum entry, and collect labels.
3. Explain why the priority alone cannot guarantee stable ties. Trace two equal-priority tasks whose labels sort in the opposite order to their arrival.
4. Check no tasks, one task, all priorities equal, repeated labels, negative priorities, and extreme signed priorities. Compare small outputs with a stable sort of the original records.
5. Explain O(N log N) time and O(N) storage. A heap is useful when insertions and removals interleave; sorting once is a useful independent correctness oracle for this fixed batch.

The core studio is queue ordering with a documented tie rule. An optional extension can add task arrivals while the program runs; that changes the input contract and belongs in a separate attempt. The completed `solution/` pack is available for review after implementing and testing the learner task.

## Build and run

From `starter/` (or `solution/`), compile with `c++ -std=c++20 -Wall -Wextra -Wpedantic main.cpp -o priority` and run `./priority`. Files are read and written in the current directory. The untouched starter exits with a TODO message and writes no answer file. Remove any stale `priority.out` before a fresh attempt.

The source acceptance check compares actual file output with an independent stable-order model, including the 100,000-task limit. Review differences by tracing the queue entries, rather than replacing the unfinished helper with a precomputed output.
