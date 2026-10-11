// ADDED: separate unfinished graph-search learner; reference Main.java is preserved.
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class milk3 {
    public static void main(String[] args) throws IOException {
        try (Scanner input = new Scanner(new File("milk3.in"))) {
            int[] capacity = new int[3];
            for (int i = 0; i < capacity.length; i++) {
                if (!input.hasNextInt()) {
                    throw new IllegalArgumentException("Expected three integer capacities.");
                }
                capacity[i] = input.nextInt();
                if (capacity[i] < 1 || capacity[i] > 20) {
                    throw new IllegalArgumentException("Capacities must be in 1..20.");
                }
            }
            if (input.hasNext()) {
                throw new IllegalArgumentException("Unexpected extra input.");
            }
            ArrayList<Integer> answer = reachableVolumes(capacity);
            Collections.sort(answer);
            // ADDED: open the output only after the learner task succeeds.
            try (PrintWriter output = new PrintWriter("milk3.out")) {
                for (int i = 0; i < answer.size(); i++) {
                    if (i > 0) output.print(" ");
                    output.print(answer.get(i));
                }
                output.println();
            }
        } catch (UnsupportedOperationException unfinished) {
            System.err.println("Unfinished learner task: " + unfinished.getMessage());
            System.exit(2);
        } catch (IllegalArgumentException invalid) {
            System.err.println("Invalid input: " + invalid.getMessage());
            System.exit(2);
        }
    }

    static ArrayList<Integer> reachableVolumes(int[] capacity) {
        // ADDED TASK: explore the full state graph and collect C when A is empty.
        throw new UnsupportedOperationException("TASK: reachable bucket states");
    }
}

class Node {
    public int[] milk = new int[3];

    public Node(int i0, int i1, int i2) {
        this.milk[0] = i0;
        this.milk[1] = i1;
        this.milk[2] = i2;
    }

    public Node(Node n) {
        this.milk[0] = n.milk[0];
        this.milk[1] = n.milk[1];
        this.milk[2] = n.milk[2];
    }

    public Node pour(Node n, int from, int to, int toCap) {
        Node newN = new Node(n);

        // calculate how much can be poured
        int toPour = toCap - newN.milk[to];
        if (toPour > newN.milk[from]) {
            toPour = newN.milk[from];
        }

        // update the milk amounts
        newN.milk[to] = newN.milk[to] + toPour;
        newN.milk[from] = newN.milk[from] - toPour;

        return newN;
    }

    public String toString() {
        return this.milk[0] + " " + this.milk[1] + " " + this.milk[2];
    }
}
