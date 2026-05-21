//Adapted from USACO 

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<Integer> eastCows = new ArrayList<>();
        List<Integer> northCows = new ArrayList<>();
        int n = in.nextInt();
        int[] xs = new int[n];
        int[] ys = new int[n];
        for (int j = 0; j < n; j++) {
            if (in.next().charAt(0) == 'E') {
                eastCows.add(j);
            } else {
                northCows.add(j);
            }
            xs[j] = in.nextInt();
            ys[j] = in.nextInt();
        }
        eastCows.sort(Comparator.comparingInt(j -> ys[j]));
        northCows.sort(Comparator.comparingInt(j -> xs[j]));
        boolean[] isStopped = new boolean[n];
        int[] amtStopped = new int[n];
        for (int j : eastCows) {
            for (int k : northCows) {
                if (!isStopped[j] && !isStopped[k] && xs[k] > xs[j] && ys[j] > ys[k]) {
                    if (xs[k] - xs[j] > ys[j] - ys[k]) {
                        isStopped[j] = true;
                        amtStopped[k] += 1 + amtStopped[j];
                    } else if (ys[j] - ys[k] > xs[k] - xs[j]) {
                        isStopped[k] = true;
                        amtStopped[j] += 1 + amtStopped[k];
                    }
                }
            }
        }
        for (int j = 0; j < n; j++) {
            System.out.println(amtStopped[j]);
        }
    }
}