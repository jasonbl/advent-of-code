package year2019.day11;

public class Instruction {

  private final Operation operation;

  private final ParameterMode[] parameterModes;

  private Instruction(Operation operation, ParameterMode[] parameterModes) {
    this.operation = operation;
    this.parameterModes = parameterModes;
  }

  public Operation getOperation() {
    return operation;
  }

  public ParameterMode[] getParameterModes() {
    return parameterModes;
  }

  public static Instruction buildInstruction(int instruction) {
    Operation operation = Operation.valueOf(instruction % 100);
    instruction /= 100;

    ParameterMode[] parameterModes = new ParameterMode[operation.getNumParameters()];
    for (int i = 0; i < parameterModes.length; i++) {
      parameterModes[i] = ParameterMode.valueOf(instruction % 10);
      instruction /= 10;
    }

    return new Instruction(operation, parameterModes);
  }

}
