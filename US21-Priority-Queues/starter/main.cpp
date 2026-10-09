#include <algorithm>
#include <cstdint>
#include <fstream>
#include <functional>
#include <iostream>
#include <queue>
#include <stdexcept>
#include <string>
#include <utility>
#include <vector>
struct Task { std::int64_t priority; std::string label; };

// ADDED: learner task. Input position, rather than the label, breaks ties.
std::vector<std::string> orderTasks(const std::vector<Task>& tasks) {
    (void)tasks;
    // TODO: insert (priority, input index) entries into a min-priority queue,
    // then remove entries in queue order and return their original labels.
    throw std::logic_error("Complete orderTasks before running the starter");
}

// ADDED: shared file driver; complete the marked helper tasks above main.
int main() {
    try {
        std::ifstream input("priority.in");
        int n;
        if (!(input >> n) || n < 0 || n > 100000)
            throw std::runtime_error("Expected a valid task count in priority.in");
        std::vector<Task> tasks(n);
        for (auto& task : tasks)
            if (!(input >> task.priority >> task.label)) throw std::runtime_error("Missing task");
        const auto labels = orderTasks(tasks);
        std::ofstream output("priority.out");
        if (!output) throw std::runtime_error("Cannot write priority.out");
        for (const auto& label : labels) output << label << '\n';
    } catch (const std::exception& error) {
        std::cerr << error.what() << '\n';
        return 2;
    }
}
