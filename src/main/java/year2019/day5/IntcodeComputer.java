package year2019.day5;

public class IntcodeComputer {

  private final InstructionProcessor instructionProcessor;

  public IntcodeComputer(int[] memory) {
    this.instructionProcessor = new InstructionProcessor(memory);
  }

  public void runDiagnosticTest(int systemId) {
    instructionProcessor.process(systemId);
  }

}
