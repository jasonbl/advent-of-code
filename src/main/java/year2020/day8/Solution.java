package year2020.day8;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] instructions = InputLoader.load("/year2020/day8/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(instructions));
    System.out.println("Part 2: " + partTwo(instructions));
  }

  private static int partOne(String[] instructions) {
    return processInstructions(instructions, true);
  }

  private static int partTwo(String[] instructions) {
    for (int i = 0; i < instructions.length; i++) {
      String originalInstruction = instructions[i];
      if (instructions[i].startsWith("nop")) {
        instructions[i] = instructions[i].replace("nop", "jmp");
      } else if (instructions[i].startsWith("jmp")) {
        instructions[i] = instructions[i].replace("jmp", "nop");
      }

      int response = processInstructions(instructions, false);
      if (response != -1) {
        return response;
      }

      instructions[i] = originalInstruction;
    }

    return -1;
  }

  private static int processInstructions(String[] instructions, boolean isPartOne) {
    boolean[] instructionsProcessed = new boolean[instructions.length];
    int accumulator = 0;
    int index = 0;
    while (index < instructions.length && !instructionsProcessed[index]) {
      instructionsProcessed[index] = true;
      String[] halves = instructions[index].split(" ");
      String op = halves[0];
      int val = Integer.parseInt(halves[1].substring(1));
      if (halves[1].charAt(0) == '-') {
        val *= -1;
      }

      switch (op) {
        case "acc":
          accumulator += val;
          index++;
          break;
        case "jmp":
          index += val;
          break;
        case "nop":
          index++;
          break;
      }
    }

    if (index >= instructions.length || isPartOne) {
      return accumulator;
    }

    return -1;
  }

}
