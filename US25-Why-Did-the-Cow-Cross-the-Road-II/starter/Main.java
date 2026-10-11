import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

/** Learner: complete minimumRepairs before producing maxcross.out. */
public final class Main {
    private Main() {}

    record InputData(int signalCount, int windowLength, int brokenCount,
                     boolean[] broken) {}

    static final class TokenReader implements AutoCloseable {
        private final BufferedReader reader;
        private StringTokenizer tokens = new StringTokenizer("");

        TokenReader(Reader source) {
            reader = new BufferedReader(source);
        }

        String next() throws IOException {
            while (!tokens.hasMoreTokens()) {
                String line = reader.readLine();
                if (line == null) return null;
                tokens = new StringTokenizer(line);
            }
            return tokens.nextToken();
        }

        int integer() throws IOException {
            String token = next();
            if (token == null) throw new IllegalArgumentException("Missing integer.");
            try {
                return Integer.parseInt(token);
            } catch (NumberFormatException error) {
                throw new IllegalArgumentException("Expected an integer.", error);
            }
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }

    static InputData readInput(TokenReader in) throws IOException {
        int n = in.integer();
        int k = in.integer();
        int b = in.integer();
        if (n < 1 || n > 100_000 || k < 1 || k > n || b < 1 || b > n) {
            throw new IllegalArgumentException("Require 1 <= K, B <= N <= 100000.");
        }
        boolean[] broken = new boolean[n + 1];
        for (int i = 0; i < b; i++) {
            int signal = in.integer();
            if (signal < 1 || signal > n || broken[signal]) {
                throw new IllegalArgumentException("Broken signal IDs must be distinct and in 1..N.");
            }
            broken[signal] = true;
        }
        if (in.next() != null) throw new IllegalArgumentException("Unexpected extra input.");
        return new InputData(n, k, b, broken);
    }

    static int minimumRepairs(InputData input) {
        // TODO: count broken signals in every length-K window in O(N) time.
        // State what the first and last valid windows are before coding.
        throw new UnsupportedOperationException("Complete minimumRepairs.");
    }

    public static void main(String[] args) {
        try (TokenReader in = new TokenReader(
                Files.newBufferedReader(Path.of("maxcross.in"), StandardCharsets.UTF_8))) {
            InputData input = readInput(in);
            int answer = minimumRepairs(input);
            if (answer < 0 || answer > Math.min(input.windowLength(), input.brokenCount())) {
                throw new IllegalArgumentException("Answer is outside the possible repair range.");
            }
            // Open the answer file only after parsing and the learner task succeed.
            Files.writeString(Path.of("maxcross.out"), answer + "\n", StandardCharsets.UTF_8);
        } catch (IOException | IllegalArgumentException | UnsupportedOperationException error) {
            System.err.println("No answer written: " + error.getMessage());
            System.exit(2);
        }
    }
}
