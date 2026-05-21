// http://www.usaco.org/index.php?page=viewproblem2&cpid=788

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input (n and q)
    BufferedReader br = new BufferedReader(new FileReader("mootube.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
		int n = Integer.parseInt(st.nextToken());
		int q = Integer.parseInt(st.nextToken());

    // create list of videos
    Video[] videos = new Video[n];
    for (int i = 0; i < n; i++) {
      Video v = new Video(i);
      videos[i] = v;
    }
    
    // read input (all connections)
    for (int i = 0; i < n-1; i++) {
      st = new StringTokenizer(br.readLine());
			int video1 = Integer.parseInt(st.nextToken());
			int video2 = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken());
      
      // add Connection to video1
      Connection c1 = new Connection(video2-1, r);
      videos[video1-1].connections.add(c1);

      // add Connection to video2
      Connection c2 = new Connection(video1-1,r);
      videos[video2-1].connections.add(c2);
    }

    // BFS on each video FJ questions about

    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("mootube.out")));

    for (int i = 0; i < q; i++) {
      st = new StringTokenizer(br.readLine());
			int k = Integer.parseInt(st.nextToken());
			int videoId = Integer.parseInt(st.nextToken());
      pw.println(BFS(videos[videoId-1], k, n, videos));
    }
    pw.close();
  }

  // BFS to find number of videos connected to this video with relevance >= k along the entire path
  public static int BFS(Video video, int k, int numVideos, Video[] videos) {
    int numReached = 0;
    Queue<Video> q = new LinkedList<Video>();
    q.add(video);
    boolean[] visited = new boolean[numVideos];

    while (!q.isEmpty()) {
      Video v = q.remove();
      numReached++;
      visited[v.id] = true;
      for (Connection c : v.connections) {
        if (!visited[c.id] && c.relevance >= k) {
          q.add(videos[c.id]);
          visited[c.id] = true;
        }
      }
    }

    // subtract out the initial video
    return numReached-1;
  }
}

class Video {
  public int id;
  public List<Connection> connections;

  public Video(int id) {
    this.id = id;
    this.connections = new ArrayList<Connection>();
  }
}

class Connection {
  int id;
  int relevance;

  public Connection(int id, int relevance) {
    this.id = id;
    this.relevance = relevance;
  }
}