import java.util.*;

class Main {
  public static void main(String[] args) {
    Node root = new Node(null, null, 0);
    Node node1 = new Node(null, null, 1);
    Node node2 = new Node(null, null, 2);
    Node node1l = new Node(null, null, 3);
    Node node1r = new Node(null, null, 4);
    root.left = node1;
    root.right = node2;
    node1.left = node1l;
    node1.right = node1r;

    BFS(root);
  }

  public static void BFS(Node root) {
    Queue<Node> q = new LinkedList<Node>();
    q.add(root);
    root.visited = true;
    while(!q.isEmpty()) {
      Node n = q.poll();
      System.out.println(n.value);
      for (Node child : n.getChildren()) {
        if (child != null && !child.visited) {
          child.visited = true;
          q.add(child);
        }
      }
    }
  }
}