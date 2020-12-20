package year2020.day17;

import util.InputLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Solution {

  public static void main(String[] args) {
    String input = InputLoader.load("/year2020/day17/input.txt");
    System.out.println("Part 1: " + partOne(input));
    System.out.println("Part 2: " + partTwo(input));
  }

  private static long partOne(String input) {
    Map<Position, Character> state = runCycles(buildInitState(input), 6, false);
    return countActive(state);
  }

  private static long partTwo(String input) {
    Map<Position, Character> state = runCycles(buildInitState(input), 6, true);
    return countActive(state);
  }

  private static Map<Position, Character> runCycles(Map<Position, Character> initState, int numCycles,
      boolean isPartTwo) {
    Map<Position, Character> state = initState;
    for (int i = 0; i < numCycles; i++) {
      state = computeNextState(state, isPartTwo);
    }

    return state;
  }

  private static Map<Position, Character> computeNextState(Map<Position, Character> state, boolean isPartTwo) {
    int[][] minMaxes = computeMinMaxes(state.keySet());
    Map<Position, Character> nextState = new HashMap<>();
    int wMin = isPartTwo ? minMaxes[3][0] - 1 : 0;
    int wMax = isPartTwo ? minMaxes[3][1] + 1 : 0;
    for (int w = wMin; w <= wMax; w++) {
      for (int z = minMaxes[2][0] - 1; z <= minMaxes[2][1] + 1; z++) {
        for (int y = minMaxes[1][0] - 1; y <= minMaxes[1][1] + 1; y++) {
          for (int x = minMaxes[0][0] - 1; x <= minMaxes[0][1] + 1; x++) {
            Position pos = new Position(x, y, z, w);
            char nextCubeState = computeNextCubeState(state, pos);
            nextState.put(pos, nextCubeState);
          }
        }
      }
    }

    return nextState;
  }

  private static char computeNextCubeState(Map<Position, Character> state, Position position) {
    int[] directions = { -1, 0, 1 };
    int neighboringActive = 0;
    for (int wDirection : directions) {
      for (int zDirection : directions) {
        for (int yDirection : directions) {
          for (int xDirection : directions) {
            if (xDirection == 0 && yDirection == 0 && zDirection == 0 && wDirection == 0) {
              continue;
            }

            Position neighbor = position.add(new Position(xDirection, yDirection, zDirection, wDirection));
            if (state.computeIfAbsent(neighbor, (val) -> '.') == '#') {
              neighboringActive++;
            }
          }
        }
      }
    }

    Character currentChar = state.computeIfAbsent(position, (val) -> '.');
    if (currentChar == '#' && (neighboringActive < 2 || neighboringActive > 3)) {
      return '.';
    } else if (currentChar == '.' && neighboringActive == 3) {
      return '#';
    }

    return currentChar;
  }

  // Returns the min/max x/y/z/w positions in the form: [
  //   [minX, maxX],
  //   [minY, maxY],
  //   [minZ, maxZ],
  //   [minW, maxW]
  // ]
  private static int[][] computeMinMaxes(Set<Position> positions) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int minZ = Integer.MAX_VALUE;
    int minW = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    int maxZ = Integer.MIN_VALUE;
    int maxW = Integer.MIN_VALUE;
    for (Position position : positions) {
      minX = Math.min(minX, position.getX());
      minY = Math.min(minY, position.getY());
      minZ = Math.min(minZ, position.getZ());
      minW = Math.min(minW, position.getW());
      maxX = Math.max(maxX, position.getX());
      maxY = Math.max(maxY, position.getY());
      maxZ = Math.max(maxZ, position.getZ());
      maxW = Math.max(maxW, position.getW());
    }

    return new int[][] {
        { minX, maxX },
        { minY, maxY },
        { minZ, maxZ },
        { minW, maxW }
    };
  }

  private static long countActive(Map<Position, Character> state) {
    return state.values()
        .stream()
        .filter((charVal) -> charVal == '#')
        .count();
  }

  private static Map<Position, Character> buildInitState(String input) {
    Map<Position, Character> state = new HashMap<>();
    String[] rows = input.split("\n");
    for (int y = 0; y < rows.length; y++) {
      for (int x = 0; x < rows[y].length(); x++) {
        Position pos = new Position(x, y, 0, 0);
        state.put(pos, rows[y].charAt(x));
      }
    }

    return state;
  }

}
