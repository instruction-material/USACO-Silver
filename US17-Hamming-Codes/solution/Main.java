/*
ID: roobeel1
LANG: JAVA
TASK: hamming
*/

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("hamming.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int numCodewords = Integer.parseInt(st.nextToken());
    int bitLength = Integer.parseInt(st.nextToken());
    int goalDist = Integer.parseInt(st.nextToken());

    ArrayList<Integer> codewords = new ArrayList<Integer>();
    codewords.add(0);
    int codeword = 1;

    while (codewords.size() < numCodewords) {
      // compare this codeword to all of the other codewords to check if it's more than dist away from all of them
      String codewordStr = Integer.toBinaryString(codeword);
     
      boolean meetsCriteria = true;
      for (int num: codewords) {
        String codewordStr2 = Integer.toBinaryString(num);

        // make sure codewordStr and codewordStr2 are the same length
        int length = Math.max(codewordStr.length(), codewordStr2.length());
        if (codewordStr.length() < length) {
          while (codewordStr.length() != length) {
            codewordStr = "0" + codewordStr;
          }
        } else {
          while (codewordStr2.length() != length) {
            codewordStr2 = "0" + codewordStr2;
          }
        }

        // calculate Hamming distance
        int dist = 0;
        for (int i = 0; i < codewordStr.length(); i++) {
          if (codewordStr.charAt(i) != codewordStr2.charAt(i)) {
            dist++;
          }
        }

        if (dist < goalDist) {
          meetsCriteria = false;
          break;
        }
      }

      if (meetsCriteria) {
        codewords.add(codeword);
      }

      codeword++;
    }

    // print output
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
    "hamming.out")));
    
    for (int i = 0; i < codewords.size(); i++) {
      if (i % 10 == 0 && i != 0) {
        out.print("\n");
      }
      out.print(codewords.get(i));
      if ((i+1) % 10 != 0 && i != codewords.size()-1) {
        out.print(" ");
      }
    }
    out.print("\n");

    out.close();
  }
}