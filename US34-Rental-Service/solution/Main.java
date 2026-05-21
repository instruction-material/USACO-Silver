import java.io.*;
import java.util.*;
import java.lang.Object;

class Main {
  public static void main(String[] args) throws IOException {
    // read input
    BufferedReader br = new BufferedReader(new FileReader("rental.in"));
    StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    int R = Integer.parseInt(st.nextToken());
    
    int[] cows = new int[N];
    for (int i = 0; i < N; i++) {
      cows[i] = Integer.parseInt(br.readLine());
    }

    int[][] stores = new int[M][2];
    for (int i = 0; i < M; i++) {
      st = new StringTokenizer(br.readLine());
      int gallons = Integer.parseInt(st.nextToken());
      int cents = Integer.parseInt(st.nextToken());
      int[] temp = {gallons, cents};
      stores[i] = temp;
    }

    int[] renters = new int[R];
    for (int i = 0; i < R; i++) {
      renters[i] = Integer.parseInt(br.readLine());
    }

    br.close();

    // sort cows, stores, and renters (backwards)
    Arrays.sort(cows);
    int[] cowsTemp = new int[N];
    for (int i = 0; i < N; i++) {
      cowsTemp[i] = cows[N-i-1];
    }
    cows = cowsTemp;

    Arrays.sort(stores, new Comparator<int[]>() {
      @Override
      public int compare(int[] store1, int[] store2) {
        return store2[1] - store1[1];
      }
    });

    Arrays.sort(renters);
    int[] rentersTemp = new int[R];
    for (int i = 0; i < R; i++) {
      rentersTemp[i] = renters[R-i-1];
    }
    renters = rentersTemp;

    // construct an array that holds the amount of money made if 0 cows are rented, 1 cow is rented, 2 cows are rented, etc.
    int[] rentalMoney = new int[N+1];
    for (int i = 1; i <= N; i++) {
      int rentAmt = 0;
      if (i <= R) {
        rentAmt = renters[i-1];
      }
      rentalMoney[i] = rentalMoney[i-1] + rentAmt;
    }

    // also construct an array that holds the amount of money made if 0 cows are used for milking, 1 cow is used for milking, 2 cows are used for milking, etc.
    int storeIndex = 0;
    int storeGallonsUsed = 0;
    int[] storeMoney = new int[N+1];

    for (int i = 1; i <= N; i++) {
      int money = 0;

      if (storeIndex < M) {
        int cowGallonsUsed = 0;
        int cowGallonsTotal = cows[i-1];

        while(cowGallonsUsed < cowGallonsTotal) {
          int gallons = Math.min(stores[storeIndex][0]-storeGallonsUsed, cowGallonsTotal-cowGallonsUsed);

          money += gallons * stores[storeIndex][1];
          cowGallonsUsed += gallons;
          storeGallonsUsed += gallons;

          if (storeGallonsUsed == stores[storeIndex][0]) {
            storeIndex++;
            storeGallonsUsed = 0;
          }

          if (storeIndex >= M) {
            break;
          }
        }
      }

      storeMoney[i] = storeMoney[i-1] + money;
    }

    long maxMoney = 0;
    for (int i = 0; i <= N; i++) {
      maxMoney = Math.max(maxMoney, rentalMoney[i] + storeMoney[N-i]);
    }

    // write output
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("rental.out")));
    pw.println(maxMoney);
    pw.close();
  }
}