package year2021.day7;

import util.InputLoader;

import java.util.Arrays;

public class Solution {

  public static void main(String[] args) {
    int[] input = Arrays.stream(InputLoader.load("/year2021/day7/input.txt").split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (int val : input) {
      if (val < min) {
        min = val;
      }

      if (val > max) {
        max = val;
      }
    }

    int minFuelPartOne = Integer.MAX_VALUE;
    int minFuelPartTwo = Integer.MAX_VALUE;
    for (int i = min; i <= max; i++) {
      int fuelPartOne = computeFuelPartOne(input, i);
      if (fuelPartOne < minFuelPartOne) {
        minFuelPartOne = fuelPartOne;
      }

      int fuelPartTwo = computeFuelPartTwo(input, i);
      if (fuelPartTwo < minFuelPartTwo) {
        minFuelPartTwo = fuelPartTwo;
      }
    }

    System.out.println("Part 1: " + minFuelPartOne);
    System.out.println("Part 2: " + minFuelPartTwo);
  }

  private static int computeFuelPartOne(int[] input, int position) {
    int fuel = 0;
    for (int val : input) {
      fuel += Math.abs(position - val);
    }

    return fuel;
  }

  private static int computeFuelPartTwo(int[] input, int position) {
    int fuel = 0;
    for (int val : input) {
      int diff = Math.abs(position - val);
      fuel += (diff * (diff + 1)) / 2;
    }

    return fuel;
  }

}
