package year2020.day5;

import util.InputLoader;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

  public static void main(String[] args) {
    String[] boardingPasses = InputLoader.load("/year2020/day5/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(boardingPasses));
    System.out.println("Part 2: " + partTwo(boardingPasses));
  }

  private static int partOne(String[] boardingPasses) {
    return computeMaxSeatId(boardingPasses);
  }

  private static int partTwo(String[] boardingPasses) {
    Set<Integer> ids = Arrays.stream(boardingPasses)
        .map(Solution::computeSeatId)
        .collect(Collectors.toSet());

    int minSeatId = computeMinSeatId(boardingPasses);
    int maxSeatId = computeMaxSeatId(boardingPasses);
    for (int i = minSeatId; i <= maxSeatId; i++) {
      if (!ids.contains(i)) {
        return i;
      }
    }

    return -1;
  }

  private static int computeMaxSeatId(String[] boardingPasses) {
    return Arrays.stream(boardingPasses)
        .mapToInt(Solution::computeSeatId)
        .max()
        .orElse(-1);
  }

  private static int computeMinSeatId(String[] boardingPasses) {
    return Arrays.stream(boardingPasses)
        .mapToInt(Solution::computeSeatId)
        .min()
        .orElse(-1);
  }

  private static int computeSeatId(String boardingPass) {
    int low = 0;
    int high = 127;
    int rowsRemaining = 128;
    int row = -1;
    for (int i = 0; i < 7; i++) {
      rowsRemaining /= 2;
      if (boardingPass.charAt(i) == 'F') {
        high -= rowsRemaining;
        row = low;
      } else {
        low += rowsRemaining;
        row = high;
      }
    }

    low = 0;
    high = 7;
    int column = -1;
    int colsRemaining = 8;
    for (int i = 7; i < 10; i++) {
      colsRemaining /= 2;
      if (boardingPass.charAt(i) == 'L') {
        high -= colsRemaining;
        column = high;
      } else {
        low += colsRemaining;
        column = low;
      }
    }

    return 8 * row + column;
  }

}
