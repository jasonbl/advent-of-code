package year2021.day24;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] program = InputLoader.load("/year2021/day24/input.txt").split("\n");
    ALU alu = new ALU(program);
    alu.run("39494195799979");
    System.out.println(alu.getZ());

    alu = new ALU(program);
    alu.run("13161151139617");
    System.out.println(alu.getZ());
  }

}
