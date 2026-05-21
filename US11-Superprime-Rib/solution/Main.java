/*
ID: roobeel1
LANG: JAVA
TASK: sprime
*/

import java.io.*;
import java.util.*;

class sprime {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("sprime.in"));
    int n = Integer.parseInt(br.readLine());
    br.close();

    // recursively generate potential superprimes
    ArrayList<Integer> superprimes = generateSuperprimes(n);

    // output answer
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("sprime.out")));
    for (int num : superprimes) {
      pw.println(num);
    }
    pw.close();
  }

  // wrapper method for recursion
  public static ArrayList<Integer> generateSuperprimes (int numDigits) {
    // pass in one-digit primes into recursive method
    ArrayList<Integer> startingNums = new ArrayList<Integer>();
    startingNums.add(2);
    startingNums.add(3);
    startingNums.add(5);
    startingNums.add(7);
    ArrayList<Integer> superprimes = checkNums(startingNums, numDigits, 1);
    return superprimes;
  }

  public static ArrayList<Integer> checkNums(ArrayList<Integer> superprimes, int goalNumDigits, int currNumDigits) {
    if (goalNumDigits == currNumDigits) {
      return superprimes;
    } else {
      // create new list of superprimes with currNumDigits
      ArrayList<Integer> newSuperprimes = new ArrayList<Integer>();
      for (int superprime : superprimes) {
        for (int i = 1; i <= 9; i+=2) {
          int num = Integer.parseInt(Integer.toString(superprime) + Integer.toString(i));
          if (isPrime(num)) {
            newSuperprimes.add(num);
          }
        }
      }

      return checkNums(newSuperprimes, goalNumDigits, currNumDigits+1);
    }
  }

  public static boolean isPrime(int num) {
    // check divisors up to sqrt(num)

    for (int i = 2; i <= Math.sqrt(num); i++) {
      if (num % i == 0) {
        return false;
      }
    }

    return true;
  }
}