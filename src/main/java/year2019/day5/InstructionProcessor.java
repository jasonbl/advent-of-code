package year2019.day5;

public class InstructionProcessor {

  private final int[] memory;
  private int instructionPointer;

  public InstructionProcessor(int[] memory) {
    this.memory = memory;
    this.instructionPointer = 0;
  }

  public void process(int input) {
    while (instructionPointer < memory.length) {
      Instruction instruction = Instruction.buildInstruction(memory[instructionPointer]);
      Operation operation = instruction.getOperation();
      switch (instruction.getOperation()) {
        case ADD:
          add(instruction.getParameterModes());
          break;
        case MULTIPLY:
          multiply(instruction.getParameterModes());
          break;
        case INPUT:
          input(input);
          break;
        case OUTPUT:
          output(instruction.getParameterModes());
          break;
        case JUMP_IF_TRUE:
          jumpIfTrue(instruction.getParameterModes());
          break;
        case JUMP_IF_FALSE:
          jumpIfFalse(instruction.getParameterModes());
          break;
        case LESS_THAN:
          lessThan(instruction.getParameterModes());
          break;
        case EQUALS:
          equals(instruction.getParameterModes());
          break;
        case HALT:
          return;
        default:
          throw new IllegalArgumentException("Unexpected operation: " + operation.name());
      }
    }
  }

  private void add(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    int secondParam = getParamInMode(1, parameterModes[1]);
    int thirdParam = getParam(2);

    memory[thirdParam] = firstParam + secondParam;
    instructionPointer += 4;
  }

  private void multiply(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    int secondParam = getParamInMode(1, parameterModes[1]);
    int thirdParam = getParam(2);

    memory[thirdParam] = firstParam * secondParam;
    instructionPointer += 4;
  }

  private void input(int input) {
    int firstParam = getParam(0);
    memory[firstParam] = input;
    instructionPointer += 2;
  }

  private void output(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    System.out.println("Output: " + firstParam);
    instructionPointer += 2;
  }

  private void jumpIfTrue(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    int secondParam = getParamInMode(1, parameterModes[1]);
    if (firstParam != 0) {
      instructionPointer = secondParam;
    } else {
      instructionPointer += 3;
    }
  }

  private void jumpIfFalse(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    int secondParam = getParamInMode(1, parameterModes[1]);
    if (firstParam == 0) {
      instructionPointer = secondParam;
    } else {
      instructionPointer += 3;
    }
  }

  private void lessThan(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    int secondParam = getParamInMode(1, parameterModes[1]);
    int thirdParam = getParam(2);
    if (firstParam < secondParam) {
      memory[thirdParam] = 1;
    } else {
      memory[thirdParam] = 0;
    }

    instructionPointer += 4;
  }

  private void equals(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    int secondParam = getParamInMode(1, parameterModes[1]);
    int thirdParam = getParam(2);
    if (firstParam == secondParam) {
      memory[thirdParam] = 1;
    } else {
      memory[thirdParam] = 0;
    }

    instructionPointer += 4;
  }

  private int getParamInMode(int paramIndex, ParameterMode parameterMode) {
    int param = getParam(paramIndex);
    switch (parameterMode) {
      case POSITION:
        return memory[param];
      case IMMEDIATE:
        return param;
      default:
        throw new IllegalArgumentException("Unexpected parameter mode: " + parameterMode.name());
    }
  }

  private int getParam(int paramIndex) {
    return memory[instructionPointer + paramIndex + 1];
  }

}
