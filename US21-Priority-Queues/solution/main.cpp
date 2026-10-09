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

// ADDED: preserve input order when priorities tie by comparing the input index.
std::vector<std::string> orderTasks(const std::vector<Task>& tasks) {
    using Entry = std::pair<std::int64_t, std::size_t>;
    std::priority_queue<Entry, std::vector<Entry>, std::greater<Entry>> pending;
    for (std::size_t i = 0; i < tasks.size(); ++i)
        pending.push({tasks[i].priority, i});
    std::vector<std::string> labels;
    while (!pending.empty()) {
        labels.push_back(tasks[pending.top().second].label);
        pending.pop();
    }
    return labels;
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
