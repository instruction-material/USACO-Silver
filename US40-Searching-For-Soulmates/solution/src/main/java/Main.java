// adapted from USACO solution

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        for (int t = Integer.parseInt(in.readLine()); t > 0; t--) {
            StringTokenizer tokenizer = new StringTokenizer(in.readLine());
            long cow1 = Long.parseLong(tokenizer.nextToken());
            long cow2 = Long.parseLong(tokenizer.nextToken());
            long answer = Long.MAX_VALUE;
            for (int removed = 0; cow2 >> removed > 0; removed++) {
                long here = 0;
                long prefix = cow2 >> removed;
                long cow = cow1;
                while (cow > prefix) {
                    if (cow % 2L == 1L) {
                        cow++;
                        here++;
                    }
                    cow /= 2L;
                    here++;
                }
                here += prefix - cow;
                here += removed;
                here += Long.bitCount(cow2 & ((1L << removed) - 1L));
                answer = Math.min(answer, here);
            }
            out.append(answer).append('\n');
        }
        System.out.print(out);
    }
}
