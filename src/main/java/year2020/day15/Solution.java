package year2020.day15;

import util.InputLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solution {

  public static void main(String[] args) {
    int[] numbers = Arrays.stream(InputLoader.load("/year2020/day15/input.txt").split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    System.out.println("Part 1: " + playGame(numbers, 2020));
    System.out.println("Part 2: " + playGame(numbers, 30_000_000));
  }

  private static int playGame(int[] numbers, int targetTurn) {
    Map<Integer, Integer> numberToLastSpokenTurn = new HashMap<>();
    int nextToSpeak = numbers[0];
    for (int turn = 1; turn < targetTurn; turn++) {
      int spoken = nextToSpeak;
      if (turn < numbers.length) {
        nextToSpeak = numbers[turn];
        numberToLastSpokenTurn.put(spoken, turn);
        continue;
      }

      if (numberToLastSpokenTurn.containsKey(spoken)) {
        nextToSpeak = turn - numberToLastSpokenTurn.get(nextToSpeak);
      } else {
        nextToSpeak = 0;
      }

      numberToLastSpokenTurn.put(spoken, turn);
    }

    return nextToSpeak;
  }

}
