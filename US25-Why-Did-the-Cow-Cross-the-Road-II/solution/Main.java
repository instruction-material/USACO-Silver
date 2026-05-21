import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("maxcross.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
    int K = Integer.parseInt(st.nextToken());
    int numBroken = Integer.parseInt(st.nextToken());

    int[] broken = new int[numBroken];
    for (int i = 0; i < numBroken; i++) {
      broken[i] = Integer.parseInt(br.readLine());
    }

    br.close();

    // sort broken signals
    Arrays.sort(broken);

    // use prefix sums to calculate cumulative frequency of broken signals from 0 to N
    int brokenIndex = 0;
    int[] cumulativeBroken = new int[N+1];
    int cumulativeCount = 0;
    for (int i = 0; i < cumulativeBroken.length; i++) {
      if (brokenIndex < broken.length && i == broken[brokenIndex]) {
        cumulativeCount++;
        brokenIndex++;
      }
      cumulativeBroken[i] = cumulativeCount;
    }

    // from 0 to N-K, see how many signals need to be fixed. continuously update this window
    int minFixed = K;
    for (int i = 0; i <= N-K; i++) {
      int numFixed = cumulativeBroken[i+K] - cumulativeBroken[i];
      minFixed = Math.min(minFixed, numFixed);
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("maxcross.out")));
    pw.println(minFixed);
    pw.close();
  }
}