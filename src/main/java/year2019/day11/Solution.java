package year2019.day11;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2019/day11/input.txt")
        .split(",");

    long[] program = buildProgram(input);
    HullPainter painter = new HullPainter(program);
    System.out.println(painter.paint());
  }

  private static long[] buildProgram(String[] input) {
    long[] program = new long[input.length];
    for (int i = 0; i < input.length; i++) {
      program[i] = Long.parseLong(input[i]);
    }

    return program;
  }

}
