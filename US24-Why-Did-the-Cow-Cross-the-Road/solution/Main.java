import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("helpcross.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
		int numChickens = Integer.parseInt(st.nextToken());
    int numCows = Integer.parseInt(st.nextToken());
    Animal[] animals = new Animal[numChickens+numCows];
    
    int[] chickens = new int[numChickens];
    for (int i = 0; i < numChickens; i++) {
      int time = Integer.parseInt(br.readLine());
      animals[i] = new Animal("chicken", time, time);
    }

    int[][] cows = new int[numCows][2];
    for (int i = 0; i < numCows; i++) {
      st = new StringTokenizer(br.readLine());
      int start = Integer.parseInt(st.nextToken());
      int end = Integer.parseInt(st.nextToken());
      animals[i+numChickens] = new Animal("cow", start, end);
    }

    br.close();

    // sort chickens and cows
    Arrays.sort(animals);

    // go through the list of animals. add every cow's end time to the priority queue. when we see a chicken, pair it with the cow with the earliest end time. because cows come before chickens in the case of ties, if a chicken comes first in the list, we can essentially ignore it.
    PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
    int numPairs = 0;
    for (int i = 0; i < animals.length; i++) {
      if (animals[i].type == "cow") {
        pq.add(animals[i].end);
      } else {
        // remove cows no longer available
        while (pq.size() > 0 && pq.peek() < animals[i].start) {
          pq.poll();
        }

        // pair this chicken with the cow (guaranteed to work because we added the cow's end time into the pq)
        if (pq.size() > 0) {
          numPairs++;
          pq.poll();
        }
      }
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("helpcross.out")));
    pw.println(numPairs);
    pw.close();
  }
}

class Animal implements Comparable<Animal>{
  public String type;
  public int start;
  public int end;

  public Animal(String t, int s, int e) {
    type = t;
    start = s;
    end = e;
  }

  public int compareTo(Animal a) {
    if (start != a.start) {
      return start - a.start;
    } else if (a.type == "cow") {
      return 1;
    } else {
      return -1;
    }
  }
}