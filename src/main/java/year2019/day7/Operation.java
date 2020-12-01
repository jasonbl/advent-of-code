package year2019.day7;

public enum Operation {

  ADD(1, 3),
  MULTIPLY(2, 3),
  INPUT(3, 1),
  OUTPUT(4, 1),
  JUMP_IF_TRUE(5, 2),
  JUMP_IF_FALSE(6, 2),
  LESS_THAN(7, 3),
  EQUALS(8, 3),
  HALT(99, 0);

  private final int opcode;

  private final int numParameters;

  Operation(int opcode, int numParameters) {
    this.opcode = opcode;
    this.numParameters = numParameters;
  }

  public int getOpcode() {
    return opcode;
  }

  public int getNumParameters() {
    return numParameters;
  }

  public static Operation valueOf(int opcode) {
    for (Operation operation : Operation.values()) {
      if (operation.getOpcode() == opcode) {
        return operation;
      }
    }

    throw new IllegalArgumentException("Unknown opcode: " + opcode);
  }

}
