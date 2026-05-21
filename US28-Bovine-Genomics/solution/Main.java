import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input (A=0, C=1, T=2, G=3)
    BufferedReader br = new BufferedReader(new FileReader("cownomics.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());

    int[][] spottyCows = new int[N][M];
    for (int i = 0; i < N; i++) {
      String line = br.readLine();
      for (int j = 0; j < M; j++) {
        if (line.charAt(j) == 'A') {
          spottyCows[i][j] = 0;
        } else if (line.charAt(j) == 'C') {
          spottyCows[i][j] = 1;
        } else if (line.charAt(j) == 'T') {
          spottyCows[i][j] = 2;
        } else {
          spottyCows[i][j] = 3;
        }
      }
    }

    int[][] plainCows = new int[N][M];
    for (int i = 0; i < N; i++) {
      String line = br.readLine();
      for (int j = 0; j < M; j++) {
        if (line.charAt(j) == 'A') {
          plainCows[i][j] = 0;
        } else if (line.charAt(j) == 'C') {
          plainCows[i][j] = 1;
        } else if (line.charAt(j) == 'T') {
          plainCows[i][j] = 2;
        } else {
          plainCows[i][j] = 3;
        }
      }
    }

    br.close();

    // test each group of 3 cows
    int numGroups = 0;

    for (int i = 0; i < M; i++) {
      for (int j = i+1; j < M; j++) {
        for (int k = j+1; k < M; k++) {
          if (testGroup(i, j, k, N, spottyCows, plainCows)) {
            numGroups++;
          }
        }
      }
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("cownomics.out")));
    pw.println(numGroups);
    pw.close();
  }

  // takes in 3 positions and returns true if these 3 can be used to explain spottiness
  public static boolean testGroup(int pos1, int pos2, int pos3, int N, int[][] spottyCows, int[][] plainCows) {
    // track which groups of 3 bases we see in the spotty cows by computing 16*base1 + 4*base2 + base3 and storing these numbers in a HashSet
    HashSet<Integer> nums = new HashSet<Integer>();
    for (int i = 0; i < N; i++) {
      nums.add(spottyCows[i][pos1] * 16 + spottyCows[i][pos2] * 4 + spottyCows[i][pos3]);
    }

    // now go through the plain cows, and if any of these numbers match, then this group doesn't work
    for (int i = 0; i < N; i++) {
      int num = plainCows[i][pos1] * 16 + plainCows[i][pos2] * 4 + plainCows[i][pos3];
      if (nums.contains(num)) {
        return false;
      }
    }

    return true;
  }
}