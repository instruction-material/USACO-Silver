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

    DFS(root);
    // System.out.println(DFS2(root, 10));
    // System.out.println(DFS2(root, 0));
    // System.out.println(DFS2(root, 4));
    // recursiveDFS(root);

    // DFS that simply prints each value along the way
  }

  public static void DFS(Node root) {
    Stack<Node> s = new Stack<Node>();
    s.push(root);
    root.visited = true;
    while (!s.isEmpty()) {
      Node n = s.pop();
      System.out.println(n.value);
      for (Node child : n.getChildren()) {
        if (child != null && !child.visited) {
          child.visited = true;
          s.push(child);
        }
      }
    }
  }

  // DFS that searches for a value
  public static boolean DFS2(Node root, int target) {
    Stack<Node> s = new Stack<Node>();
    s.push(root);
    root.visited = true;
    while (!s.isEmpty()) {
      Node n = s.pop();
      if (n.value == target) {
        return true;
      }
      for (Node child : n.getChildren()) {
        if (child != null && !child.visited) {
          child.visited = true;
          s.push(child);
        }
      }
    }
    return false;
  }

  // recursive DFS (note that this must be called on a fresh graph where nothing has been visited)
  public static void recursiveDFS(Node n) {
    System.out.println(n.value);
    n.visited = true;
    for (Node child : n.getChildren()) {
      if (child != null && !child.visited) {
        recursiveDFS(child);
      }
    }
  }
}