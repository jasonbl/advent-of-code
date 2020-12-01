package year2020.day1;

import util.InputLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution {

  public static void main(String[] args) {
    int[] inputs = Arrays.stream(InputLoader.load("/year2020/day1/input.txt").split("\n"))
        .mapToInt(Integer::parseInt)
        .toArray();

    int target = 2020;
    System.out.println("Part 1: " + twoSum(inputs, target));
    System.out.println("Part 2: " + threeSum(inputs, target));
  }

  private static int twoSum(int[] inputs, int target) {
    Set<Integer> set = new HashSet<>();
    for (int val : inputs) {
      if (set.contains(target - val)) {
        return val * (target - val);
      }

      set.add(val);
    }

    return -1;
  }

  private static int threeSum(int[] inputs, int target) {
    for (int i = 0; i < inputs.length; i++) {
      int val = inputs[i];
      int[] restOfInputs = Arrays.copyOfRange(inputs, i + 1, inputs.length);

      int twoSumResult = twoSum(restOfInputs, target - val);
      if (twoSumResult != -1) {
        return val * twoSumResult;
      }
    }

    return -1;
  }

}
