/*
ID: roobeel1
LANG: JAVA
TASK: ariprog
*/

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("ariprog.in"));
    int n = Integer.parseInt(in.readLine());
    int m = Integer.parseInt(in.readLine());

    // generate all of the bisquares with 0 <= p,q <= m
    // to make the list easy to search through, create a list of booleans (the max value is 250^2 + 250^2)

    boolean[] bisquares = new boolean[2*250*250+1];
    for (int i = 0; i <= m; i++) {
      for (int j = 0; j <= m; j++) {
        bisquares[i*i+j*j] = true;
      }
    }

    // check all of the arithmetic sequences of length n starting from 0 to m^2+m^2 to see if they are only composed of bisquares

    ArrayList<int[]> result = new ArrayList<int[]>();

    for (int start = 0; start < 2*m*m; start++) {
      for (int diff = 1; diff < (2*m*m-start)/(n-1)+1; diff++) {
        boolean found = true;

        for (int i = 0; i < n; i++) {
          int currNum = start + diff * i;

          if (!bisquares[currNum]) {
            found = false;
            break;
          }
        }

        if (found) {
          int[] startDiff = {start, diff};
          result.add(startDiff);
        }
      }
    }

    // sort and print result
    Collections.sort(result, new Comparator<int[]>() {
      public int compare(int[] o1, int[] o2) {
        if(o1[1] < o2[1]) return -1;
        if(o1[1] > o2[1]) return 1;
        if(o1[0] < o2[0]) return -1;
        if(o1[0] > o2[0]) return 1;
        return 0;
      }
    });

    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
    "ariprog.out")));
    
    if (result.size() == 0) {
      out.println("NONE");
    } else {
      for (int[] arr : result) {
        out.println(arr[0] + " " + arr[1]);
      }
    }

    out.close();
  }
}