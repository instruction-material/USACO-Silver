public class Node {
  public Node left;
  public Node right;
  public int value;
  public boolean visited;

  public Node(Node l, Node r, int val) {
    left = l;
    right = r;
    value = val;
    visited = false;
  }

  public Node[] getChildren() {
    Node[] children = new Node[2];
    children[0] = right;
    children[1] = left;
    return children;
  }
}