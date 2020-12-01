package year2019.day2;

import util.InputLoader;

public class Part1 {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2019/day2/Input.txt")
        .split(",");

    int[] memory = new int[inputs.length];
    for (int i = 0; i < inputs.length; i++) {
      memory[i] = Integer.parseInt(inputs[i]);
    }

    System.out.println(solve(memory));
  }

  private static int solve(int[] memory) {
    memory[1] = 12;
    memory[2] = 2;

    for (int i = 0; i < memory.length && memory[i] != 99;) {
      int opcode = memory[i++];
      int inputOnePos = memory[i++];
      int inputTwoPos = memory[i++];
      int outputPos = memory[i++];

      int outputValue;
      if (opcode == 1) {
        outputValue = memory[inputOnePos] + memory[inputTwoPos];
      } else {
        outputValue = memory[inputOnePos] * memory[inputTwoPos];
      }

      memory[outputPos] = outputValue;
    }

    return memory[0];
  }

}
