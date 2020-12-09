package year2020.day9;

import util.InputLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution {

  private static final int PREAMBLE_LENGTH = 25;

  public static void main(String[] args) {
    long[] values = Arrays.stream(InputLoader.load("/year2020/day9/input.txt").split("\n"))
        .mapToLong(Long::parseLong)
        .toArray();

    long partOneAnswer = partOne(values);
    System.out.println("Part 1: " + partOneAnswer);
    System.out.println("Part 2: " + partTwo(values, partOneAnswer));
  }

  private static long partOne(long[] values) {
    for (int i = PREAMBLE_LENGTH; i < values.length; i++) {
      if (!hasTwoSum(values, i - PREAMBLE_LENGTH, i, values[i])) {
        return values[i];
      }
    }

    return -1;
  }

  private static long partTwo(long[] values, long target) {
    int startIndex = 0;
    int endIndex = 1;
    long sum = values[startIndex] + values[endIndex];
    while (sum != target) {
      if (sum < target) {
        sum += values[++endIndex];
      } else {
        sum -= values[startIndex++];
      }
    }

    long min = Long.MAX_VALUE;
    long max = -1;
    for (int i = startIndex; i <= endIndex; i++) {
      min = Math.min(min, values[i]);
      max = Math.max(max, values[i]);
    }

    return min + max;
  }

  private static boolean hasTwoSum(long[] values, int startIndex, int endIndex, long target) {
    Set<Long> set = new HashSet<>();
    for (int i = startIndex; i < endIndex; i++) {
      long currentVal = values[i];
      if (set.contains(target - currentVal) && target != 2 * currentVal) {
        return true;
      }

      set.add(currentVal);
    }

    return false;
  }

}
