/*
ID: roobeel1
LANG: JAVA
TASK: milk3
*/

import java.io.*;
import java.util.*;

class milk3 {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("milk3.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
		int a = Integer.parseInt(st.nextToken());
    int b = Integer.parseInt(st.nextToken());
    int c = Integer.parseInt(st.nextToken());
    int[] cap = {a, b, c};

    // create node for starting state (bucket C is full)
    Node root = new Node(0,0,cap[2]);

    // answer will store possibilities for bucket C
    ArrayList<Integer> answer = new ArrayList<Integer>();

    // possible pouring combinations
    int [][] comb = {{0,1,2},{0,2,1},{1,0,2},{1,2,0},{2,0,1},{2,1,0}};

    // isVisited will store which combination of bucket states have been visisted
    boolean [][][] isVisited = new boolean [21][21][21];

    // create stack for DFS and add root
    Stack<Node> s = new Stack<Node>();
    s.add(root);

    while (!s.isEmpty()) {
      Node n = s.pop();

      // if bucket A is empty, add bucket C volume to answer
      if (n.milk[0] == 0 && !answer.contains(n.milk[2])) {
        answer.add(n.milk[2]);
      }
      isVisited[n.milk[0]][n.milk[1]][n.milk[2]] = true;

      // try every permutation of pouring
      for (int i = 0; i < comb.length; i++) {
        Node newCombo = n.pour(n, comb[i][0], comb[i][1], cap[comb[i][1]]);
        if (!isVisited[newCombo.milk[0]][newCombo.milk[1]][newCombo.milk[2]]) {
          s.push(newCombo);
        }
      }
    }

    // sort the answer list
    Collections.sort(answer);
    
    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("milk3.out")));

    for (int i = 0; i < answer.size(); i++) {
      pw.print(answer.get(i));
      if (i != answer.size()-1) {
        pw.print(" ");
      }
    }
    pw.print("\n");
    pw.close();
  }
}

class Node {
  public int[] milk = new int[3];

  public Node(int i0, int i1, int i2) {
    this.milk[0] = i0;
    this.milk[1] = i1;
    this.milk[2] = i2;
  }

  public Node(Node n) {
    this.milk[0] = n.milk[0];
    this.milk[1] = n.milk[1];
    this.milk[2] = n.milk[2];
  }

  public Node pour(Node n, int from, int to, int toCap) {
    Node newN = new Node(n);

    // calculate how much can be poured
    int toPour = toCap - newN.milk[to];
    if (toPour > newN.milk[from]) {
        toPour = newN.milk[from];
    }

    // update the milk amounts
    newN.milk[to] = newN.milk[to]+toPour;
    newN.milk[from] = newN.milk[from]-toPour;

    return newN;
  }

  public String toString() {
    return this.milk[0] + " " + this.milk[1] + " " + this.milk[2];
  }
}