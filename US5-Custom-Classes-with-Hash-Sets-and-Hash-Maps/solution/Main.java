import java.util.*;

class Main {
  public static void main(String[] args) {
    Student s1 = new Student("Frank", "Ocean", 10);
    Student s2 = new Student("Janelle", "Monae", 12);
    Student s3 = new Student("Hayley", "Kiyoko", 8);
    Student s4 = new Student("Kehlani", "Parrish", 9);
    Student s5 = new Student("Melissa", "Jefferson", 11);
    Student s6 = new Student("Hayley", "Kiyoko", 8);
    Student s7 = new Student("Hayley", "Williams", 8);

    HashSet<Student> setStudents = new HashSet<Student>();
    setStudents.add(s1);
    setStudents.add(s2);
    setStudents.add(s3);
    setStudents.add(s4);
    setStudents.add(s5);
    // Because .equals() will return true when comparing s6 and s3, this will be a no-op
    setStudents.add(s6);
    System.out.println(setStudents);

    HashMap<Student, String> studentToSchoolMap = new HashMap<Student, String>();
    studentToSchoolMap.put(s1, "Channel Orange School");
    studentToSchoolMap.put(s2, "Pink School");
    studentToSchoolMap.put(s3, "The Curious School");
    // .equals() will return true when comparing s7 and s3, so the s7-"Airplane School" key-value pair will replace the s3-"The Curious School" pair
    studentToSchoolMap.put(s7, "Airplane School");
    System.out.println(studentToSchoolMap);
    
    studentToSchoolMap.put(s4, "Distraction School");
    studentToSchoolMap.put(s5, "Truth Hurts School");
    // Similarly, because .equals() will return true when comparing s6 and s7, they are treated as the same key, and this will replace the s7-"Airplane School" key-value pair;
    studentToSchoolMap.put(s6, "I Wish School");
    System.out.println(studentToSchoolMap);
  }
}

class Student {
  String firstName;
  String lastName;
  int grade;

  public Student(String firstName, String lastName, int grade) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }

  @Override
  public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof Student)) {
          return false;
      }

      Student other = (Student) o;
      return firstName.equals(other.firstName) &&
        // Uncomment out the line below if we want equality to be defined as having identical last names as well
        // lastName.equals(other.lastName) &&
        grade == other.grade;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(firstName, grade);

    // Comment out the above line and ucomment out the line below if we want equality to be defined as having identical last names as well
    // return Objects.hash(firstName, lastName, grade);
  }

  public String toString() {
    return "\'Name: " + firstName + " " + lastName +  " | Grade: " + grade + "\'";
  }
}