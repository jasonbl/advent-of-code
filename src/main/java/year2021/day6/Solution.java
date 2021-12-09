package year2021.day6;

import util.InputLoader;

import java.util.Arrays;

public class Solution {

  public static void main(String[] args) {
    int[] input = Arrays.stream(InputLoader.load("/year2021/day6/input.txt").split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    long[] initialState = buildInitialState(input);

    System.out.println("Part 1: " + simulate(initialState, 80));
    System.out.println("Part 2: " + simulate(initialState, 256));
  }

  private static long simulate(long[] initialState, int days) {
    long[] currentState = initialState;
    for (int day = 1; day <= days; day++) {
      currentState = simulateDay(currentState);
    }

    return Arrays.stream(currentState)
        .sum();
  }

  private static long[] simulateDay(long[] currentState) {
    long[] nextState = new long[9];
    for (int i = 0; i < currentState.length - 1; i++) {
      nextState[i] = currentState[i + 1];
    }

    nextState[6] += currentState[0];
    nextState[8] += currentState[0];
    return nextState;
  }

  private static long[] buildInitialState(int[] input) {
    long[] fish = new long[9];
    for (int timer : input) {
      fish[timer]++;
    }

    return fish;
  }

}
