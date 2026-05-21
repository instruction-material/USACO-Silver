// http://www.usaco.org/index.php?page=viewproblem2&cpid=990

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("berries.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int K = Integer.parseInt(st.nextToken());

    int[] trees = new int[N];
    st = new StringTokenizer(br.readLine());

    for (int i = 0; i < N; i++) {
      trees[i] = Integer.parseInt(st.nextToken());
    }

    Arrays.sort(trees);
    
    br.close();

    // in this problem, we have K baskets and we can put any number of berries from a single tree into each basket. Elsie gets the K/2 largest baskets, and Bessie gets the rest. we are calculating the maximum number of berries Bessie can get.
    // to do this, we can simulate the number of berries in each of Elsie's buckets (starting from 1) until we get to some number we cannot achieve, and always calculate the most Bessie can get in this scenario

    int numBerriesPerElsieBasket = 0;
    int answer = 0;

    while (true) {
      numBerriesPerElsieBasket++;

      int[] treesAfterBessie = trees.clone();

      // calculate the number of baskets we can fill with numBerriesPerElsieBasket
      int numBasketsFillable = 0;
      for (int i = 0; i < trees.length; i++) {
        if (trees[i] >= numBerriesPerElsieBasket) {
          numBasketsFillable += trees[i]/numBerriesPerElsieBasket;
          treesAfterBessie[i] %= numBerriesPerElsieBasket;
        }
        treesAfterBessie[i] *= -1;    // so we can sort in descending order
      }

      // if numBasketsFillable < K/2, then we can stop
      if (numBasketsFillable < K/2) {
        break;
      }

      // if numBasketsFillable >= K, then we know we can fill all of the baskets with numBerriesPerElsieBasket, which means Bessie and Elsie can get the same number of berries in this scenario
      if (numBasketsFillable >= K) {
        answer = Math.max(answer, numBerriesPerElsieBasket*K/2);
        continue;
      }

      // here, we know we can fill (numBasketsFillable-K/2) of Bessie's baskets with numBerriesPerElsieBasket. the remaining ones (which is K-numBasketsFillable baskets) have to filled with the berries in the treesAfterBessie array (but we can only take up to numBerriesPerElsieBasket from each tree)

      // if the number of trees is already less than K-numBasketsFillable, then we know this scenario will not work
      if (N < K-numBasketsFillable) {
        break;
      }

      Arrays.sort(treesAfterBessie);

      int bessieMax = (numBasketsFillable-K/2) * numBerriesPerElsieBasket;
      for (int i = 0; i < K-numBasketsFillable; i++) {
        int numBerries = -treesAfterBessie[i];  // remember that the values in this array are negative
        bessieMax += Math.min(numBerries, numBerriesPerElsieBasket);
      }

      answer = Math.max(answer, bessieMax);
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("berries.out")));
    pw.println(answer);
    pw.close();
  }
}