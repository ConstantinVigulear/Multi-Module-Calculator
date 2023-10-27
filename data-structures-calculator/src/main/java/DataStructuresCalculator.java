/*
Task:
Use the stack to evaluate arithmetic expressions.
The program will be tested with compound expressions with multiple operators and parentheses.
For simplicity assume that the operands are integers, and the operators are of four types: +, -, *, /.
Catch all the errors that you can find.

input: (11 + 18) * 20 - 2
output: 578
 */

import java.util.*;

public final class DataStructuresCalculator {

  private DataStructuresCalculator() {}

  //  public static void main(String[] args) {
  //    String expression = "(11 + 18) * 20 - 2";
  //    System.out.println(parse(expression));
  //  }

  public static int evaluate(String expression) {

    Stack<Integer> integers = new Stack<>();
    Stack<Character> operators = new Stack<>();
    char[] tokens = expression.toCharArray();

    for (int i = 0; i < tokens.length; i++) {

      // Skip whitespace
      if (tokens[i] == ' ') continue;

      // Put numbers to integers
      if (Character.isDigit(tokens[i])) {
        StringBuilder sb = new StringBuilder();

        // Check the length of the number (it may consist of many digits)
        while (i < tokens.length && Character.isDigit(tokens[i])) sb.append(tokens[i++]);
        integers.push(Integer.parseInt(sb.toString()));

        // When next symbol is not a digit, stop checking
        // i is one position ahead due to tokens[i++] inside a for-loop
        // decrease the value of i by 1 to correct the offset
        // this symbol will be checked in for-loop
        i--;
      }

      // Put opening brace into operators
      else if (tokens[i] == '(') operators.push(tokens[i]);

      // Closing brace encountered -> solve entire brace
      else if (tokens[i] == ')') {

          // pick from operators until encounter "(" -> then calculate expression inside brackets
          while (!operators.empty() && operators.peek() != '(')
            integers.push(applyOperation(operators.pop(), integers.pop(), integers.pop()));
          // when "(" encountered -> remove it from stack
        if (!operators.contains('(')) {
          throw new UnsupportedOperationException("There should be an opening brace '('!");
        } else operators.pop();
      }

      // Current token is an operator.
      else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {

        // Skip if expression starts with * or / else insert 0 into integers
        if (integers.isEmpty() || (!operators.empty() && !integers.empty() && operators.size() != integers.size())) {
          if ((tokens[i] == '+') || (tokens[i] == '-')) {
            integers.push(0);
            operators.push(tokens[i]);
          }
          continue;
        }

        // If stack operator is superior or equal to current -> apply operation and add result to
        // integers
        // else push to operators
        while (!operators.empty() && hasPrecedenceStackOverCurrent(operators.peek(), tokens[i]))
          // if current operator is superior apply operation with that operator
          integers.push(applyOperation(operators.pop(), integers.pop(), integers.pop()));

        // Push current token to 'operators'.
        operators.push(tokens[i]);
      }
    }

    // Entire expression has been parsed at this point, apply remaining operators to remaining
    // values

    if (operators.contains('(')) throw new UnsupportedOperationException("There should be a closing brace ')'!");

    else {
      while (!operators.empty())
        integers.push(applyOperation(operators.pop(), integers.pop(), integers.pop()));

    }
    // Top of 'values' contains result, return it
    return integers.pop();
  }

  // Return true if 'opFromStack' has higher or same precedence as 'opCurrent', otherwise return
  // false.
  public static boolean hasPrecedenceStackOverCurrent(char opFromStack, char opCurrent) {
    if (opFromStack == '(' || opFromStack == ')') return false;
    return (opCurrent != '*' && opCurrent != '/') || (opFromStack != '+' && opFromStack != '-');
  }

  public static int applyOperation(char op, int two, int one) {
    return switch (op) {
      case '+' -> one + two;
      case '-' -> one - two;
      case '*' -> one * two;
      case '/' -> {
        if (two == 0) throw new UnsupportedOperationException("Cannot divide by zero");
        yield one / two;
      }
      default -> 0;
    };
  }
}
