import java.util.*;

class Main {
  public static void main(String[] args) {
    Exam exam1 = new Exam("Abdi", 95);
    Exam exam2 = new Exam("Beyonce", 100);
    Exam exam3 = new Exam("Camila", 87);
    Exam exam4 = new Exam("Diego", 87);

    ArrayList<Exam> listExams = new ArrayList<Exam>();
    listExams.add(exam1);
    listExams.add(exam2);
    listExams.add(exam3);
    listExams.add(exam4);

    Collections.sort(listExams);
    System.out.println(listExams);

    BankAccount ba1 = new BankAccount("Emilia", 1000);
    BankAccount ba2 = new BankAccount("Farooq", 1050);
    BankAccount ba3 = new BankAccount("Gabrielle", 1200);
    BankAccount ba4 = new BankAccount("Hassan", 1050);
    BankAccount[] arrayBankAccounts = new BankAccount[] {ba1, ba2, ba3, ba4};

    Arrays.sort(arrayBankAccounts, (BankAccount b1, BankAccount b2) -> {
      if (b1.balance != b2.balance) {
        return b2.balance - b1.balance;
      } else {
        return b1.name.compareTo(b2.name);
      }
    });
    
    System.out.println(Arrays.toString(arrayBankAccounts));
  }
}

class Exam implements Comparable<Exam>{
  String name;
  int score;

  public Exam(String name, int score) {
    this.name = name;
    this.score = score;
  }

  public int compareTo(Exam other) {
    if (score != other.score) {
      return score - other.score;
    } else {
      return name.compareTo(other.name);
    }
  }

  public String toString() {
    return "Name: " + name + " | score: " + score;
  }
}

class BankAccount {
  String name;
  int balance;

  public BankAccount(String name, int balance) {
    this.name = name;
    this.balance = balance;
  }

  public String toString() {
    return "Name: " + name + " | balance: " + balance;
  }
}