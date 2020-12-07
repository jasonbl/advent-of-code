package year2020.day6;

import util.InputLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

  public static void main(String[] args) {
    String[] groups = InputLoader.load("/year2020/day6/input.txt").split("\n\n");
    System.out.println("Part 1: " + partOne(groups));
    System.out.println("Part 2: " + partTwo(groups));
  }

  private static int partOne(String[] groups) {
    int sum = 0;
    for (String group : groups) {
      Set<String> yesAnswers = new HashSet<>();
      for (String person : group.split("\n")) {
        yesAnswers.addAll(computeYesAnswers(person));
      }

      sum += yesAnswers.size();
    }

    return sum;
  }

  private static int partTwo(String[] groups) {
    int sum = 0;
    for (String group : groups) {
      String[] people = group.split("\n");
      Set<String> sharedYeses = computeYesAnswers(people[0]);
      for (int i = 1; i < people.length; i++) {
        sharedYeses.retainAll(computeYesAnswers(people[i]));
      }

      sum += sharedYeses.size();
    }

    return sum;
  }

  private static Set<String> computeYesAnswers(String answers) {
    return Arrays.stream(answers.split(""))
        .collect(Collectors.toSet());
  }

}
