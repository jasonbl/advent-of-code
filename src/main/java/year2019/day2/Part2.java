package year2019.day2;

import util.InputLoader;

public class Part2 {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2019/day2/input.txt")
        .split(",");

    int[] memory = new int[inputs.length];
    for (int i = 0; i < inputs.length; i++) {
      memory[i] = Integer.parseInt(inputs[i]);
    }

    System.out.println(solve(memory, 19690720));
  }

  private static int solve(int[] initialMemory, int target) {
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 100; j++) {
        int[] memoryCopy = copyMemory(initialMemory);
        int solution = solve(memoryCopy, i, j);
        if (solution == target) {
          return 100 * i + j;
        }
      }
    }

    return -1;
  }

  private static int solve(int[] memory, int noun, int verb) {
    memory[1] = noun;
    memory[2] = verb;

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

  private static int[] copyMemory(int[] memory) {
    int[] copy = new int[memory.length];
    for (int i = 0; i < memory.length; i++) {
      copy[i] = memory[i];
    }

    return copy;
  }

}
