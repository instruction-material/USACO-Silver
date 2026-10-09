#include <algorithm>
#include <cstdint>
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <string>
#include <vector>

using Triangle = std::vector<std::vector<int>>;

// ADDED: each cell combines only the two legal children in the next row.
std::int64_t maximumPathSum(const Triangle& triangle) {
    std::vector<std::int64_t> best(triangle.back().begin(), triangle.back().end());
    for (int row = static_cast<int>(triangle.size()) - 2; row >= 0; --row)
        for (int column = 0; column <= row; ++column)
            best[column] = triangle[row][column] + std::max(best[column], best[column + 1]);
    return best[0];
}

// ADDED: supplied file driver; calculate first so a failed attempt writes no answer.
int main() {
    try {
        std::ifstream input("numtri.in");
        int rows;
        if (!(input >> rows) || rows < 1 || rows > 1000)
            throw std::runtime_error("Invalid row count in numtri.in");
        Triangle triangle(rows);
        for (int row = 0; row < rows; ++row) {
            triangle[row].resize(row + 1);
            for (int& value : triangle[row])
                if (!(input >> value) || value < 0 || value > 100)
                    throw std::runtime_error("Invalid triangle value in numtri.in");
        }
        std::string extra;
        if (input >> extra) throw std::runtime_error("Invalid trailing data in numtri.in");
        const auto answer = maximumPathSum(triangle);
        std::ofstream output("numtri.out");
        if (!output) throw std::runtime_error("Cannot write numtri.out");
        output << answer << '\n';
        if (!output) throw std::runtime_error("Cannot finish numtri.out");
    } catch (const std::exception& error) {
        std::cerr << error.what() << '\n';
        return 2;
    }
}
