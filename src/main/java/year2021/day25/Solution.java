package year2021.day25;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    System.out.println("Part 1: " + new SeaCucumbers(initGrid()).move());
  }

  private static char[][] initGrid() {
    String[] rows = InputLoader.load("/year2021/day25/input.txt").split("\n");
    char[][] grid = new char[rows.length][rows[0].length()];
    for (int row = 0; row < rows.length; row++) {
      for (int col = 0; col < rows[0].length(); col++) {
        grid[row][col] = rows[row].charAt(col);
      }
    }

    return grid;
  }

}
