import java.io.*;
import java.util.*;
import java.awt.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("countcross.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int N = Integer.parseInt(st.nextToken());
    int numCows = Integer.parseInt(st.nextToken());
    int numRoads = Integer.parseInt(st.nextToken());

    // store the roads in an NxN array of 4 booleans, representing whether there is a road to the [N,S,W,E]

    boolean[][][] roads = new boolean[N][N][4];
    for (int i = 0; i < numRoads; i++) {
      st = new StringTokenizer(br.readLine());
      int x1 = Integer.parseInt(st.nextToken())-1;
      int y1 = Integer.parseInt(st.nextToken())-1;
      int x2 = Integer.parseInt(st.nextToken())-1;
      int y2 = Integer.parseInt(st.nextToken())-1;
      
      int dx = x2-x1;
      int dy = y2-y1;

      if (dy == 1) {
        roads[x1][y1][1] = true;
        roads[x2][y2][0] = true;
      } else if (dy == -1) {
        roads[x1][y1][0] = true;
        roads[x2][y2][1] = true;
      } else if (dx == 1) {
        roads[x1][y1][3] = true;
        roads[x2][y2][2] = true;
      } else if (dx == -1) {
        roads[x1][y1][2] = true;
        roads[x2][y2][3] = true;
      }
    }

    // store the cows in an NxN array of booleans, and also in a Nx2 array of ints
    boolean[][] cowGrid = new boolean[N][N];
    int[][] cows = new int[N][2];
    for (int i = 0; i < numCows; i++) {
      st = new StringTokenizer(br.readLine());
      int x = Integer.parseInt(st.nextToken())-1;
      int y = Integer.parseInt(st.nextToken())-1;

      cowGrid[x][y] = true;
      cows[i][0] = x;
      cows[i][1] = y;
    }

    br.close();

    // run floodfill to count the number of cows that can be reached without crossing a road (and subtract from the total number of cows to find the number that can't be reached), starting from each cow

    int numUnreached = 0;

    for (int i = 0; i < numCows; i++) {
      int startX = cows[i][0];
      int startY = cows[i][1];
      int numReached = floodfill(cowGrid, roads, startX, startY);
      numUnreached += numCows-numReached;
    }

    // divide answer by 2 since we counted each pair twice
    numUnreached /= 2;

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("countcross.out")));
    pw.println(numUnreached);
    pw.close();
  }

  public static int floodfill(boolean[][] cowGrid, boolean[][][] roads, int startX, int startY) {
    int N = cowGrid.length;
    boolean[][] visited = new boolean[N][N];
    Stack<int[]> s = new Stack<int[]>();
    int[] coords = {startX, startY};
    s.push(coords);
    visited[startX][startY] = true;
    int numReached = 0;

    while (!s.isEmpty()) {
      coords = s.pop();
      int x = coords[0];
      int y = coords[1];

      // increment numReached if this point has a cow
      if (cowGrid[x][y]) {
        numReached++;
      }

      // add surrounding points that can be visited to the stack
      // in order to be visited, the point must be in bounds, not have been visited before, and there must not be a road between it
      if (x-1 >= 0 && !visited[x-1][y] && !roads[x][y][2]) {
        int[] newCoords = {x-1, y};
        visited[x-1][y] = true;
        s.push(newCoords);
      }

      if (x+1 < N && !visited[x+1][y] && !roads[x][y][3]) {
        int[] newCoords = {x+1, y};
        visited[x+1][y] = true;
        s.push(newCoords);
      }

      if (y-1 >= 0 && !visited[x][y-1] && !roads[x][y][0]) {
        int[] newCoords = {x, y-1};
        visited[x][y-1] = true;
        s.push(newCoords);
      }

      if (y+1 < N && !visited[x][y+1] && !roads[x][y][1]) {
        int[] newCoords = {x, y+1};
        visited[x][y+1] = true;
        s.push(newCoords);
      }
    }

    return numReached;
  }
}