// http://www.usaco.org/index.php?page=viewproblem2&cpid=992

import java.io.*;
import java.util.*;

class Main {
  static ArrayList<int[]>[] edges;
  static int[] component;
  static int[] positions;

  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("wormsort.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());

    positions = new int[N];
    component = new int[N];

    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < N; i++) {
      positions[i] = Integer.parseInt(st.nextToken())-1;
    }

    edges = new ArrayList[N];
    for (int i = 0; i < N; i++) {
      edges[i] = new ArrayList<int[]>();
    }
    
    for (int i = 0; i < M; i++) {
      st = new StringTokenizer(br.readLine());
      int a = Integer.parseInt(st.nextToken())-1;
      int b = Integer.parseInt(st.nextToken())-1;
      int w = Integer.parseInt(st.nextToken());
      int[] temp1 = {b, w};
      int[] temp2 = {a, w};
      edges[a].add(temp1);
      edges[b].add(temp2);
    }

    br.close();

    // in this problem, we binary search over the wormhole widths until we find the largest width possible where the cows can still sort themselves. to test a given width, we only consider the wormholes of up to that width as the edges we can use. we then calculate the connected components of this graph, meaning which regions of the graph are connected. we label each region (or component) with a number. this maxWidth works if each cow p_i is connected to cow i, because that means it can reach its correct position!

    int minWidth = 0;
    int maxWidth = 1000000001;
    while (minWidth != maxWidth) {
      int mid = (minWidth + maxWidth + 1) / 2;
      if (isValid(mid)) {
        minWidth = mid;
      }
      else {
        maxWidth = mid-1;
      }
    }
    if (minWidth > 1e9) {
      minWidth = -1;
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("wormsort.out")));
    pw.println(minWidth);
    pw.close();
  }

  // we use this method to recursively label all of the cows that can be connected using wormholes up to minWidth
  private static void dfs(int currIndex, int label, int minWidth) {
    if (component[currIndex] == label) {
      return;
    }

    component[currIndex] = label;

    for (int[] neighbor: edges[currIndex]) {
      int index = neighbor[0];
      int width = neighbor[1];
      if (width >= minWidth) {
        dfs(index, label, minWidth);
      }
    }
  }

  private static boolean isValid(int minWidth) {
    // start with none of the cows connected
    Arrays.fill(component, -1);

    // use DFS to label each cow as part of a component
    int numComponents = 0;
    for(int i = 0; i < component.length; i++) {
      if (component[i] < 0) {
        numComponents++;
        dfs(i, numComponents, minWidth);
      }
    }

    // check if all cows p_i are connected to i
    for (int i = 0; i < positions.length; i++) {
      if (component[i] != component[positions[i]]) {
        return false;
      }
    }

    return true;
  }
}