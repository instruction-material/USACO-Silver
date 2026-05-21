import java.util.*;

class Main {
  public static void main(String[] args) {
    HashMap<String, Integer> prices = new HashMap<String, Integer>();

    prices.put("ice cream", 5);
    prices.put("brownie", 2);
    prices.put("cake", 10);

    System.out.println(prices);

    // decrease the price of the brownie
    prices.put("brownie", prices.get("brownie")-1);

    System.out.println(prices.get("brownie"));

    System.out.println(prices.containsKey("ice cream"));
    System.out.println(prices.containsValue(10));
    System.out.println(prices.size());

    for (String key : prices.keySet()) {
      System.out.println(key);
    }

    // 1. Create a HashMap where the keys are the numbers 1 to 25 and the values are their factorials. Then, print out the HashMap.
    // 2.   Save a word of your choice in a string. Then, create a HashMap where the keys are the letters in the word and the values are the number of times each letter appears in the word. Then, print out the HashMap.
  }
}