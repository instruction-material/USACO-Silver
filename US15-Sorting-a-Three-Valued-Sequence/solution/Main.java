/*
ID: roobeel1
LANG: JAVA
TASK: sort3
*/

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("sort3.in"));
    int N = Integer.parseInt(br.readLine());

    int[] nums = new int[N];
    for (int i = 0; i < N; i++) {
      nums[i] = Integer.parseInt(br.readLine());
    }

    // count the number of 1s, 2s, 3s
    int[] counts = new int[4];
    for (int num : nums) {
      counts[num] += 1;
    }

    // create correctNums
    int[] correctNums = new int[N];
    for (int i = 0; i < N; i++) {
      if (i < counts[1]) {
        correctNums[i] = 1;
      } else if (i < counts[1] + counts[2]) {
        correctNums[i] = 2;
      } else {
        correctNums[i] = 3;
      }
    }

    // count the number of 1s, 2s, 3s in the incorrect position
    // a 2 in swapPos[2][1] means there are two 2s that are in the section of 1s
    int[][] swapPos = new int[4][4];
    for (int i = 0; i < N; i++) {
      swapPos[nums[i]][correctNums[i]] += 1;
    }

    for (int[] row : swapPos) {
      for (int num : row) {
        System.out.print(num);
      }
      System.out.println("");
    }

    // make all the direct swaps that make sense first
    int numSwaps = 0;
    int num12 = Math.min(swapPos[1][2], swapPos[2][1]);
    swapPos[1][2] -= num12;
    swapPos[2][1] -= num12;
    int num23 = Math.min(swapPos[2][3], swapPos[3][2]);
    swapPos[2][3] -= num23;
    swapPos[3][2] -= num23;
    int num13 = Math.min(swapPos[1][3], swapPos[3][1]);
    swapPos[1][3] -= num13;
    swapPos[3][1] -= num13;

    numSwaps = num12 + num23 + num13;

    // at this point, there are only indirect swaps left, which will take 2 swaps to correct. swapPos[1][2] and swapPos[1][3] capture all of the numbers left that need to be fixed through a series of indirect swaps.
    numSwaps += 2 * (swapPos[1][2] + swapPos[1][3]);

    // write result
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
    "sort3.out")));
    out.println(numSwaps);
    out.close();
  }
}