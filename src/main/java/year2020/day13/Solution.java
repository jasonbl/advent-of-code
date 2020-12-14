package year2020.day13;

import util.InputLoader;

import java.util.Arrays;

public class Solution {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2020/day13/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(inputs));
    System.out.println("Part 2: " + partTwo(inputs));
  }

  private static int partOne(String[] inputs) {
    int timestamp = Integer.parseInt(inputs[0]);
    int[] buses = Arrays.stream(inputs[1].split(","))
        .filter((bus) -> !bus.equals("x"))
        .mapToInt(Integer::parseInt)
        .toArray();

    int minWaitTime = Integer.MAX_VALUE;
    int bestBus = -1;
    for (int bus : buses) {
      int waitTime = computeWaitTime(timestamp, bus);
      if (waitTime < minWaitTime) {
        minWaitTime = waitTime;
        bestBus = bus;
      }
    }

    return bestBus * minWaitTime;
  }

  private static long partTwo(String[] inputs) {
    String[] buses = inputs[1].split(",");
    long baseTime = 1;
    long addend = 1;
    for (int i = 0; i < buses.length; i++) {
      if (buses[i].equals("x")) {
        continue;
      }

      int currentBus = Integer.parseInt(buses[i]);

      long timeToCheck = baseTime;
      while ((timeToCheck + i) % currentBus != 0) {
        timeToCheck += addend;
      }

      baseTime = timeToCheck;
      addend *= currentBus;
    }

    return baseTime;
  }

  private static int computeWaitTime(int timestamp, int bus) {
    if (timestamp % bus == 0) {
      return 0;
    }

    return bus - (timestamp % bus);
  }

}
