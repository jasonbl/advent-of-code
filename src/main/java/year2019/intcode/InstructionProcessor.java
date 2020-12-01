package year2019.intcode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.toIntExact;

public class InstructionProcessor {

  private static final int MEMORY_SIZE = 10_000;

  private final long[] memory;

  private final ExecutorService executorService;
  private final IORecorder inputRecorder;
  private final IORecorder outputRecorder;
  private int instructionPointer;
  private int relativeBase;
  private AtomicBoolean isHalted;

  private InstructionProcessor(long[] memory) {
    this.memory = memory;
    this.executorService = Executors.newFixedThreadPool(1);
    this.inputRecorder = new IORecorder();
    this.outputRecorder = new IORecorder();
    this.instructionPointer = 0;
    this.relativeBase = 0;
    this.isHalted = new AtomicBoolean();
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

  public List<Long> executeSynchronously(List<Long> inputs) {
    inputs.forEach(inputRecorder::record);
    Future<?> processFuture = executorService.submit(this::execute);
    try {
      processFuture.get();
    } catch (Exception e) {
      System.out.println("Error awaiting program completion: " + e.getMessage());
      throw new RuntimeException(e);
    }

    return outputRecorder.flush();
  }

  public void submitInput(Integer input) {
    inputRecorder.record(input);
  }

  public long awaitOutput() {
    return awaitValue(outputRecorder);
  }

  public List<Long> awaitNextOutputs() {
    return awaitNextOutputs(null);
  }

  public List<Long> awaitNextOutputs(Integer input) {
    if (input != null) {
      inputRecorder.record(input);
    }

    // Wait until an input has been requested or the process has halted
    while (!isHalted.get() && !inputRecorder.isIOValueRequested()) {
    }

    // Return available outputs
    return outputRecorder.flush();
  }

  public boolean isHalted() {
    return this.isHalted.get();
  }

  private void execute() {
    while (!isHalted.get()) {
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
    long input = awaitValue(inputRecorder);
    memory[writeIndex] = input;
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
    isHalted.set(true);
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
      return ioRecorder.requestIOValue().get();
    } catch (ExecutionException | InterruptedException e) {
      System.out.println("Failed to get IO value: " + e);
      throw new RuntimeException(e);
    }
  }

}
