import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.StringTokenizer;

/** Learner: use directed cycles to find the best achievable gift for each cow. */
public final class Main {
    private Main() {}

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

    static int[][] readInput(TokenReader in) throws IOException {
        int n = in.integer();
        if (n < 1 || n > 500) {
            throw new IllegalArgumentException("Require 1 <= N <= 500.");
        }
        int[][] preferences = new int[n][n];
        for (int cow = 0; cow < n; cow++) {
            boolean[] seen = new boolean[n + 1];
            for (int rank = 0; rank < n; rank++) {
                int gift = in.integer();
                if (gift < 1 || gift > n || seen[gift]) {
                    throw new IllegalArgumentException("Each preference row must be a permutation of 1..N.");
                }
                preferences[cow][rank] = gift;
                seen[gift] = true;
            }
        }
        if (in.next() != null) throw new IllegalArgumentException("Unexpected extra input.");
        return preferences;
    }

    static int rankOf(int[] preference, int gift) {
        for (int rank = 0; rank < preference.length; rank++) {
            if (preference[rank] == gift) return rank;
        }
        throw new IllegalArgumentException("Gift is not in the preference row.");
    }

    static boolean[][] reachability(int[][] preferences) {
        // TODO: define one consistent edge direction for acceptable gifts.
        // Include the own-gift case and compute transitive reachability.
        throw new UnsupportedOperationException("Complete reachability.");
    }

    static int[] bestGifts(int[][] preferences, boolean[][] reachable) {
        // TODO: identify which acceptable gifts participate in a return cycle.
        // Return one gift ID per cow, in cow order, best preferences first.
        throw new UnsupportedOperationException("Complete bestGifts.");
    }

    static String formatAnswers(int[][] preferences, int[] answers) {
        int n = preferences.length;
        if (answers == null || answers.length != n) {
            throw new IllegalArgumentException("Require one answer per cow.");
        }
        StringJoiner lines = new StringJoiner("\n", "", "\n");
        for (int cow = 0; cow < n; cow++) {
            int gift = answers[cow];
            if (gift < 1 || gift > n
                    || rankOf(preferences[cow], gift) > rankOf(preferences[cow], cow + 1)) {
                throw new IllegalArgumentException("Each gift ID must be in range and no worse than that cow's own gift.");
            }
            // Best possibilities are evaluated separately, so IDs may repeat.
            lines.add(Integer.toString(gift));
        }
        return lines.toString();
    }

    public static void main(String[] args) {
        try (TokenReader in = new TokenReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            int[][] preferences = readInput(in);
            boolean[][] reachable = reachability(preferences);
            int[] answers = bestGifts(preferences, reachable);
            // Buffer the whole answer before writing any standard output.
            System.out.print(formatAnswers(preferences, answers));
        } catch (IOException | IllegalArgumentException | UnsupportedOperationException error) {
            System.err.println("No answer written: " + error.getMessage());
            System.exit(2);
        }
    }
}
