import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

/** Learner: implement the schedule check and minimum stage-size search. */
public final class Main {
    private Main() {}

    record InputData(int timeLimit, int[] durations) {}

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
        int limit = in.integer();
        if (n < 1 || n > 10_000 || limit < 1 || limit > 1_000_000) {
            throw new IllegalArgumentException("Require 1 <= N <= 10000 and 1 <= T <= 1000000.");
        }
        int[] durations = new int[n];
        for (int i = 0; i < n; i++) {
            durations[i] = in.integer();
            if (durations[i] < 1 || durations[i] > 100_000 || durations[i] > limit) {
                throw new IllegalArgumentException("Require durations in 1..100000 and a feasible K=N.");
            }
        }
        if (in.next() != null) throw new IllegalArgumentException("Unexpected extra input.");
        return new InputData(limit, durations);
    }

    static boolean finishesWithin(int[] durations, int stageSize, int timeLimit) {
        // TODO: simulate ordered arrivals with a min-heap of finish times.
        // Several cows may finish together; the next cows start at that time.
        throw new UnsupportedOperationException("Complete finishesWithin.");
    }

    static int minimumStageSize(InputData input) {
        // TODO: use the monotone feasibility predicate to find the smallest K.
        // First verify finishesWithin independently on hand-worked schedules.
        throw new UnsupportedOperationException("Complete minimumStageSize.");
    }

    public static void main(String[] args) {
        try (TokenReader in = new TokenReader(
                Files.newBufferedReader(Path.of("cowdance.in"), StandardCharsets.UTF_8))) {
            InputData input = readInput(in);
            int answer = minimumStageSize(input);
            if (answer < 1 || answer > input.durations().length) {
                throw new IllegalArgumentException("Stage size must be in 1..N.");
            }
            // Preserve an existing answer until parsing and learner work succeed.
            Files.writeString(Path.of("cowdance.out"), answer + "\n", StandardCharsets.UTF_8);
        } catch (IOException | IllegalArgumentException | UnsupportedOperationException error) {
            System.err.println("No answer written: " + error.getMessage());
            System.exit(2);
        }
    }
}
