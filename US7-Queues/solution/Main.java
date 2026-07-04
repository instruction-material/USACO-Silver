import java.util.*;

/**
 * @brief Solve the lesson problem using the provided input and output format
 */
class Main {
  /**
   * @brief Read input, compute the answer, and write output
   *
   * @param args Command-line arguments
   */
  public static void main(String[] args) {
    // Example 1
    Queue<Integer> q = new LinkedList<Integer>();
    q.add(1);
    q.add(2);
    q.add(3);

    System.out.println(q.poll());
    System.out.println(q.poll());
    System.out.println(q.peek());

    System.out.println(q.isEmpty());

    // Example 2
    Queue<Integer> q2 = new LinkedList<Integer>();
    q.add(10);
    q.add(15);
    q.add(20);

    while (!q.isEmpty()) {
      System.out.println(q.poll());
    }
  }
}