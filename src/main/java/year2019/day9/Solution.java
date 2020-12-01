package year2019.day9;

import util.InputLoader;

import java.util.List;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2019/day9/input.txt")
        .split(",");

    long[] program = buildProgram(input);
    IntcodeComputer computer = new IntcodeComputer();
    solve(computer, program, 1);
    solve(computer, program, 2);
  }

  private static void solve(IntcodeComputer computer, long[] program, int input) {
    List<Long> outputs = computer.runBoostProgram(program, input);
    for (Long output : outputs) {
      System.out.println(output);
    }
  }

  private static long[] buildProgram(String[] input) {
    long[] program = new long[input.length];
    for (int i = 0; i < input.length; i++) {
      program[i] = Long.parseLong(input[i]);
    }

    return program;
  }

}
