/*
ID: roobeel1
LANG: JAVA
TASK: pprime
*/

import java.io.*;
import java.util.*;

class pprime {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("pprime.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int a = Integer.parseInt(st.nextToken());
    int b = Integer.parseInt(st.nextToken());
    br.close();

    // generate all the palindromes between 5 and 100,000,000, inclusive
    ArrayList<Integer> palindromes = new ArrayList<Integer>();
    for (int numDigits = 1; numDigits <= 8; numDigits++) {
      ArrayList<Integer> temp = generatePalindromes(numDigits);
      for (int num : temp) {
        palindromes.add(num);
      }
    }

    // pick out all prime palindromes between a and b, inclusive
    ArrayList<Integer> answer = new ArrayList<Integer>();
    for (int num : palindromes) {
      if (num >= a && num <= b && isPrime(num)) {
        answer.add(num);
      }
    }

    // output answer
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("pprime.out")));
    for (int num : answer) {
      pw.println(num);
    }
    pw.close();
  }

  public static ArrayList<Integer> generatePalindromes(int numDigits) {
    // only generate odd palindromes
    ArrayList<Integer> answer = new ArrayList<Integer>();

    if (numDigits == 1) {
      for (int a = 1; a <= 9; a+=2) {
        answer.add(a);
      }
    } else if (numDigits == 2) {
      for (int a = 1; a <= 9; a+=2) {
        int num = Integer.parseInt(Integer.toString(a) + Integer.toString(a));
        answer.add(num);
      }
    } else if (numDigits == 3) {
      for (int a = 1; a <= 9; a+=2) {
        for (int b = 0; b <= 9; b++) {
          int num = Integer.parseInt(Integer.toString(a) + Integer.toString(b) + Integer.toString(a));
          answer.add(num);
        }
      }
    } else if (numDigits == 4) {
      for (int a = 1; a <= 9; a+=2) {
        for (int b = 0; b <= 9; b++) {
          int num = Integer.parseInt(Integer.toString(a) + Integer.toString(b) + Integer.toString(b) + Integer.toString(a));
          answer.add(num);
        }
      }
    } else if (numDigits == 5) {
      for (int a = 1; a <= 9; a+=2) {
        for (int b = 0; b <= 9; b++) {
          for (int c = 0; c <= 9; c++) {
            int num = Integer.parseInt(Integer.toString(a) + Integer.toString(b) + 
            Integer.toString(c) + Integer.toString(b) + Integer.toString(a));
            answer.add(num);
          }
        }
      }
    } else if (numDigits == 6) {
      for (int a = 1; a <= 9; a+=2) {
        for (int b = 0; b <= 9; b++) {
          for (int c = 0; c <= 9; c++) {
            int num = Integer.parseInt(Integer.toString(a) + Integer.toString(b) + Integer.toString(c) + Integer.toString(c) + Integer.toString(b) + Integer.toString(a));
            answer.add(num);
          }
        }
      }
    } else if (numDigits == 7) {
      for (int a = 1; a <= 9; a+=2) {
        for (int b = 0; b <= 9; b++) {
          for (int c = 0; c <= 9; c++) {
            for (int d = 0; d <= 9; d++) {
              int num = Integer.parseInt(Integer.toString(a) + Integer.toString(b) + Integer.toString(c) + Integer.toString(d) + Integer.toString(c) + Integer.toString(b) + Integer.toString(a));
              answer.add(num);
            }
            
          }
        }
      }
    } else if (numDigits == 8) {
      for (int a = 1; a <= 9; a+=2) {
        for (int b = 0; b <= 9; b++) {
          for (int c = 0; c <= 9; c++) {
            for (int d = 0; d <= 9; d++) {
              int num = Integer.parseInt(Integer.toString(a) + Integer.toString(b) + Integer.toString(c) + Integer.toString(d) + Integer.toString(d) +  Integer.toString(c) + Integer.toString(b) + Integer.toString(a));
              answer.add(num);
            }
            
          }
        }
      }
    }

    return answer;
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