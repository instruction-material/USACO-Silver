// Store a binary tree node for traversal lessons
public class Node {
    private static final int CHILD_COUNT = 2;
    private static final int RIGHT_CHILD_INDEX = 0;
    private static final int LEFT_CHILD_INDEX = 1;

    public Node left;
    public Node right;
    public int value;
    public boolean visited;

    /**
     * @brief Build one tree node
     *
     * @param leftChild Left child reference
     *
     * @param rightChild Right child reference
     *
     * @param nodeValue Value stored in this node
     */
    public Node(Node leftChild, Node rightChild, int nodeValue) {
        left = leftChild;
        right = rightChild;
        value = nodeValue;
        visited = false;
    }

    /**
     * @brief Return children in traversal order
     *
     * @return Child references ordered right then left
     */
    public Node[] getChildren() {
        Node[] children = new Node[CHILD_COUNT];
        children[RIGHT_CHILD_INDEX] = right;
        children[LEFT_CHILD_INDEX] = left;
        return children;
    }
}
