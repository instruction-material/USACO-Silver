// http://usaco.org/index.php?page=viewproblem2&cpid=668

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {

    // read input
    BufferedReader br = new BufferedReader(new FileReader("moocast.in"));
		int n = Integer.parseInt(br.readLine());
    Cow[] cows = new Cow[n];

    for (int i = 0; i < n; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int x = Integer.parseInt(st.nextToken());
      int y = Integer.parseInt(st.nextToken());
      int p = Integer.parseInt(st.nextToken());
      
      cows[i] = new Cow(x,y,p,i);
    }

    // set up graph so that each cow is connected to only the cows it can reach
    for (int i = 0; i < cows.length; i++) {
      for (int j = 0; j < cows.length; j++) {
        if (i != j) {
          Cow a = cows[i];
          Cow b = cows[j];
          double dist = Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
          if (dist <= a.power) {
            a.reachableCows.add(b);
          }
        }
      }
      System.out.println(cows[i].reachableCows.size());
    }

    // run BFS starting from each cow to see the maximum number of cows that can be reached
    int maxReached = 0;
    for (int i = 0; i < cows.length; i++) {
      int numReached = BFS(cows[i],n);
      if (numReached > maxReached) {
        maxReached = numReached;
      }
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("moocast.out")));
    pw.println(maxReached);
    pw.close();
  }

  public static int BFS(Cow root, int numCows) {
    int numReached = 0;
    Queue<Cow> q = new LinkedList<Cow>();
    q.add(root);
    boolean[] visited = new boolean[numCows];
    
    while (!q.isEmpty()) {
      Cow c = q.remove();
      numReached++;
      visited[c.id] = true;
      for (Cow child : c.reachableCows) {
        if (!visited[child.id]) {
          q.add(child);
          visited[child.id] = true; // this is needed so the cow doesn't get added to the queue multiple times
        }
      }
    }

    return numReached;
  }
}

class Cow {
  public int x;
  public int y;
  public int power;
  public int id;    // needed to construct visited array
  public List<Cow> reachableCows;

  public Cow(int x, int y, int power, int id) {
    this.x = x;
    this.y = y;
    this.power = power;
    this.id = id;
    this.reachableCows = new ArrayList<Cow>();
  }
  
  public String toString() {
    return x + ", " + y + ", " + power;
  }
}