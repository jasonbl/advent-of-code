package year2020.day18;

import util.InputLoader;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern OP_PATTERN = Pattern.compile("(\\d+) ([+*]) (\\d+)");
  private static final Pattern LONE_VALUE_PATTERN = Pattern.compile("\\((\\d+)\\)");

  public static void main(String[] args) {
    String[] problems = InputLoader.load("/year2020/day18/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(problems));
  }

  private static long partOne(String[] problems) {
    return Arrays.stream(problems)
        .mapToLong(Solution::solvePartOneProblem)
        .sum();
  }

  private static long solvePartOneProblem(String problem) {
    return Long.parseLong(collapsePartOneProblem(problem));
  }

  private static String collapsePartOneProblem(String problem) {
    Matcher matcher = OP_PATTERN.matcher(problem);
    while (matcher.find()) {
      long leftVal = Long.parseLong(matcher.group(1));
      String op = matcher.group(2);
      long rightVal = Long.parseLong(matcher.group(3));
      long result;
      if (op.equals("+")) {
        result = leftVal + rightVal;
      } else {
        result = leftVal * rightVal;
      }

      problem = matcher.replaceFirst(result + "");

      matcher = LONE_VALUE_PATTERN.matcher(problem);
      if (matcher.find()) {
        long loneValue = Long.parseLong(matcher.group(1));
        problem = matcher.replaceFirst(loneValue + "");
      }

      matcher = OP_PATTERN.matcher(problem);
    }

    return problem;
  }

}
