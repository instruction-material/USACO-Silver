/*
ID: roobeel1
LANG: JAVA
TASK: holstein
*/

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("holstein.in"));
    int V = Integer.parseInt(br.readLine());

    int[] vitaminNeeds = new int[V];
    StringTokenizer st = new StringTokenizer(br.readLine());
    for (int i = 0; i < V; i++) {
      vitaminNeeds[i] = Integer.parseInt(st.nextToken());
    }

    int G = Integer.parseInt(br.readLine());
    int[][] vitaminContents = new int[G][V];
    for (int i = 0; i < G; i++) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < V; j++) {
        vitaminContents[i][j] = Integer.parseInt(st.nextToken());
      }    
    }

    // try every combination of feeds
    // feedAmt is an array where 1 means we are using this feed and 0 means we are not using these feed. we'll start by filling it with -1s. combos will store all feedAmt arrays that satisfy the vitamin requirements.
    int[] feedAmt = new int[G];
    for (int i = 0; i < G; i++) {
      feedAmt[i] = -1;
    }
    ArrayList<int[]> combos = new ArrayList<int[]>();
    recur(feedAmt, vitaminNeeds, vitaminContents, combos);
    
    // sort combos
    Collections.sort(combos, new Comparator<int[]>() {
      public int compare(int[] c1, int[] c2) {
        int numFeeds1 = 0;
        int numFeeds2 = 0;
        int sumFeeds1 = 0;
        int sumFeeds2 = 0;

        for (int i = 0 ; i < c1.length; i++) {
          if (c1[i] == 1) {
            numFeeds1++;
            sumFeeds1 += i;
          }
          if (c2[i] == 1) {
            numFeeds2++;
            sumFeeds2 += i;
          }
        }

        if (numFeeds1 > numFeeds2) {
          return 1;
        } else if (numFeeds1 < numFeeds2) {
          return -1;
        } else if (sumFeeds1 > sumFeeds2) {
          return 1;
        } else if (sumFeeds1 < sumFeeds2) {
          return -1;
        } else {
          return 0;
        }
      }
    });

    // output result
    int[] bestCombo = combos.get(0);
    int numFeeds = 0;
    for (int num : bestCombo) {
      if (num == 1) {
        numFeeds += 1;
      }
    }
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
    "holstein.out")));
    
    out.print(numFeeds);
    for (int i = 0; i < bestCombo.length; i++) {
      if (bestCombo[i] == 1) {
        out.print(" " + (i+1));
      }
    }
    out.print("\n");

    out.close();
  }

  public static void recur(int[] feedAmt, int[] vitaminNeeds, int[][] vitaminContents, ArrayList<int[]> combos) {

    // check if each feedAmt has been set to 0 or 1
    int indexUnassigned = -1;
    for (int i = 0; i < feedAmt.length; i++) {
      if (feedAmt[i] == -1) {
        indexUnassigned = i;
        break;
      }
    }

    if (indexUnassigned == -1) {
      // calculate whether this feedAmt combination satisfies the vitamin needs by summing the total vitamin amounts contained in these feeds
      int[] vitaminAmt = new int[vitaminNeeds.length];
      int numScoops = 0;
      int feedTypeSum = 0;
      for (int i = 0; i < feedAmt.length; i++) {
        if (feedAmt[i] == 1) {
          numScoops++;
          feedTypeSum += i;
          int feedNum = i;
          for (int j = 0; j < vitaminContents[i].length; j++) {
            vitaminAmt[j] += vitaminContents[i][j];
          }
        }
      }

      boolean satisfies = true;
      for (int i = 0; i < vitaminAmt.length; i++) {
        if (vitaminAmt[i] < vitaminNeeds[i]) {
          satisfies = false;
        }
      }
      if (satisfies) {
        combos.add(feedAmt.clone());
      }

    } else {
      // continue assigning feedAmt
      feedAmt[indexUnassigned] = 1;
      recur(feedAmt, vitaminNeeds, vitaminContents, combos);
      feedAmt[indexUnassigned] = 0;
      recur(feedAmt, vitaminNeeds, vitaminContents, combos);
      feedAmt[indexUnassigned] = -1;
    }
  }
}