package year2019.day7;

import java.util.concurrent.Future;

public class IntcodeComputer {

  private final int[] initialMemory;

  private final InstructionProcessor[] processors;

  public IntcodeComputer(int numCores, int[] initialMemory) {
    this.initialMemory = initialMemory;
    this.processors = new InstructionProcessor[numCores];
    resetAllProcessors();
  }

  public int runAmplifierCircuit(int[] phaseSettings) {
    restartAllProcessors();

    for (int i = 0; i < processors.length; i++) {
      processors[i].submitInput(phaseSettings[i]);
    }

    int input = 0;
    for (int i = 0; i < processors.length; i++) {
      processors[i].submitInput(input);
      input = processors[i].awaitOutput();
    }

    return input;
  }

  public int runFeedbackLoop(int[] phaseSettings) {
    Future<?>[] processorFutures = restartAllProcessors();

    for (int i = 0; i < processors.length; i++) {
      processors[i].submitInput(phaseSettings[i]);
    }

    int input = 0;
    for (int i = 0; i < processors.length; i = (i + 1) % processors.length) {
      if (processorFutures[i].isDone()) {
        break;
      }

      processors[i].submitInput(input);
      input = processors[i].awaitOutput();
    }

    return input;
  }

  private Future<?>[] restartAllProcessors() {
    resetAllProcessors();
    Future<?>[] futures = new Future[processors.length];
    for (int i = 0; i < processors.length; i++) {
      futures[i] = processors[i].start();
    }

    return futures;
  }

  private void resetAllProcessors() {
    for (int i = 0; i < processors.length; i++) {
      processors[i] = new InstructionProcessor(initialMemory.clone());
    }
  }

}
