// http://www.usaco.org/index.php?page=viewproblem2&cpid=692

import java.util.*;
import java.io.*;

class Main {
  public static void main(String[] args) throws IOException {
     // read input
    BufferedReader br = new BufferedReader(new FileReader("cowcode.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    String s = st.nextToken();
    long N = Long.parseLong(st.nextToken())-1;

    char answer = calcLetter(s, N);

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("cowcode.out")));
    pw.println(answer);
    pw.close();
  }

  public static char calcLetter(String s, long index) {
    System.out.println("index: " + index);

    // base case: index is within the original string
    if (index < s.length()) {
      return s.charAt((int)index);
    }

    // otherwise, keep doubling the original string and keeping track of its length until right before the doubling where the index would be in the string
    long length = s.length();
		while (2*length <= index) {
			length *= 2;
		}

    System.out.println("length: " + length);

    // find the new index relative to starting in the middle half of the string
    // for example, if the index is 7 and the string is COWWCOOCOWWC, the new index should be 0
    // if the index is 4 and the string is COWWCO, the new index should be 0

    // if the index happens to be the first letter of the second half, then calculate the last letter of the first half
    if (length == index) {
			return calcLetter(s, length-1);
		}

    // otherwise the new index is just:
    long newIndex = index - length - 1;
    return calcLetter(s, newIndex);
  }
}