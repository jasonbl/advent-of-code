package year2021.day1;

import util.InputLoader;

import java.util.Arrays;

public class Solution {

  public static void main(String[] args) {
    int[] input = Arrays.stream(InputLoader.load("/year2021/day1/input.txt").split("\n"))
        .mapToInt(Integer::parseInt)
        .toArray();

    System.out.println("Part 1: " + partOne(input));
    System.out.println("Part 2: " + partTwo(input));
  }

  private static int partOne(int[] input) {
    return countIncreasesWithOffset(input, 1);
  }

  private static int partTwo(int[] input) {
    return countIncreasesWithOffset(input, 3);
  }

  private static int countIncreasesWithOffset(int[] input, int offset) {
    int increases = 0;
    for (int i = offset; i < input.length; i++) {
      if (input[i] > input[i - offset]) {
        increases++;
      }
    }

    return increases;
  }

}
