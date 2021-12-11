package year2021.day9;

import util.InputLoader;

import java.util.ArrayList;
import java.util.Comparator;

public class Solution {

  public static void main(String[] args) {
    int[][] floor = buildFloor();
    System.out.println("Part 1: " + partOne(floor));
    System.out.println("Part 2: " + partTwo(floor));
  }

  private static int partOne(int[][] floor) {
    int risk = 0;
    for (int y = 0; y < floor.length; y++) {
      for (int x = 0; x < floor[y].length; x++) {
        risk += getRisk(floor, x, y);
      }
    }

    return risk;
  }

  private static long partTwo(int[][] floor) {
    ArrayList<Long> basinSizes = new ArrayList<>();
    for (int y = 0; y < floor.length; y++) {
      for (int x = 0; x < floor[y].length; x++) {
        if (isLowPoint(floor, x, y)) {
          basinSizes.add(getBasinSize(floor, x, y));
        }
      }
    }

    basinSizes.sort(Comparator.reverseOrder());
    return basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
  }

  private static int getRisk(int[][] floor, int x, int y) {
    return isLowPoint(floor, x, y) ? floor[y][x] + 1 : 0;
  }

  private static long getBasinSize(int[][] floor, int x, int y) {
    boolean[][] visited = new boolean[floor.length][floor[0].length];
    return getBasinSizeHelper(floor, visited, x, y);
  }

  private static long getBasinSizeHelper(int[][] floor, boolean[][] visited, int x, int y) {
    if (y < 0 || y >= floor.length || x < 0 || x >= floor[y].length || visited[y][x] || floor[y][x] == 9) {
      return 0;
    }

    visited[y][x] = true;
    return 1 + getBasinSizeHelper(floor, visited, x + 1, y)
        + getBasinSizeHelper(floor, visited, x - 1, y)
        + getBasinSizeHelper(floor, visited, x, y + 1)
        + getBasinSizeHelper(floor, visited, x, y - 1);
  }

  private static boolean isLowPoint(int[][] floor, int x, int y) {
    return !((y > 0 && floor[y][x] >= floor[y - 1][x])
        || (x > 0 && floor[y][x] >= floor[y][x - 1])
        || (y < floor.length - 1 && floor[y][x] >= floor[y + 1][x])
        || (x < floor[y].length - 1 && floor[y][x] >= floor[y][x + 1]));
  }

  private static int[][] buildFloor() {
    String[] lines = InputLoader.load("/year2021/day9/input.txt").split("\n");
    int[][] floor = new int[lines.length][];
    for (int i = 0; i < lines.length; i++) {
      floor[i] = new int[lines[i].length()];
      for (int j = 0; j < lines[i].length(); j++) {
        floor[i][j] = Integer.parseInt(lines[i].charAt(j) + "");
      }
    }

    return floor;
  }
}
