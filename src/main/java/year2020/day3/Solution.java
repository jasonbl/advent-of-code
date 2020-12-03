package year2020.day3;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] rows = InputLoader.load("/year2020/day3/input.txt").split("\n");
    char[][] map = buildMap(rows);

    System.out.println("Part 1: " + countTrees(map, 3, 1));

    long partTwo = countTrees(map, 1, 1)
        * countTrees(map, 3, 1)
        * countTrees(map, 5, 1)
        * countTrees(map, 7, 1)
        * countTrees(map, 1, 2);
    System.out.println("Part 2: " + partTwo);
  }

  private static char[][] buildMap(String[] rows) {
    int height = rows.length;
    int width = rows[0].length();
    char[][] map = new char[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        map[i][j] = rows[i].charAt(j);
      }
    }

    return map;
  }

  private static long countTrees(char[][] map, int xSlope, int ySlope) {
    int height = map.length;
    int width = map[0].length;

    int x = 0;
    int y = 0;
    long treeCount = 0;
    while (y < height) {
      if (map[y][x] == '#') {
        treeCount++;
      }

      x = (x + xSlope) % width;
      y += ySlope;
    }

    return treeCount;
  }

}
