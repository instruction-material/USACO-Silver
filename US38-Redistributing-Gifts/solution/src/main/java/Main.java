// adapted from USACO


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        int[][] rankings = new int[n + 1][n + 1];
        for (int cow = 1; cow <= n; cow++) {
            StringTokenizer tokenizer = new StringTokenizer(in.readLine());
            for (int rank = n; rank > 0; rank--) {
                rankings[cow][Integer.parseInt(tokenizer.nextToken())] = rank;
            }
        }
        boolean[][] reachable = new boolean[n + 1][n + 1];
        for (int cow1 = 1; cow1 <= n; cow1++) {
            for (int cow2 = 1; cow2 <= n; cow2++) {
                if (rankings[cow1][cow2] >= rankings[cow1][cow1]) {
                    reachable[cow2][cow1] = true;
                }
            }
        }
        for (int cow2 = 1; cow2 <= n; cow2++) {
            for (int cow1 = 1; cow1 <= n; cow1++) {
                for (int cow3 = 1; cow3 <= n; cow3++) {
                    reachable[cow1][cow3] = reachable[cow1][cow3] || (reachable[cow1][cow2] && reachable[cow2][cow3]);
                }
            }
        }
        StringJoiner joiner = new StringJoiner("\n");
        for (int cow = 1; cow <= n; cow++) {
            int bestGift = 0;
            for (int gift = 1; gift <= n; gift++) {
                if (rankings[cow][gift] > rankings[cow][bestGift] && reachable[cow][gift]) {
                    bestGift = gift;
                }
            }
            joiner.add(bestGift + "");
        }
        System.out.println(joiner);
    }
}