package year2021.day10;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import util.InputLoader;

import java.util.*;

public class Solution {

  private static final Map<Character, Integer> ERROR_SCORES = new ImmutableMap.Builder<Character, Integer>()
      .put(')', 3)
      .put(']', 57)
      .put('}', 1197)
      .put('>', 25137)
      .build();

  private static final Map<Character, Integer> INCOMPLETE_SCORES = new ImmutableMap.Builder<Character, Integer>()
      .put('(', 1)
      .put('[', 2)
      .put('{', 3)
      .put('<', 4)
      .build();

  private static final BiMap<Character, Character> SYNTAX_PAIRS = new ImmutableBiMap.Builder<Character, Character>()
      .put('(', ')')
      .put('[', ']')
      .put('{', '}')
      .put('<', '>')
      .build();

  private static final Set<Character> TERMINAL_CHARS = ERROR_SCORES.keySet();

  public static void main(String[] args) {
    String[] lines = InputLoader.load("/year2021/day10/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(lines));
    System.out.println("Part 2: " + partTwo(lines));
  }

  private static int partOne(String[] lines) {
    return Arrays.stream(lines)
        .mapToInt(Solution::computeFirstErrorScore)
        .sum();
  }

  private static long partTwo(String[] lines) {
    long[] scores = Arrays.stream(lines)
        .mapToLong(Solution::computeIncompleteLineScore)
        .filter((l) -> l != -1)
        .toArray();

    Arrays.sort(scores);
    return scores[scores.length / 2];
  }

  private static int computeFirstErrorScore(String line) {
    Deque<Character> stack = new ArrayDeque<>();
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (isTerminalChar(c)) {
        if (stack.isEmpty() || stack.getFirst() != SYNTAX_PAIRS.inverse().get(c)) {
          return ERROR_SCORES.get(c);
        }

        stack.pop();
      } else {
        stack.push(c);
      }
    }

    return 0;
  }

  private static long computeIncompleteLineScore(String line) {
    Deque<Character> stack = new ArrayDeque<>();
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (isTerminalChar(c)) {
        if (stack.isEmpty() || stack.getFirst() != SYNTAX_PAIRS.inverse().get(c)) {
          return -1;
        }

        stack.pop();
      } else {
        stack.push(c);
      }
    }

    long score = 0;
    while (!stack.isEmpty()) {
      score *= 5;
      score += INCOMPLETE_SCORES.get(stack.pop());
    }

    return score;
  }

  private static boolean isTerminalChar(char c) {
    return TERMINAL_CHARS.contains(c);
  }

}
