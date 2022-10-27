package year2021.day24;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ALU {

  private final String[] program;

  private final Variable w, x, y, z;

  private final Map<String, Variable> variables;

  private int nextInput;

  public ALU(String[] program) {
    this.program = program;
    this.w = new Variable("w");
    this.x = new Variable("x");
    this.y = new Variable("y");
    this.z = new Variable("z");
    this.variables = Stream.of(w, x, y, z)
        .collect(Collectors.toMap((variable) -> variable.name, Function.identity()));
  }

  public int getW() {
    return w.value;
  }

  public int getX() {
    return x.value;
  }

  public int getY() {
    return y.value;
  }

  public int getZ() {
    return z.value;
  }

  public void run(String input) {
    for (String instruction : program) {
      String[] parts = instruction.split(" ");
      Variable var1 = variables.get(parts[1]);
      int val2 = 0;
      if (parts.length == 3) {
        if (variables.containsKey(parts[2])) {
          val2 = variables.get(parts[2]).value;
        } else {
          val2 = Integer.parseInt(parts[2]);
        }
      }

      switch (parts[0]) {
        case "inp":
          variables.get(parts[1]).value = loadInput(input, nextInput++);
          break;
        case "add":
          var1.value += val2;
          break;
        case "mul":
          var1.value *= val2;
          break;
        case "div":
          var1.value /= val2;
          break;
        case "mod":
          var1.value %= val2;
          break;
        case "eql":
          var1.value = var1.value == val2 ? 1 : 0;
          break;
        default:
          throw new IllegalArgumentException("Invalid instruction");
      }

      // printState();
    }
  }

  private void printState() {
    System.out.println("w=" + w.value + " x=" + x.value + " y=" + y.value + " z=" + z.value);
  }

  private static int loadInput(String input, int index) {
    return Integer.parseInt(input.charAt(index) + "");
  }

  private static class Variable {
    private final String name;
    private int value;

    private Variable(String name) {
      this.name = name;
    }
  }

}
