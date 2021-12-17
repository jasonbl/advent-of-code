package year2021.day11;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    System.out.println("Part 1: " + partOne());
    System.out.println("Part 2: " + partTwo());
  }

  private static int partOne() {
    int[][] grid = initGrid();
    return simulate(grid, 100);
  }

  private static int partTwo() {
    int[][] grid = initGrid();
    int maxFlashes = grid.length * grid[0].length;
    int steps = 1;
    while (simulateStep(grid) != maxFlashes) {
      steps++;
    }

    return steps;
  }

  private static int simulate(int[][] grid, int steps) {
    int numFlashes = 0;
    for (int i = 0; i < steps; i++) {
      numFlashes += simulateStep(grid);
    }

    return numFlashes;
  }

  private static int simulateStep(int[][] grid) {
    increaseEnergy(grid);
    simulateFlashes(grid);
    return countFlashes(grid);
  }

  private static void increaseEnergy(int[][] grid) {
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[y].length; x++) {
        grid[y][x]++;
      }
    }
  }

  private static void simulateFlashes(int[][] grid) {
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[y].length; x++) {
        if (grid[y][x] > 9) {
          flash(grid, x, y);
        }
      }
    }
  }

  private static int countFlashes(int[][] grid) {
    int numFlashes = 0;
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[y].length; x++) {
        if (grid[y][x] == -1) {
          numFlashes++;
          grid[y][x] = 0;
        }
      }
    }

    return numFlashes;
  }

  private static void flash(int[][] grid, int x, int y) {
    grid[y][x] = -1;
    flashNeighbor(grid, x - 1, y - 1);
    flashNeighbor(grid, x - 1, y);
    flashNeighbor(grid, x - 1, y + 1);
    flashNeighbor(grid, x, y - 1);
    flashNeighbor(grid, x, y + 1);
    flashNeighbor(grid, x + 1, y - 1);
    flashNeighbor(grid, x + 1, y);
    flashNeighbor(grid, x + 1, y + 1);
  }

  private static void flashNeighbor(int[][] grid, int x, int y) {
    if (y < 0 || y >= grid.length || x < 0 || x >= grid[y].length || grid[y][x] == -1) {
      return;
    }

    grid[y][x]++;
    if (grid[y][x] > 9) {
      flash(grid, x, y);
    }
  }

  private static int[][] initGrid() {
    String[] lines = InputLoader.load("/year2021/day11/input.txt").split("\n");
    int[][] grid = new int[lines.length][];
    for (int y = 0; y < lines.length; y++) {
      grid[y] = new int[lines[y].length()];
      for (int x = 0; x < lines[y].length(); x++) {
        grid[y][x] = Integer.parseInt(lines[y].charAt(x) + "");
      }
    }

    return grid;
  }

}
