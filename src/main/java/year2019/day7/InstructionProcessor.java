package year2019.day7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class InstructionProcessor {

  private final int[] memory;

  private final ExecutorService executorService;
  private final IORecorder inputRecorder;
  private final IORecorder outputRecorder;
  private int instructionPointer;
  private boolean isHalted;

  public InstructionProcessor(int[] memory) {
    this.memory = memory;
    this.executorService = Executors.newFixedThreadPool(1);
    this.inputRecorder = new IORecorder();
    this.outputRecorder = new IORecorder();
    this.instructionPointer = 0;
    this.isHalted = false;
  }

  public Future<?> start() {
    return executorService.submit(this::execute);
  }

  public void submitInput(Integer input) {
    inputRecorder.record(input);
  }

  public int awaitOutput() {
    return awaitValue(outputRecorder);
  }

  private void execute() {
    while (!isHalted) {
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
          input();
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
          halt();
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

  private void input() {
    int firstParam = getParam(0);
    memory[firstParam] = awaitValue(inputRecorder);
    instructionPointer += 2;
  }

  private void output(ParameterMode[] parameterModes) {
    int firstParam = getParamInMode(0, parameterModes[0]);
    outputRecorder.record(firstParam);
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

  private void halt() {
    isHalted = true;
    executorService.shutdown();
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

  private static int awaitValue(IORecorder ioRecorder) {
    try {
      return ioRecorder.requestIOValue().get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      System.out.println("Failed to get IO value: " + e);
      throw new RuntimeException(e);
    }
  }

}
