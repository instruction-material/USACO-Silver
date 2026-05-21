// http://www.usaco.org/index.php?page=viewproblem2&cpid=690

import java.util.*;
import java.io.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("cowdance.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
    int tmax = Integer.parseInt(st.nextToken());
    int[] durations = new int[N];

    for (int i = 0; i < N; i++) {
      durations[i] = Integer.parseInt(br.readLine());
    }

    br.close();

    // binary search between 1 and N to find the max possible value of K
    int min = 1;
    int max = N;
    while (min < max) {
      int mid = (min + max) / 2;
      if (canFinishDance(durations, mid, tmax)) {
        max = mid;
      } else {
        min = mid + 1;
      }
    }

    // at this point, min should equal K

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("cowdance.out")));
    pw.println(min);
    pw.close();
  }

  // returns true if having K cows on the stage at a time will allow the cows to finish dancing before tmax
  public static boolean canFinishDance(int[] durations, int k, int tmax) {
    // priority queues are queues where the elements are inserted with some ordering. takes O(logn) time for insertion and O(1) time for polling
    PriorityQueue<Integer> activeDancers = new PriorityQueue<Integer>();

    // put the first k cows on stage
    for (int i = 0; i < k; i++) {
      activeDancers.add(durations[i]);
    }

    // as dancers finish, replace them with the next dancer (when adding into the queue, add the time they will finish)
    for (int i = k; i < durations.length; i++) {
      int finishTime = activeDancers.poll();
      activeDancers.add(durations[i] + finishTime);
    }

    // get the largest time left in the priority queue and compare it against tmax
    int finishTime = 0;
    while (activeDancers.size() > 0) {
      finishTime = Math.max(finishTime, activeDancers.poll());
    }
    
    return finishTime <= tmax;
  }
}