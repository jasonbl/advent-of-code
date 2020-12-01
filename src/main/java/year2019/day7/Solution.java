package year2019.day7;

import util.InputLoader;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2019/day7/input.txt")
        .split(",");

    try {
      partOne(input);
      partTwo(input);
    } catch (Exception e) {
      System.out.println("Error encountered: " + e);
    }
  }

  private static void partOne(String[] input) {
    int[] memory = buildMemory(input);

    // There's definitely a better way to do this but I'm lazy
    int maxThrusterSignal = 0;
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        for (int k = 0; k < 5; k++) {
          for (int l = 0; l < 5; l++) {
            for (int m = 0; m < 5; m++) {
              Set<Integer> set = new HashSet<>(asList(i, j, k, l, m));
              if (set.size() != 5) {
                continue;
              }

              IntcodeComputer computer = new IntcodeComputer(5, memory);
              int thrusterSignal = computer.runAmplifierCircuit(new int[] { i, j, k, l, m });
              maxThrusterSignal = Math.max(thrusterSignal, maxThrusterSignal);
            }
          }
        }
      }
    }

    System.out.println("Solution: " + maxThrusterSignal);
  }

  private static void partTwo(String[] input) {
    int[] memory = buildMemory(input);

    // There's definitely a better way to do this but I'm lazy
    int maxThrusterSignal = 0;
    for (int i = 5; i < 10; i++) {
      for (int j = 5; j < 10; j++) {
        for (int k = 5; k < 10; k++) {
          for (int l = 5; l < 10; l++) {
            for (int m = 5; m < 10; m++) {
              Set<Integer> set = new HashSet<>(asList(i, j, k, l, m));
              if (set.size() != 5) {
                continue;
              }

              IntcodeComputer computer = new IntcodeComputer(5, memory);
              int thrusterSignal = computer.runFeedbackLoop(new int[] { i, j, k, l, m });
              maxThrusterSignal = Math.max(thrusterSignal, maxThrusterSignal);
            }
          }
        }
      }
    }

    System.out.println("Solution: " + maxThrusterSignal);
  }

  private static int[] buildMemory(String[] input) {
    int[] memory = new int[input.length];
    for (int i = 0; i < input.length; i++) {
      memory[i] = Integer.parseInt(input[i]);
    }

    return memory;
  }

}
