// ADDED: separate unfinished palindrome-generation learner; reference is preserved.
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class pprime {
    public static void main(String[] args) throws IOException {
        try (Scanner input = new Scanner(new File("pprime.in"))) {
            if (!input.hasNextInt()) throw new IllegalArgumentException("Expected lower bound.");
            int lower = input.nextInt();
            if (!input.hasNextInt()) throw new IllegalArgumentException("Expected upper bound.");
            int upper = input.nextInt();
            if (input.hasNext() || lower < 5 || lower > upper || upper > 100000000) {
                throw new IllegalArgumentException("Require exactly 5 <= lower <= upper <= 100000000.");
            }
            ArrayList<Integer> answer = new ArrayList<Integer>();
            for (int digits = 1; digits <= 8; digits++) {
                for (int value : generatePalindromes(digits)) {
                    if (value >= lower && value <= upper && isPrime(value)) {
                        answer.add(value);
                    }
                }
            }
            // ADDED: the unfinished generator cannot create or replace an answer.
            try (PrintWriter output = new PrintWriter("pprime.out")) {
                for (int value : answer) output.println(value);
            }
        } catch (UnsupportedOperationException unfinished) {
            System.err.println("Unfinished learner task: " + unfinished.getMessage());
            System.exit(2);
        } catch (IllegalArgumentException invalid) {
            System.err.println("Invalid input: " + invalid.getMessage());
            System.exit(2);
        }
    }

    public static ArrayList<Integer> generatePalindromes(int digits) {
        // ADDED TASK: return each odd-valued palindrome of this length in order.
        throw new UnsupportedOperationException("TASK: palindrome generation");
    }

    public static boolean isPrime(int num) {
        if (num < 2) { // ADDED: 0, 1 and negative integers are not prime.
            return false;
        }
        // check divisors up to sqrt(num)

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }
}
