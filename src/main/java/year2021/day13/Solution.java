package year2021.day13;

import util.InputLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PATTERN = Pattern.compile("^fold along (x|y)=(\\d+)$");

  public static void main(String[] args) {
    String[] parts = InputLoader.load("/year2021/day13/input.txt").split("\n\n");
    boolean[][] grid = initGrid(parts);
    String[] folds = parts[1].split("\n");
    for (String fold : folds) {
      Matcher matcher = PATTERN.matcher(fold);
      if (!matcher.matches()) {
        throw new RuntimeException("Invalid match");
      }

      String direction = matcher.group(1);
      int index = Integer.parseInt(matcher.group(2));
      grid = fold(grid, direction, index);
      printGrid(grid);
    }
  }

  private static boolean[][] fold(boolean[][] grid, String direction, int index) {
    return direction.equals("x") ? foldX(grid, index) : foldY(grid, index);
  }

  private static boolean[][] foldX(boolean[][] grid, int index) {
    boolean[][] newGrid = new boolean[grid.length][index];
    for (int y = 0; y < newGrid.length; y++) {
      for (int x = 0; x < newGrid[y].length; x++) {
        newGrid[y][x] = grid[y][x] || grid[y][grid[y].length - x - 1];
      }
    }

    return newGrid;
  }

  private static boolean[][] foldY(boolean[][] grid, int index) {
    boolean[][] newGrid = new boolean[index][grid[0].length];
    for (int y = 0; y < newGrid.length; y++) {
      for (int x = 0; x < newGrid[y].length; x++) {
        newGrid[y][x] = grid[y][x] || grid[grid.length - y - 1][x];
      }
    }

    return newGrid;
  }

  private static void printGrid(boolean[][] grid) {
    int numDots = 0;
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[y].length; x++) {
        if (grid[y][x]) {
          numDots++;
          System.out.print("#");
        } else {
          System.out.print(" ");
        }
      }

      System.out.println();
    }

    System.out.println("Dot count: " + numDots + "\n");
  }

  private static boolean[][] initGrid(String[] parts) {
    String[] points = parts[0].split("\n");
    int maxX = -1;
    int maxY = -1;
    for (String point : points) {
      String[] coords = point.split(",");
      maxX = Math.max(maxX, Integer.parseInt(coords[0] + ""));
      maxY = Math.max(maxY, Integer.parseInt(coords[1] + ""));
    }

    boolean[][] grid = new boolean[maxY + 1][maxX + 1];
    for (String point : points) {
      String[] coords = point.split(",");
      int x = Integer.parseInt(coords[0] + "");
      int y = Integer.parseInt(coords[1] + "");
      grid[y][x] = true;
    }

    return grid;
  }

}
