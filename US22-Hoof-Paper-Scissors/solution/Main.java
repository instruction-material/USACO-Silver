// http://www.usaco.org/index.php?page=viewproblem2&cpid=691

import java.util.*;
import java.io.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("hps.in"));
    int N = Integer.parseInt(br.readLine());

    String[] gestures = new String[N];
    for (int i = 0; i < N; i++) {
      gestures[i] = br.readLine();
    }

    br.close();

    // create array of [countH, countP, countS], where each count is the total number of each gesture up until that point
    int[][] totalGestures = new int[N][3];
    if (gestures[0].equals("H")) {
      totalGestures[0][0]++;
    } else if (gestures[0].equals("P")) {
      totalGestures[0][1]++;
    } else if (gestures[0].equals("S")) {
      totalGestures[0][2]++;
    }

    for (int i = 1; i < N; i++) {
      // copy over the frequency from the previous time point
      for (int j = 0; j < 3; j++) {
        totalGestures[i][j] = totalGestures[i-1][j];
      }

      // increment accordingly
      if (gestures[i].equals("H")) {
        totalGestures[i][0]++;
      } else if (gestures[i].equals("P")) {
        totalGestures[i][1]++;
      } else if (gestures[i].equals("S")) {
        totalGestures[i][2]++;
      }
    }

    // try every split point from 0 to N, see which gesture occurred most frequently in each of the two segments, and find the split point that results in the highest summed frequency
    int maxWins = 0;
    for (int splitPoint = 0; splitPoint < N; splitPoint++) {
      int maxFirstHalf = 0;
      int maxSecondHalf = 0;
      if (splitPoint == 0) {
        // edge case where the we make the same gesture for the whole game
        maxSecondHalf = findMax(0, N, totalGestures);
      } else {
        maxFirstHalf = findMax(0, splitPoint, totalGestures);
        maxSecondHalf = findMax(splitPoint, N, totalGestures);
      }

      maxWins = Math.max(maxWins, maxFirstHalf + maxSecondHalf);
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("hps.out")));
    pw.println(maxWins);
    pw.close();
  }

  // finds the maximum number of a gesture from start to end, where start is inclusive and end is non-inclusive
  public static int findMax(int start, int end, int[][] totalGestures) {
    int maxGesture = 0;
    for (int i = 0; i < 3; i++) {
      // calculate the number of each gesture in this segment
      int numGesture = 0;
      if (start == 0) {
        numGesture = totalGestures[end-1][i];
      } else {
        numGesture = totalGestures[end-1][i] - totalGestures[start-1][i];
      }
      maxGesture = Math.max(maxGesture, numGesture);
    }

    return maxGesture;
  }
}