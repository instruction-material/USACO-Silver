// http://www.usaco.org/index.php?page=viewproblem2&cpid=991

import java.io.*;
import java.util.*;

class Main {
  static long N;
  static long K;
  static long M;

  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("loan.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    
    // N = number gallons of milk
    // K = number of days within repayment must happen
    // M = min gallons per day to repay
    N = Long.parseLong(st.nextToken());
    K = Long.parseLong(st.nextToken());
    M = Long.parseLong(st.nextToken());

    // we can binary search on X, checking whether for a given X, following the rules, we pay N within K days. if we do, we can make X larger

    long low = 1;
    long high = N;
    while (low < high) {
      long cur = (low + high + 1) / 2;

      if (canRepayOptimized(cur)) {
        low = cur;
      } else {
        high = cur - 1;
      }
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("loan.out")));
    pw.println(low);
    pw.close();

  }

  // this is the simple version of canRepay()
  public static boolean canRepay(long X) {
    long totalMilkRepaid = 0;
    long numDays = 0;

    while (totalMilkRepaid < N) {
      long Y = (N-totalMilkRepaid) / X;
      if (Y < M) {
        Y = M;
      }
      totalMilkRepaid += Y;
      numDays += 1;
    }

    return numDays <= K;
  }

  // canRepayOptimized() runs in O(sqrt(N)) time. the idea here is that instead of looping through every day, we can calculate all of the transitions that leave Y constant at once
  public static boolean canRepayOptimized (long X) {
    long totalMilkRepaid = 0;
    long numDays = 0;

    while (true) {
      // calculate numDaysAtY, which is the total number of days (N-G)/X remains fixed at the current Y
      long currY = (N-totalMilkRepaid)/X;

      // if currY is less than M, this mean the remaining days will pay M gallons per day
      if (currY < M) {
        long milkRemaining = N - totalMilkRepaid;
        long daysAtM = milkRemaining/M;
        if (milkRemaining % M != 0) {
          daysAtM++;
        }
        numDays += daysAtM;
        break;
      }

      // the numerator (N-totalMilkRepaid) is going to get smaller as the days go on. the smallest it can get to is X * currY
      long minNumerator = X * currY;

      // numDaysAtY is then equal to the difference between (n-totalMilkRepaid) and minNumerator, divided by currY (because we repay currY gallons of milk per day), plus one at the end
      long numDaysAtY = ((N-totalMilkRepaid) - minNumerator) / currY + 1;

      // we don't want to spend more days at Y that needed - calculate if we can finish repaying faster
      long milkRemaining = N - totalMilkRepaid;
      long totalMilkAtY = currY * numDaysAtY;

      if (totalMilkAtY > milkRemaining) {
        // this means we can finish repaying at this Y
        numDaysAtY = milkRemaining/currY;
        if (milkRemaining % currY != 0) {
          numDaysAtY++;
        }
        numDays += numDaysAtY;
        break;
      } else {
        totalMilkRepaid += totalMilkAtY;
        numDays += numDaysAtY;
      }

      // System.out.println(currY + " " + numDaysAtY + " " + totalMilkAtY + " " + totalMilkRepaid);
    }

    return numDays <= K;
  }
}