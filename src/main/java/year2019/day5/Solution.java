package year2019.day5;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2019/day5/input.txt")
        .split(",");

    int[] memory = buildMemory(input);
    IntcodeComputer computer = new IntcodeComputer(memory);
    computer.runDiagnosticTest(5);
  }

  private static int[] buildMemory(String[] input) {
    int[] memory = new int[input.length];
    for (int i = 0; i < input.length; i++) {
      memory[i] = Integer.parseInt(input[i]);
    }

    return memory;
  }

}
