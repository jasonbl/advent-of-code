package year2021.day22;

import util.InputLoader;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {

  private static final Range INIT_RANGE = new Range(-50, 50);
  private static final Cuboid INIT_CUBOID = new Cuboid(false, INIT_RANGE, INIT_RANGE, INIT_RANGE);

  public static void main(String[] args) {
    List<Cuboid> steps = initSteps();
    System.out.println("Part 1: " + partOne(steps));
    System.out.println("Part 2: " + partTwo(steps));
  }

  private static int partOne(List<Cuboid> steps) {
    boolean[][][] grid = initGrid(steps);
    return countOn(grid);
  }

  private static long partTwo(List<Cuboid> steps) {
    List<Cuboid> grid = new ArrayList<>();
    for (Cuboid step : steps) {
      grid = applyStep(grid, step);
    }

    return countOn(grid);
  }

  private static List<Cuboid> applyStep(List<Cuboid> grid, Cuboid step) {
    return step.isOn() ? applyOnStep(grid, step) : applyOffStep(grid, step);
  }

  private static List<Cuboid> applyOnStep(List<Cuboid> grid, Cuboid step) {
    // Retain the current grid, this step will only add to it
    List<Cuboid> nextGrid = new ArrayList<>(grid);
    Deque<Cuboid> toAddQueue = new ArrayDeque<>();
    toAddQueue.add(step);
    while (!toAddQueue.isEmpty()) {
      Cuboid toAdd = toAddQueue.remove();
      boolean overlapped = false;

      // Attempt to add the cuboid to the grid
      for (Cuboid cuboid : grid) {
        // If the cuboid overlaps with an existing cuboid, subtract the existing cuboid from it and add those parts
        // back to the queue
        if (toAdd.overlaps(cuboid)) {
          List<Cuboid> remainingToAdd = toAdd.subtract(cuboid);
          toAddQueue.addAll(remainingToAdd);
          overlapped = true;
          break;
        }
      }

      // If the cuboid didn't overlap with any existing cuboids, officially add it to the grid
      if (!overlapped) {
        nextGrid.add(toAdd);
      }
    }

    return nextGrid;
  }

  private static List<Cuboid> applyOffStep(List<Cuboid> grid, Cuboid step) {
    List<Cuboid> nextGrid = new ArrayList<>();
    for (Cuboid cuboid : grid) {
      if (step.overlaps(cuboid)) {
        nextGrid.addAll(cuboid.subtract(step));
      } else {
        nextGrid.add(cuboid);
      }
    }

    return nextGrid;
  }

  private static long countOn(List<Cuboid> grid) {
    long count = 0;
    for (Cuboid cuboid : grid) {
      long xRange = cuboid.getxRange().getMax() - cuboid.getxRange().getMin() + 1;
      long yRange = cuboid.getyRange().getMax() - cuboid.getyRange().getMin() + 1;
      long zRange = cuboid.getzRange().getMax() - cuboid.getzRange().getMin() + 1;
      count += xRange * yRange * zRange;
    }

    return count;
  }

  private static int countOn(boolean[][][] grid) {
    int count = 0;
    for (boolean[][] z : grid) {
      for (boolean[] y : z) {
        for (boolean x : y) {
          if (x) {
            count++;
          }
        }
      }
    }

    return count;
  }

  private static boolean[][][] initGrid(List<Cuboid> steps) {
    boolean[][][] grid = new boolean[101][101][101];
    for (Cuboid step : steps) {
      if (!INIT_CUBOID.contains(step)) {
        continue;
      }

      for (int z = step.getzRange().getMin(); z <= step.getzRange().getMax(); z++) {
        for (int y = step.getyRange().getMin(); y <= step.getyRange().getMax(); y++) {
          for (int x = step.getxRange().getMin(); x <= step.getxRange().getMax(); x++) {
            grid[z + 50][y + 50][x + 50] = step.isOn();
          }
        }
      }
    }

    return grid;
  }

  private static List<Cuboid> initSteps() {
    return Arrays.stream(InputLoader.load("/year2021/day22/input.txt").split("\n"))
        .map(Cuboid::fromString)
        .collect(Collectors.toList());
  }


}
