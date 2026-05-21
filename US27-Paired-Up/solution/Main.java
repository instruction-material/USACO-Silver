// http://www.usaco.org/index.php?page=viewproblem2&cpid=738

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("pairup.in"));
    int N = Integer.parseInt(br.readLine());

    int[][] cows = new int[N][2];
    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      cows[i][0] = Integer.parseInt(st.nextToken());
      cows[i][1] = Integer.parseInt(st.nextToken());
    }

    br.close();

    // sort by milk output
    Arrays.sort(cows, new Comparator<int[]>() {
      @Override
      public int compare(int[] arr1, int[] arr2)
      {
          Integer milkAmt = arr1[1];
          Integer milkAmt2 = arr2[1];
          return milkAmt.compareTo(milkAmt2);
      }
    });
    
    // match up pairs, starting from the smallest and greatest production

    int startIndex = 0;
    int endIndex = cows.length - 1;
    int minMilkTime = 0;

    while (true) {
      // find whether the cows at the startIndex or the endIndex will be completely paired off
      if (cows[startIndex][0] < cows[endIndex][0]) {
        // this means the startIndex cows will be completely paired off
        int milkTime = cows[startIndex][1] + cows[endIndex][1];
        minMilkTime = Math.max(milkTime, minMilkTime);
        cows[endIndex][0] -= cows[startIndex][0];
        startIndex++;
      } else if (cows[startIndex][0] > cows[endIndex][0]) {
        // this means the endIndex cows will be completely paired off
        int milkTime = cows[startIndex][1] + cows[endIndex][1];
        minMilkTime = Math.max(milkTime, minMilkTime);
        cows[startIndex][0] -= cows[endIndex][0];
        endIndex--;
      } else {
        // this means the cows were paired off perfectly
        int milkTime = cows[startIndex][1] + cows[endIndex][1];
        minMilkTime = Math.max(milkTime, minMilkTime);
        startIndex++;
        endIndex--;
      }

      if (startIndex == endIndex) {
        // pair off the remainining cows
        int milkTime = cows[startIndex][1] / 2;
        minMilkTime = Math.max(milkTime, minMilkTime);
        break;
      }
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("pairup.out")));
    pw.println(minMilkTime);
    pw.close();
  }
}