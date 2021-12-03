package year2021.day3;

import util.InputLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

  public static void main(String[] args) {
    List<String> inputs = Arrays.stream(InputLoader.load("/year2021/day3/input.txt").split("\n"))
            .collect(Collectors.toList());

    System.out.println("Part 1: " + partOne(inputs));
    System.out.println("Part 2: " + partTwo(inputs));
  }

  private static int partOne(List<String> inputs) {
    StringBuilder gamma = new StringBuilder();
    StringBuilder epsilon = new StringBuilder();
    for (int i = 0; i < inputs.get(0).length(); i++) {
      if (getMostCommonBit(inputs, i) == 0) {
        gamma.append('0');
        epsilon.append('1');
      } else {
        gamma.append('1');
        epsilon.append('0');
      }
    }

    return toDecimal(gamma.toString()) * toDecimal(epsilon.toString());
  }

  private static int partTwo(List<String> inputs) {
    return partTwoHelper(inputs, false) * partTwoHelper(inputs, true);
  }

  private static int partTwoHelper(List<String> inputs, boolean strategy) {
    for (int i = 0; i < inputs.get(0).length(); i++) {
      inputs = filterInputs(inputs, i, strategy);
      if (inputs.size() == 1) {
        return toDecimal(inputs.get(0));
      }
    }

    return -1;
  }

  private static int getMostCommonBit(List<String> inputs, int index) {
    int numZeros = 0;
    int numOnes = 0;
    for (String input : inputs) {
      if (input.charAt(index) == '0') {
        numZeros++;
      } else {
        numOnes++;
      }
    }

    return numZeros > numOnes ? 0 : 1;
  }

  private static List<String> filterInputs(List<String> inputs, int index, boolean matching) {
    if (getMostCommonBit(inputs, index) == 0) {
      char filterChar = matching ? '0' : '1';
      return inputs.stream()
          .filter((input) -> input.charAt(index) == filterChar)
          .collect(Collectors.toList());
    } else {
      char filterChar = matching ? '1' : '0';
      return inputs.stream()
          .filter((input) -> input.charAt(index) == filterChar)
          .collect(Collectors.toList());
    }
  }

  private static int toDecimal(String binary) {
    return Integer.parseInt(binary, 2);
  }

}
