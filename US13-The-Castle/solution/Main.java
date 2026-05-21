/*
ID: roobeel1
LANG: JAVA
TASK: castle
*/

import java.io.*;
import java.util.*;

class castle {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("castle.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int m = Integer.parseInt(st.nextToken());
    int n = Integer.parseInt(st.nextToken());
    
    int[][] castle = new int[n][m];
    Set<String>[][] allWalls = new HashSet[n][m];
    for (int i = 0; i < n; i++) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < m; j++) {
        castle[i][j] = Integer.parseInt(st.nextToken());
        allWalls[i][j] = getWalls(castle[i][j]);
      }
    }

    br.close();

    boolean[][] visited = new boolean[n][m];
    int maxRoomSize = 0;
    int numRooms = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (!visited[i][j]) {
          int roomSize = floodfill(castle, allWalls, i, j, n, m, visited);
          maxRoomSize = Math.max(maxRoomSize, roomSize);
          numRooms++;
        }
      }   
    }
 
    int wallRemovedX = 0, wallRemovedY = 0;
    String wallDir = null;
    int maxMergedRoomSize = 0;
    // Loop through the rooms, west to east and bottom to top
    for (int j = 0; j < m; j++) {
      for (int i = n - 1; i >= 0; i--) {   

        // If the current room has a N wall, remove it and see how large the combined room will be
        if (allWalls[i][j].contains("N")) {
          allWalls[i][j].remove("N");

          visited = new boolean[n][m];
          int mergedRoomSize = floodfill(castle, allWalls, i, j, n, m, visited);
          if (mergedRoomSize > maxMergedRoomSize) {
            maxMergedRoomSize = mergedRoomSize;
            wallRemovedX = i;
            wallRemovedY = j;
            wallDir = "N";
          }

          allWalls[i][j].add("N");
        } 

        // If the current room has an E wall, remove it and see how large the combined room will be
        if (allWalls[i][j].contains("E")) {
          allWalls[i][j].remove("E");

          visited = new boolean[n][m];
          int mergedRoomSize = floodfill(castle, allWalls, i, j, n, m, visited);
          if (mergedRoomSize > maxMergedRoomSize) {
            maxMergedRoomSize = mergedRoomSize;
            wallRemovedX = i;
            wallRemovedY = j;
            wallDir = "E";
          }

          allWalls[i][j].add("E");
        }
      }
    }
    
    // write output:
    // 1. number of rooms
    // 2. size of largest room
    // 3. size of largest room after removing a wall
    // 4. wall to beremoved (i+1, j+1, wallDir)

    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
    "castle.out")));
    out.println(numRooms);
    out.println(maxRoomSize);
    out.println(maxMergedRoomSize);
    out.print(wallRemovedX+1 + " ");
    out.print(wallRemovedY+1 + " ");
    out.print(wallDir + "\n");
    out.close();
  }

  public static int floodfill(int[][] castle, Set<String>[][] allWalls, int x, int y, int n, int m, boolean[][] visited) {
    int roomSize = 0;

    Stack<Coord> s = new Stack<Coord>();
    Coord coords = new Coord(x, y);
    s.push(coords);
    visited[x][y] = true;

    while (!s.isEmpty()) {
      coords = s.pop();
      x = coords.x;
      y = coords.y;
      roomSize++;

      // get the walls around this room
      Set<String> wallDirections = allWalls[x][y];

      if (!wallDirections.contains("E") && y+1 < m && !visited[x][y+1]) {
        Coord newCoords = new Coord(x, y + 1);
        s.push(newCoords);
        visited[x][y+1] = true;
      }

      if (!wallDirections.contains("W") && y-1 >= 0 && !visited[x][y-1]) {
        Coord newCoords = new Coord(x, y - 1);
        s.push(newCoords);
        visited[x][y-1] = true;
      }

      if (!wallDirections.contains("S") && x+1 < n && !visited[x+1][y]) {
        Coord newCoords = new Coord(x + 1, y);
        s.push(newCoords);
        visited[x+1][y] = true;
      }

      if (!wallDirections.contains("N") && x-1 >= 0 && !visited[x-1][y]) {
        Coord newCoords = new Coord(x - 1, y);
        s.push(newCoords);
        visited[x-1][y] = true;
      }
    }

    return roomSize;
  }

  // returns set of N, S, W, E indicating which walls exist for a given room
  public static Set<String> getWalls(int wallNum) {
    Set<String> wallDirections = new HashSet<String>();

    if(wallNum >= 8) {
			wallDirections.add("S");
			wallNum -= 8;
		}
		
		if(wallNum >= 4) {
			wallDirections.add("E");
			wallNum -= 4;
		}
		
		if(wallNum >= 2) {
			wallDirections.add("N");
			wallNum -= 2;
		}
		
		if(wallNum >= 1) {
			wallDirections.add("W");
			wallNum--;
		}

    return wallDirections;
  }
}

class Coord {
  int x;
  int y;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }
}