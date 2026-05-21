import java.util.*;

class Main {
  public static void main(String[] args) {
    HashSet<Integer> set = new HashSet<Integer>();

    set.add(1);
    set.add(2);
    set.add(3);
    set.add(3);

    System.out.println(set);
    set.remove(3);
    System.out.println(set);

    System.out.println(set.contains(4));
    System.out.println(set.isEmpty());
    System.out.println(set.size());

    HashSet<Integer> set2 = new HashSet<Integer>();
    set2.add(2);
    set2.add(3);
    set2.add(4);

    set.addAll(set2);  // union
    System.out.println(set);

    set.retainAll(set2);  // intersection
    System.out.println(set);

    // 1. Save a word of your choice in a string. Then, use a set to print out all of the unique letters in the word and the number of unique letters in the word.
    // 2. Save a second word of your choice in another string. Print out the letters these two word have in common.
    // 3. Write a method that takes in a HashSet of numbers and returns a HashSet with only the even numbers from that set.
    // 4. Write a method that takes in a HashSet of words and returns a HashSet with only the words that have 3 letters in them.
  }
}