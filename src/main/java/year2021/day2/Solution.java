package year2021.day2;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2021/day2/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(inputs));
    System.out.println("Part 2: " + partTwo(inputs));
  }

  private static int partOne(String[] inputs) {
    int position = 0;
    int depth = 0;
    for (String input : inputs) {
      String[] instruction = input.split(" ");
      int distance = Integer.parseInt(instruction[1]);
      switch (instruction[0]) {
        case "forward":
          position += distance;
          break;
        case "down":
          depth += distance;
          break;
        case "up":
          depth -= distance;
          break;
      }
    }

    return position * depth;
  }

  private static int partTwo(String[] inputs) {
    int position = 0;
    int depth = 0;
    int aim = 0;
    for (String input : inputs) {
      String[] instruction = input.split(" ");
      int distance = Integer.parseInt(instruction[1]);
      switch (instruction[0]) {
        case "forward":
          position += distance;
          depth += aim * distance;
          break;
        case "down":
          aim += distance;
          break;
        case "up":
          aim -= distance;
          break;
      }
    }

    return position * depth;
  }

}
