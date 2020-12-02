package year2020.day2;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2020/day2/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(inputs));
    System.out.println("Part 2: " + partTwo(inputs));
  }

  private static int partOne(String[] inputs) {
    int validPasswords = 0;
    for (String input : inputs) {
      String[] inputParts = input.split(" ");

      String[] bounds = inputParts[0].split("-");
      int lowerBound = Integer.parseInt(bounds[0]);
      int upperBound = Integer.parseInt(bounds[1]);
      char character = inputParts[1].charAt(0);
      String password = inputParts[2];

      int characterCount = 0;
      for (int i = 0; i < password.length(); i++) {
        if (password.charAt(i) == character) {
          characterCount++;
        }
      }

      if (characterCount >= lowerBound && characterCount <= upperBound) {
        validPasswords++;
      }
    }

    return validPasswords;
  }

  private static int partTwo(String[] inputs) {
    int validPasswords = 0;
    for (String input : inputs) {
      String[] inputParts = input.split(" ");

      String[] positions = inputParts[0].split("-");
      int firstIndex = Integer.parseInt(positions[0]) - 1;
      int secondIndex = Integer.parseInt(positions[1]) - 1;
      char character = inputParts[1].charAt(0);
      String password = inputParts[2];

      if (password.charAt(firstIndex) == character) {
        if (password.charAt(secondIndex) != character) {
          validPasswords++;
        }
      } else if (password.charAt(secondIndex) == character) {
        validPasswords++;
      }
    }

    return validPasswords;
  }

}
