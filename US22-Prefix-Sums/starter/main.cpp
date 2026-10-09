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

// ADDED: learner tasks. The driver validates 0 <= left <= right <= N.
std::vector<std::int64_t> makePrefix(const std::vector<std::int64_t>& values) {
    (void)values;
    // TODO 1: construct N+1 prefix entries with a zero sentinel.
    throw std::logic_error("Complete makePrefix before running the starter");
}

std::int64_t rangeSum(const std::vector<std::int64_t>& prefix, int left, int right) {
    (void)prefix; (void)left; (void)right;
    // TODO 2: return the sum of values with indices in [left, right).
    throw std::logic_error("Complete rangeSum before running the starter");
}

// ADDED: shared file driver; complete the marked helper tasks above main.
int main() {
    try {
        std::ifstream input("prefix.in");
        int n, q;
        if (!(input >> n >> q) || n < 0 || n > 100000 || q < 0 || q > 100000)
            throw std::runtime_error("Expected valid N and Q in prefix.in");
        std::vector<std::int64_t> values(n);
        for (auto& value : values)
            if (!(input >> value) || value < -1000000000LL || value > 1000000000LL)
                throw std::runtime_error("Invalid array value");
        const auto prefix = makePrefix(values);
        std::vector<std::int64_t> answers;
        for (int i = 0; i < q; ++i) {
            int left, right;
            if (!(input >> left >> right) || left < 0 || left > right || right > n)
                throw std::runtime_error("Invalid half-open interval");
            answers.push_back(rangeSum(prefix, left, right));
        }
        std::ofstream output("prefix.out");
        if (!output) throw std::runtime_error("Cannot write prefix.out");
        for (auto answer : answers) output << answer << '\n';
    } catch (const std::exception& error) {
        std::cerr << error.what() << '\n';
        return 2;
    }
}
