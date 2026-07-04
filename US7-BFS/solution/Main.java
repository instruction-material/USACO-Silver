import java.util.LinkedList;
import java.util.Queue;

// Demonstrate breadth-first traversal on a small binary tree
class Main {
    private static final int ROOT_VALUE = 0;
    private static final int LEFT_CHILD_VALUE = 1;
    private static final int RIGHT_CHILD_VALUE = 2;
    private static final int LEFT_GRANDCHILD_VALUE = 3;
    private static final int RIGHT_GRANDCHILD_VALUE = 4;

    /**
     * @brief Build the sample tree and print a BFS traversal
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        Node root = new Node(null, null, ROOT_VALUE);
        Node left_child = new Node(null, null, LEFT_CHILD_VALUE);
        Node right_child = new Node(null, null, RIGHT_CHILD_VALUE);
        Node left_grandchild = new Node(null, null, LEFT_GRANDCHILD_VALUE);
        Node right_grandchild = new Node(null, null, RIGHT_GRANDCHILD_VALUE);

        root.left = left_child;
        root.right = right_child;
        left_child.left = left_grandchild;
        left_child.right = right_grandchild;

        breadthFirstSearch(root);
    }

    /**
     * @brief Print each tree value using iterative BFS
     *
     * @param root Starting node for the traversal
     */
    public static void breadthFirstSearch(Node root) {
        Queue<Node> node_queue = new LinkedList<Node>();
        node_queue.add(root);
        root.visited = true;

        // Process nodes until the queue is empty
        while (!node_queue.isEmpty()) {
            Node current_node = node_queue.poll();
            System.out.println(current_node.value);

            // Visit each unvisited child node
            for (Node child : current_node.getChildren()) {
                // Skip null children and nodes already reached
                if (child != null && !child.visited) {
                    child.visited = true;
                    node_queue.add(child);
                }
            }
        }
    }
}
