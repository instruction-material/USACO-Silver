import java.util.Stack;

class Main {
  public static void main(String[] args) {

    // example 1: fill a selected region of a painting with a new color (recursive method)

    int[][] painting = new int[][]{
      { 0, 0, 0, 0, 0 },
      { 0, 1, 1, 0, 0 },
      { 0, 1, 0, 0, 0 },
      { 0, 1, 0, 0, 0 },
      { 0, 1, 0, 0, 0 }
    };

    int[][] filledPainting = floodfillRecursive(painting, 1, 1, 2);
    printPainting(filledPainting);

    // example 2: fill a selected region of a painting with a new color (uses a stack, because large paintings can exceed the JVM recursion depth)

    filledPainting = floodfill(painting, 1, 1, 1);
    printPainting(filledPainting);

    // example 3: find the size of the largest connected region

    int[][] painting2 = new int[][]{
      { 0, 0, 0, 1, 2 },
      { 0, 1, 1, 2, 2 },
      { 0, 1, 3, 2, 4 },
      { 0, 1, 3, 3, 4 },
      { 0, 1, 3, 3, 4 }
    };

    int maxConnected = findMaxConnected(painting2);
    System.out.println(maxConnected);
  }

  // wrapper method to create visited array and oldColor
  public static int[][] floodfillRecursive(int[][] painting, int xStart, int yStart, int newColor) {
    int n = painting.length;
    int m = painting[0].length;
    boolean[][] visited = new boolean[n][m];
    int oldColor = painting[xStart][yStart];

    fillRecursive(painting, visited, xStart, yStart, oldColor, newColor);

    return painting;
  }

  public static void fillRecursive (int[][] painting, boolean[][] visited, int x, int y, int oldColor, int newColor) {
    // check to stay inbounds
     if (x < 0 || x > painting.length-1) return;
     if (y < 0 || y > painting[0].length-1) return;

    // check we haven't already visited this pixel
     if (visited[x][y]) return;
     
     // mark this pixel as visited
     visited[x][y] = true;

    // fill this pixel with target color if it matches the old color
     if (painting[x][y] == oldColor) {
       painting[x][y] = newColor;
     } else {
       return;
     }

     // recursively fill surrounding pixels
     fillRecursive(painting, visited, x-1, y, oldColor, newColor);
     fillRecursive(painting, visited, x+1, y, oldColor, newColor);
     fillRecursive(painting, visited, x, y-1, oldColor, newColor);
     fillRecursive(painting, visited, x, y+1, oldColor, newColor);
  }

  public static int[][] floodfill(int[][] painting, int xStart, int yStart, int newColor) {  
    int n = painting.length;
    int m = painting[0].length;
    boolean[][] visited = new boolean[n][m];
    int oldColor = painting[xStart][yStart];

    Stack<int[]> s = new Stack<int[]>();
    int[] coords = {xStart, yStart};
    s.push(coords);
    visited[xStart][yStart] = true;

    while (!s.isEmpty()) {
      coords = s.pop();
      int x = coords[0];
      int y = coords[1];

      // fill this pixel with target color if it matches the old color
      if (painting[x][y] == oldColor) {
        painting[x][y] = newColor;

        // add surrounding pixels to stack
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        for (int[] dir : dirs) {
          int newX = x + dir[0];
          int newY = y + dir[1];

          if (newX >= 0 && newX < painting.length && newY >= 0 && newY < painting[0].length && !visited[newX][newY]) {
            int[] newCoords = {newX, newY};
            visited[newX][newY] = true;
            s.push(newCoords);
          }
        }
      }
    }

    return painting;
  }

  public static void printPainting (int[][] painting) {
    for (int[] row : painting) {
      for (int i : row) {
        System.out.print(i);
      }
      System.out.println();
    }
    System.out.println();
  }

  public static int findMaxConnected(int[][] painting) {
    int maxConnected = 0;

    // note: an additional optimization would only be testing starting points where that color has not been tested yet
    for (int i = 0; i < painting.length; i++) {
      for (int j = 0; j < painting[0].length; j++) {
        int color = painting[i][j];

        int connectedSize = findConnected(painting, i, j, color);

        if (connectedSize > maxConnected) {
          maxConnected = connectedSize;
        }
      }
    }

    return maxConnected;
  }

  public static int findConnected(int[][] painting, int x, int y, int color) {
    Stack<int[]> s = new Stack<int[]>();
    int[] coords = {x, y};
    s.push(coords);

    int n = painting.length;
    int m = painting[0].length;
    boolean[][] visited = new boolean[n][m];
    visited[x][y] = true;
    int connectedSize = 0;

    while (!s.isEmpty()) {
      coords = s.pop();
      x = coords[0];
      y = coords[1];
      connectedSize++;

      int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

      for (int[] dir : dirs) {
        int newX = x + dir[0];
        int newY = y + dir[1];

        if (newX >= 0 && newX < painting.length && newY >= 0 && newY < painting[0].length && !visited[newX][newY] && painting[newX][newY] == color) {
          int[] newCoords = {newX, newY};
          visited[newX][newY] = true;
          s.push(newCoords);
        }
      }
    }

    return connectedSize;
  }
}