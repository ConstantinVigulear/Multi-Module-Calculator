import java.util.Scanner;

public class TestClass {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter expression: ");
//    String expression = args[0];
    String expression = scanner.nextLine();
    System.out.println("Result: " + DataStructuresCalculator.evaluate(expression));
  }
}
