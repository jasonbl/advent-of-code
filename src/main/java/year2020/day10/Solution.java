package year2020.day10;

import util.InputLoader;

import java.util.Arrays;

public class Solution {

  private static final int[] TRIBONACCI_NUMS = { 1, 2, 4, 7 };

  public static void main(String[] args) {
    int[] inputs = Arrays.stream(InputLoader.load("/year2020/day10/input.txt").split("\n"))
        .mapToInt(Integer::parseInt)
        .toArray();

    int[] adapters = buildAndSortAdapters(inputs);
    System.out.println("Part 1: " + partOne(adapters));
    System.out.println("Part 2: " + partTwo(adapters));
    System.out.println("Part 2 differently: " + partTwoDifferently(adapters));
  }

  private static int partOne(int[] adapters) {
    int oneJolt = 0;
    int threeJolt = 0;
    for (int i = 0; i < adapters.length - 1; i++) {
      if (adapters[i + 1] - adapters[i] == 1) {
        oneJolt++;
      } else if (adapters[i + 1] - adapters[i] == 3) {
        threeJolt++;
      }
    }

    return oneJolt * threeJolt;
  }

  /**
   * The number of paths to adapter n is equal to the sum of the number of paths to any
   * of the adapters within 3 jolts of that adapter.
   */
  private static long partTwo(int[] adapters) {
    long[] pathsToAdapter = new long[adapters.length];
    pathsToAdapter[0] = 1;

    for (int i = 1; i < adapters.length; i++) {
      long paths = 0;
      if (adapters[i] - adapters[i - 1] <= 3) {
        paths += pathsToAdapter[i - 1];
      }

      if (i >= 2 && adapters[i] - adapters[i - 2] <= 3) {
        paths += pathsToAdapter[i - 2];
      }

      if (i >= 3 && adapters[i] - adapters[i - 3] <= 3) {
        paths += pathsToAdapter[i - 3];
      }

      pathsToAdapter[i] = paths;
    }

    return pathsToAdapter[adapters.length - 1];
  }

  /**
   * The number of paths changes depending on the number of sequential "not required"
   * adapters. This pattern happens to follow the Tribonacci sequence. Multiplying the number
   * of possible arrangements per group of sequential "not required" adapters results in the
   * total number of possible arrangements.
   */
  private static long partTwoDifferently(int[] adapters) {
    int notRequiredCount = 0;
    long paths = 1;
    for (int i = 0; i < adapters.length; i++) {
      boolean isAdapterRequired = isAdapterRequired(adapters, i);
      if (isAdapterRequired) {
        paths *= TRIBONACCI_NUMS[notRequiredCount];
        notRequiredCount = 0;
      } else {
        notRequiredCount++;
      }
    }

    return paths;
  }

  private static boolean isAdapterRequired(int[] adapters, int index) {
    return index == 0
        || index == adapters.length - 1
        || adapters[index + 1] - adapters[index - 1] > 3;
  }

  private static int[] buildAndSortAdapters(int[] inputs) {
    Arrays.sort(inputs);

    int[] adapters = new int[inputs.length + 2];
    System.arraycopy(inputs, 0, adapters, 1, inputs.length);
    adapters[inputs.length + 1] = inputs[inputs.length - 1] + 3;

    return adapters;
  }

}
