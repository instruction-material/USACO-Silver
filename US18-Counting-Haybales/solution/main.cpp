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

// ADDED: return an insertion boundary in the half-open index range [0, size].
int firstAtLeast(const std::vector<std::int64_t>& values, std::int64_t target) {
    int low = 0, high = static_cast<int>(values.size());
    while (low < high) {
        const int middle = low + (high - low) / 2;
        if (values[middle] < target) low = middle + 1;
        else high = middle;
    }
    return low;
}

int firstGreater(const std::vector<std::int64_t>& values, std::int64_t target) {
    int low = 0, high = static_cast<int>(values.size());
    while (low < high) {
        const int middle = low + (high - low) / 2;
        if (values[middle] <= target) low = middle + 1;
        else high = middle;
    }
    return low;
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
