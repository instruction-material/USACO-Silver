/*
ID: roobeel1
LANG: JAVA
TASK: frac1
*/

import java.io.*;
import java.util.*;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("frac1.in"));
    int N = Integer.parseInt(br.readLine());

    ArrayList<Fraction> res = new ArrayList<Fraction>();

    // test all possible options
    for (int denom = 1; denom <= N; denom++) {
      for (int numer = 0; numer <= denom; numer++) {
        if (isReduced(numer, denom)) {
          Fraction f = new Fraction(numer, denom);
          res.add(f);
        }
      }
    }

    // sort array
    Collections.sort(res);
    
    // write output
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
    "frac1.out")));
    for (Fraction f : res) {
      out.println(f);
    }
    out.close();
  }

  public static boolean isReduced(int numer, int denom) {
    // check if any number up to denom/2 divides into both
    for (int i = 2; i <= denom/2; i++) {
      if (numer % i == 0 && denom % i == 0) {
        return false;
      }
    }

    // special case to remove 0/2, 0/3, 0/4, 0/5, etc.
    if (numer == 0 && denom != 1) {
      return false;
    }

    // special case to remove 2/2, 3/3, 4/4, 5/5, etc.
    if (numer == denom && numer != 1) {
      return false;
    }

    return true;
  }
}

class Fraction implements Comparable<Fraction> {
  public int numerator;
  public int denominator;
  
  public Fraction (int numer, int denom) {
    numerator = numer;
    denominator = denom;
  }

  public String toString() {
    return numerator + "/" + denominator;
  }

  public int compareTo(Fraction f2) {
    double val = (double)this.numerator / this.denominator;
    double val2 = (double)f2.numerator / f2.denominator;

    if (val < val2) {
      return -1;
    } else if (val == val2) {
      return 0;
    } else {
      return 1;
    }
  }
}