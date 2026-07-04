import java.util.Stack;

// Demonstrate depth-first traversal on a small binary tree
class Main {
    private static final int ROOT_VALUE = 0;
    private static final int LEFT_CHILD_VALUE = 1;
    private static final int RIGHT_CHILD_VALUE = 2;
    private static final int LEFT_GRANDCHILD_VALUE = 3;
    private static final int RIGHT_GRANDCHILD_VALUE = 4;

    /**
     * @brief Build the sample tree and print a DFS traversal
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

        depthFirstSearch(root);
        // System.out.println(containsWithDepthFirstSearch(root, 10))
        // System.out.println(containsWithDepthFirstSearch(root, 0))
        // System.out.println(containsWithDepthFirstSearch(root, 4))
        // recursiveDepthFirstSearch(root)
    }

    /**
     * @brief Print each tree value using iterative DFS
     *
     * @param root Starting node for the traversal
     */
    public static void depthFirstSearch(Node root) {
        Stack<Node> node_stack = new Stack<Node>();
        node_stack.push(root);
        root.visited = true;

        // Process nodes until the stack is empty
        while (!node_stack.isEmpty()) {
            Node current_node = node_stack.pop();
            System.out.println(current_node.value);

            // Visit each unvisited child node
            for (Node child : current_node.getChildren()) {
                // Skip null children and nodes already reached
                if (child != null && !child.visited) {
                    child.visited = true;
                    node_stack.push(child);
                }
            }
        }
    }

    /**
     * @brief Search a tree value using iterative DFS
     *
     * @param root Starting node for the traversal
     *
     * @param target Value to find
     *
     * @return True when the target value is found
     */
    public static boolean containsWithDepthFirstSearch(Node root, int target) {
        Stack<Node> node_stack = new Stack<Node>();
        node_stack.push(root);
        root.visited = true;

        // Process nodes until the stack is empty
        while (!node_stack.isEmpty()) {
            Node current_node = node_stack.pop();

            // Return as soon as the target value is found
            if (current_node.value == target) {
                return true;
            }

            // Visit each unvisited child node
            for (Node child : current_node.getChildren()) {
                // Skip null children and nodes already reached
                if (child != null && !child.visited) {
                    child.visited = true;
                    node_stack.push(child);
                }
            }
        }

        return false;
    }

    /**
     * @brief Print each tree value using recursive DFS
     *
     * @param currentNode Node currently being visited
     */
    public static void recursiveDepthFirstSearch(Node currentNode) {
        System.out.println(currentNode.value);
        currentNode.visited = true;

        // Recursively visit each unvisited child node
        for (Node child : currentNode.getChildren()) {
            // Skip null children and nodes already reached
            if (child != null && !child.visited) {
                recursiveDepthFirstSearch(child);
            }
        }
    }
}
