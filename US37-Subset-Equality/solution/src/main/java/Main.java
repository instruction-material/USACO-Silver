// adapted from USACO
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        char[] s = in.readLine().toCharArray();
        char[] t = in.readLine().toCharArray();
        int[] freqsS = new int[26];
        int[] freqsT = new int[26];
        for (char x = 'a'; x <= 'z'; x++) {
            for (char letter : s) {
                if (letter == x) {
                    freqsS[x - 'a']++;
                }
            }
            for (char letter : t) {
                if (letter == x) {
                    freqsT[x - 'a']++;
                }
            }
        }
        boolean[][] compatible = new boolean[26][26];
        for (char y = 'a'; y <= 'z'; y++) {
            for (char x = 'a'; x < y; x++) {
                StringBuilder sRestricted = new StringBuilder();
                StringBuilder tRestricted = new StringBuilder();
                for (char letter : s) {
                    if (letter == x || letter == y) {
                        sRestricted.append(letter);
                    }
                }
                for (char letter : t) {
                    if (letter == x || letter == y) {
                        tRestricted.append(letter);
                    }
                }
                compatible[x - 'a'][y - 'a'] = sRestricted.toString().equals(tRestricted.toString());
            }
        }
        StringBuilder out = new StringBuilder();
        for (int q = Integer.parseInt(in.readLine()); q > 0; q--) {
            char[] subset = in.readLine().toCharArray();
            char answer = 'Y';
            int sSum = 0;
            int tSum = 0;
            for (char x : subset) {
                sSum += freqsS[x - 'a'];
                tSum += freqsT[x - 'a'];
            }
            if (sSum != tSum) {
                answer = 'N';
            }
            for (int j = 0; j < subset.length; j++) {
                for (int k = j + 1; k < subset.length; k++) {
                    if (!compatible[subset[j] - 'a'][subset[k] - 'a']) {
                        answer = 'N';
                    }
                }
            }
            out.append(answer);
        }
        System.out.println(out);
    }
}