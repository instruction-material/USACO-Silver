import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// ADDED: separate unfinished learner; the reference remains in solution/.
class Main {
    public static void main(String[] args) {
        ArrayList<Exam> exams = new ArrayList<>();
        exams.add(new Exam("Abdi", 95));
        exams.add(new Exam("Beyonce", 100));
        exams.add(new Exam("Camila", 87));
        exams.add(new Exam("Diego", 87));
        BankAccount[] accounts = {
            new BankAccount("Emilia", 1000),
            new BankAccount("Farooq", 1050),
            new BankAccount("Gabrielle", 1200),
            new BankAccount("Hassan", 1050)
        };
        try {
            Collections.sort(exams);
            Arrays.sort(accounts, Main::compareBankAccounts);
            // ADDED: print results only after both learner tasks succeed.
            System.out.println(exams);
            System.out.println(Arrays.toString(accounts));
        } catch (UnsupportedOperationException unfinished) {
            System.err.println("Unfinished learner task: " + unfinished.getMessage());
            System.exit(2);
        }
    }

    static int compareBankAccounts(BankAccount left, BankAccount right) {
        // ADDED TASK 2: descending balance, then ascending name; avoid subtraction.
        throw new UnsupportedOperationException("TASK 2: bank account ordering");
    }
}

class Exam implements Comparable<Exam> {
    String name;
    int score;

    Exam(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int compareTo(Exam other) {
        // ADDED TASK 1: ascending score, then ascending name; avoid subtraction.
        throw new UnsupportedOperationException("TASK 1: exam ordering");
    }

    public String toString() {
        return "Name: " + name + " | score: " + score;
    }
}

class BankAccount {
    String name;
    int balance;

    BankAccount(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public String toString() {
        return "Name: " + name + " | balance: " + balance;
    }
}
