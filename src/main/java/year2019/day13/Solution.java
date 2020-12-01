package year2019.day13;

import util.InputLoader;
import year2019.intcode.InstructionProcessor;

import java.util.Collections;
import java.util.List;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2019/day13/input.txt")
        .split(",");

    long[] program = buildProgram(input);
    System.out.println("Part one: " + partOne(program));
    System.out.println("Part two: " + partTwo(program));
  }

  private static long partTwo(long[] program) {
    program[0] = 2;
    return Game.init(program)
        .play(false);
  }

  private static int partOne(long[] program) {
    List<Long> outputs = InstructionProcessor.loadProgram(program)
        .executeSynchronously(Collections.emptyList());

    int blockCount = 0;
    int index = 0;
    for (Long output : outputs) {
      if (index % 3 == 2 && output == 2) {
        blockCount++;
      }

      index++;
    }

    return blockCount;
  }

  private static long[] buildProgram(String[] input) {
    long[] program = new long[input.length];
    for (int i = 0; i < input.length; i++) {
      program[i] = Long.parseLong(input[i]);
    }

    return program;
  }

}
