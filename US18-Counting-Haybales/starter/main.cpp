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

// ADDED: learner tasks. Return size() when no matching value exists.
int firstAtLeast(const std::vector<std::int64_t>& values, std::int64_t target) {
    (void)values; (void)target;
    // TODO 1: implement the first index whose position is >= target.
    throw std::logic_error("Complete firstAtLeast before running the starter");
}

int firstGreater(const std::vector<std::int64_t>& values, std::int64_t target) {
    (void)values; (void)target;
    // TODO 2: implement the first index whose position is > target.
    throw std::logic_error("Complete firstGreater before running the starter");
}

// ADDED: shared file driver; complete the marked helper tasks above main.
int main() {
    try {
        std::ifstream input("haybales.in");
        int n, q;
        if (!(input >> n >> q) || n < 1 || n > 100000 || q < 1 || q > 100000)
            throw std::runtime_error("Expected valid N and Q in haybales.in");
        std::vector<std::int64_t> positions(n);
        for (auto& value : positions)
            if (!(input >> value)) throw std::runtime_error("Missing haybale position");
        std::sort(positions.begin(), positions.end());
        std::vector<int> answers;
        for (int i = 0; i < q; ++i) {
            std::int64_t a, b;
            if (!(input >> a >> b) || a > b) throw std::runtime_error("Invalid inclusive interval");
            answers.push_back(firstGreater(positions, b) - firstAtLeast(positions, a));
        }
        std::ofstream output("haybales.out");
        if (!output) throw std::runtime_error("Cannot write haybales.out");
        for (int answer : answers) output << answer << '\n';
    } catch (const std::exception& error) {
        std::cerr << error.what() << '\n';
        return 2;
    }
}
