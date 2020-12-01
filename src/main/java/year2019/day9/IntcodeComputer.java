package year2019.day9;

import java.util.Collections;
import java.util.List;

public class IntcodeComputer {

  public List<Long> runBoostProgram(long[] program, long input) {
    InstructionProcessor processor = InstructionProcessor.loadProgram(program);
    return processor.startSynchronously(Collections.singletonList(input));
  }

}
