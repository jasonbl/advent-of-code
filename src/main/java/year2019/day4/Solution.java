package year2019.day4;

import java.util.function.Function;

public class Solution {

  public static void main(String[] args) {
    System.out.println(countValidPasswords(130254, 678275, Solution::isValidPartOne));
    System.out.println(countValidPasswords(130254, 678275, Solution::isValidPartTwo));
  }

  private static int countValidPasswords(int start, int end, Function<Integer, Boolean> validationStrategy) {
    int count = 0;
    for (int i = start; i <= end; i++) {
      if (validationStrategy.apply(i)) {
        count++;
      }
    }

    return count;
  }

  private static boolean isValidPartOne(int passwordInt) {
    boolean hasDouble = false;
    String password = passwordInt + "";
    int previousDigit = -1;
    for (int i = 0; i < password.length(); i++) {
      int currentDigit = Integer.parseInt(password.charAt(i) + "");
      if (currentDigit < previousDigit) {
        return false;
      } else if (currentDigit == previousDigit) {
        hasDouble = true;
      }

      previousDigit = currentDigit;
    }

    return hasDouble;
  }

  private static boolean isValidPartTwo(int passwordInt) {
    boolean hasDouble = false;
    String password = passwordInt + "";
    int previousDigit = -1;
    int copies = 1;
    for (int i = 0; i < password.length(); i++) {
      int currentDigit = Integer.parseInt(password.charAt(i) + "");
      if (currentDigit < previousDigit) {
        return false;
      } else if (currentDigit == previousDigit) {
        copies++;
      } else {
        if (copies == 2) {
          hasDouble = true;
        }

        copies = 1;
      }

      previousDigit = currentDigit;
    }

    return copies == 2 || hasDouble;
  }

}
