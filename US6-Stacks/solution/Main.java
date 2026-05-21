import java.util.*;

class Main {
  public static void main(String[] args) {
    // Example 1
    Stack<Integer> s = new Stack<Integer>();
    s.push(1);
    s.push(2);
    s.push(3);

    System.out.println(s.pop());
    System.out.println(s.pop());
    System.out.println(s.peek());

    System.out.println(s.isEmpty());

    // Example 2
    Stack<Integer> s2 = new Stack<Integer>();
    s.push(10);
    s.push(15);
    s.push(20);

    while (!s.isEmpty()) {
      System.out.println(s.pop());
    }
  }
}