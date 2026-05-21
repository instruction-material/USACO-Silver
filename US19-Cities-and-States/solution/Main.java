// http://usaco.org/index.php?page=viewproblem2&cpid=667

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // for a given city-state pair, count the number of city pairs where the first two letters of the city = the other city's state abbreviation, and vice versa (they must come from different states)

    // approach: use a HashMap, where the key is the first two letters of the city + the first two letters of the state, and the value is the number of times this combo exists. then, iterate through the HashMap and see how many of the reverse pair exist.

    // read input
    BufferedReader br = new BufferedReader(new FileReader("citystate.in"));
    int N = Integer.parseInt(br.readLine());

    HashMap<String, Integer> hmap = new HashMap<String, Integer>();

    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      String city = st.nextToken();
      String state = st.nextToken();

      // pairs can't be in the same state, so don't add city-state pairs where the first two letters of the city are the same as the first two letters of the state
      
      String cityFirstTwo = city.substring(0,2);

      if (!cityFirstTwo.equals(state)) {
        String key = state + cityFirstTwo;
        if (hmap.containsKey(key)) {
          hmap.put(key, hmap.get(key)+1);
        } else {
          hmap.put(key, 1);
        }
      }
    }

    br.close();

    // iterate through the HashMap and see how many pairs exist, then divide by 2 due to double-counting
    int count = 0;
    for (String key : hmap.keySet()) {
      String reverse = key.substring(2) + key.substring(0,2);
      if (hmap.containsKey(reverse)) {
        count += hmap.get(key) * hmap.get(reverse);
      }
    }

    count /= 2;

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("citystate.out")));
    pw.println(count);
    pw.close();
  }
}