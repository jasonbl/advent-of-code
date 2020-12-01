package year2019.day9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.toIntExact;

public class InstructionProcessor {

  private static final int MEMORY_SIZE = 10_000;

  private final long[] memory;

  private final ExecutorService executorService;
  private final IORecorder inputRecorder;
  private final IORecorder outputRecorder;
  private int instructionPointer;
  private int relativeBase;
  private boolean isHalted;

  private InstructionProcessor(long[] memory) {
    this.memory = memory;
    this.executorService = Executors.newFixedThreadPool(1);
    this.inputRecorder = new IORecorder();
    this.outputRecorder = new IORecorder();
    this.instructionPointer = 0;
    this.relativeBase = 0;
    this.isHalted = false;
  }

  public static InstructionProcessor loadProgram(long[] program) {
    long[] memory = new long[MEMORY_SIZE];
    for (int i = 0; i < program.length; i++) {
      memory[i] = program[i];
    }

    return new InstructionProcessor(memory);
  }

  public Future<?> start() {
    return executorService.submit(this::execute);
  }

  public List<Long> startSynchronously(List<Long> inputs) {
    inputs.forEach(inputRecorder::record);
    Future<?> processFuture = executorService.submit(this::execute);
    try {
      processFuture.get();
    } catch (Exception e) {
      System.out.println("Error awaiting program completion: " + e.getMessage());
      throw new RuntimeException(e);
    }

    List<Long> outputs = new ArrayList<>();
    Future<Long> nextOutput;
    while ((nextOutput = outputRecorder.requestIOValue()).isDone()) {
      try {
        outputs.add(nextOutput.get());
      } catch (Exception e) {
        // Should never reach this
      }
    }

    return outputs;
  }

  public void submitInput(Integer input) {
    inputRecorder.record(input);
  }

  public long awaitOutput() {
    return awaitValue(outputRecorder);
  }

  private void execute() {
    while (!isHalted) {
      Instruction instruction = Instruction.buildInstruction(toIntExact(memory[instructionPointer]));
      Operation operation = instruction.getOperation();
      switch (instruction.getOperation()) {
        case ADD:
          add(instruction.getParameterModes());
          break;
        case MULTIPLY:
          multiply(instruction.getParameterModes());
          break;
        case INPUT:
          input(instruction.getParameterModes());
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
        case RELATIVE_BASE_OFFSET:
          relativeBaseOffset(instruction.getParameterModes());
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
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    long secondParam = getReadParamInMode(1, parameterModes[1]);
    int writeIndex = getWriteIndexInMode(2, parameterModes[2]);

    memory[writeIndex] = firstParam + secondParam;
    instructionPointer += 4;
  }

  private void multiply(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    long secondParam = getReadParamInMode(1, parameterModes[1]);
    int writeIndex = getWriteIndexInMode(2, parameterModes[2]);

    memory[writeIndex] = firstParam * secondParam;
    instructionPointer += 4;
  }

  private void input(ParameterMode[] parameterModes) {
    int writeIndex = getWriteIndexInMode(0, parameterModes[0]);
    memory[writeIndex] = awaitValue(inputRecorder);
    instructionPointer += 2;
  }

  private void output(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    outputRecorder.record(firstParam);
    instructionPointer += 2;
  }

  private void jumpIfTrue(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    long secondParam = getReadParamInMode(1, parameterModes[1]);
    if (firstParam != 0) {
      instructionPointer = toIntExact(secondParam);
    } else {
      instructionPointer += 3;
    }
  }

  private void jumpIfFalse(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    long secondParam = getReadParamInMode(1, parameterModes[1]);
    if (firstParam == 0) {
      instructionPointer = toIntExact(secondParam);
    } else {
      instructionPointer += 3;
    }
  }

  private void lessThan(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    long secondParam = getReadParamInMode(1, parameterModes[1]);
    int writeIndex = getWriteIndexInMode(2, parameterModes[2]);
    if (firstParam < secondParam) {
      memory[writeIndex] = 1;
    } else {
      memory[writeIndex] = 0;
    }

    instructionPointer += 4;
  }

  private void equals(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    long secondParam = getReadParamInMode(1, parameterModes[1]);
    int writeIndex = getWriteIndexInMode(2, parameterModes[2]);
    if (firstParam == secondParam) {
      memory[writeIndex] = 1;
    } else {
      memory[writeIndex] = 0;
    }

    instructionPointer += 4;
  }

  private void relativeBaseOffset(ParameterMode[] parameterModes) {
    long firstParam = getReadParamInMode(0, parameterModes[0]);
    relativeBase += firstParam;
    instructionPointer += 2;
  }

  private void halt() {
    isHalted = true;
    executorService.shutdown();
  }

  private long getReadParamInMode(int paramIndex, ParameterMode parameterMode) {
    long param = getParam(paramIndex);
    switch (parameterMode) {
      case POSITION:
        return memory[toIntExact(param)];
      case IMMEDIATE:
        return param;
      case RELATIVE_MODE:
        return memory[relativeBase + toIntExact(param)];
      default:
        throw new IllegalArgumentException("Unexpected parameter mode: " + parameterMode.name());
    }
  }

  private int getWriteIndexInMode(int paramIndex, ParameterMode parameterMode) {
    long param = getParam(paramIndex);
    switch (parameterMode) {
      case POSITION:
        return toIntExact(param);
      case RELATIVE_MODE:
        return relativeBase + toIntExact(param);
      case IMMEDIATE:
      default:
        throw new IllegalArgumentException("Unexpected parameter mode: " + parameterMode.name());
    }
  }

  private long getParam(int paramIndex) {
    return memory[instructionPointer + paramIndex + 1];
  }

  private static long awaitValue(IORecorder ioRecorder) {
    try {
      return ioRecorder.requestIOValue().get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      System.out.println("Failed to get IO value: " + e);
      throw new RuntimeException(e);
    }
  }

}
