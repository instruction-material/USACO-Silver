// adapted from USACO solution

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(in.readLine());

        int k = Integer.parseInt(tokenizer.nextToken());
        int m = Integer.parseInt(tokenizer.nextToken());
        int n = Integer.parseInt(tokenizer.nextToken());

        // store patches with locations and tastiness
        Patch[] patches = new Patch[k];
        for (int j = 0; j < k; j++) {
            tokenizer = new StringTokenizer(in.readLine());
            int location = Integer.parseInt(tokenizer.nextToken());
            long tastiness = Long.parseLong(tokenizer.nextToken());
            patches[j] = new Patch(location, tastiness);
        }

        // sort patches by location
        Arrays.sort(patches);

        // calculate prefix sum of tastiness for patches
        long[] tastinessSums = new long[k + 1];
        for (int j = 0; j < k; j++) {
            tastinessSums[j + 1] = tastinessSums[j] + patches[j].tastiness;
        }

        // array to store locations of Farmer Nhoj's cows
        Integer[] nhojCows = new Integer[m];
        for (int j = 0; j < m; j++) {
            nhojCows[j] = Integer.parseInt(in.readLine());
        }

        // sort Farmer Nhoj's cows
        Arrays.sort(nhojCows);

        // create TreeMap for efficient search and insertion
        TreeMap<Integer, Integer> nhojCowsTreeMap = new TreeMap<>();
        for (int j = 0; j < m; j++) {
            nhojCowsTreeMap.put(nhojCows[j], j);
        }

        // create TreeMap for efficient search of patches
        TreeMap<Integer, Integer> patchTreeMap = new TreeMap<>();
        for (int j = 0; j < k; j++) {
            patchTreeMap.put(patches[j].location, j);
        }

        // 2D array to store values added for one and two cows in each interval
        long[][] valueAdded = new long[3][m + 1];
        for (int j = 0; j < k; j++) {
            // find the next cow in Nhoj's cows list
            Map.Entry<Integer, Integer> nextNhojCowEntry = nhojCowsTreeMap.ceilingEntry(patches[j].location);
            int nextNhojCow = nextNhojCowEntry == null ? m : nextNhojCowEntry.getValue();

            // calculate values added for one and two cows in the interval
            valueAdded[2][nextNhojCow] += patches[j].tastiness;
            if (nextNhojCow == 0 || nextNhojCow == m) {
                valueAdded[1][nextNhojCow] += patches[j].tastiness;
            } else {
                int johnCowLocation = Math.min(nhojCows[nextNhojCow], (2 * patches[j].location) - nhojCows[nextNhojCow - 1]);
                int extent = (johnCowLocation + nhojCows[nextNhojCow] + 1) / 2;
                int farthestPatch = patchTreeMap.lowerEntry(extent).getValue();
                valueAdded[1][nextNhojCow] = Math.max(valueAdded[1][nextNhojCow], tastinessSums[farthestPatch + 1] - tastinessSums[j]);
            }
        }

        // combine values for one and two cows and sort the array
        Long[] valueAddedOverall = new Long[2 * (m + 1)];
        for (int j = 0; j <= m; j++) {
            valueAddedOverall[2 * j] = valueAdded[1][j];
            valueAddedOverall[(2 * j) + 1] = valueAdded[2][j] - valueAdded[1][j];
        }
        Arrays.sort(valueAddedOverall);

        // calculate the maximum total tastiness by summing the highest N values
        long answer = 0;
        for (int j = Math.max(0, valueAddedOverall.length - n); j < valueAddedOverall.length; j++) {
            answer += valueAddedOverall[j];
        }

        System.out.println(answer);
    }

    // Class to represent a grassy patch
    public static class Patch implements Comparable<Patch> {
        final int location;
        final long tastiness;

        public Patch(int location, long tastiness) {
            this.location = location;
            this.tastiness = tastiness;
        }

        @Override
        public int compareTo(Patch other) {
            return location - other.location;
        }
    }
}
