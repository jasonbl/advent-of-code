package year2019.day10;

import util.InputLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2019/day10/input.txt")
        .split("\n");

    boolean[][] map = buildMap(input);
    int[] bestLocation = partOne(map);
    partTwo(map, bestLocation[0], bestLocation[1], 200);
  }

  private static int[] partOne(boolean[][] map) {
    Set<Slope> slopes = buildAllPossibleSlopes(map);
    int maxAsteroids = 0;
    int bestX = 0;
    int bestY = 0;
    for (int y = 0; y < map.length; y++) {
      for (int x = 0; x < map[0].length; x++) {
        if (!map[y][x]) {
          continue;
        }

        int asteroidCount = countAsteroids(map, slopes, x, y);
        if (asteroidCount > maxAsteroids) {
          maxAsteroids = asteroidCount;
          bestX = x;
          bestY = y;
        }
      }
    }

    System.out.println("Max asteroids: " + maxAsteroids + ". Position: (" + bestX + ", " + bestY + ")");
    return new int[] { bestX, bestY };
  }

  private static void partTwo(boolean[][] map, int xSrc, int ySrc, int target) {
    Slope[] orderedSlopes = buildOrderedSlopes(map);
    int asteroidsDestroyed = 0;
    // Hack to get around the fact that orderedSlopes doesn't start at 0,-1 (end of array is 0,1)
    int slopeIndex = orderedSlopes.length - 1;
    int direction = -1;
    int[] mostRecentlyDestroyedAsteroid = null;
    while (asteroidsDestroyed < target) {
      mostRecentlyDestroyedAsteroid = hasAsteroid(map, xSrc, ySrc, orderedSlopes[slopeIndex++], direction, true);
      if (mostRecentlyDestroyedAsteroid != null) {
        asteroidsDestroyed++;
      }

      if (slopeIndex == orderedSlopes.length) {
        slopeIndex = 0;
        direction *= -1;
      }
    }

    System.out.println(target + "th destroyed asteroid position: "
        + mostRecentlyDestroyedAsteroid[0] + ", " + mostRecentlyDestroyedAsteroid[1]);
  }

  private static int countAsteroids(boolean[][] map, Set<Slope> slopes, int xSrc, int ySrc) {
    int count = 0;
    for (Slope slope : slopes) {
      // Check one direction
      if (hasAsteroid(map, xSrc, ySrc, slope, 1, false) != null) {
        count++;
      }

      // Check the other direction
      if (hasAsteroid(map, xSrc, ySrc, slope, -1, false) != null) {
        count++;
      }
    }

    return count;
  }

  // Checks the slope in the specified direction to determine if an asteroid is visible (excluding the source)
  private static int[] hasAsteroid(boolean[][] map, int xSrc, int ySrc, Slope slope, int direction,
                                     boolean shouldDestroy) {
    int currX = xSrc + slope.getRun() * direction;
    int currY = ySrc + slope.getRise() * direction;
    while (currX >= 0 && currX < map.length && currY >=0 && currY < map[0].length) {
      if (map[currY][currX]) {
        if (shouldDestroy) {
          map[currY][currX] = false;
        }
        return new int[] { currX, currY };
      }

      currX += slope.getRun() * direction;
      currY += slope.getRise() * direction;
    }

    return null;
  }

  private static Slope[] buildOrderedSlopes(boolean[][] map) {
    Set<Slope> allSlopes = buildAllPossibleSlopes(map);
    ArrayList<Slope> slopeList = new ArrayList<>(allSlopes);
    slopeList.sort(new SlopeComparator());
    Slope[] array = new Slope[slopeList.size()];
    return slopeList.toArray(array);
  }

  private static Set<Slope> buildAllPossibleSlopes(boolean[][] map) {
    Set<Slope> slopes = new HashSet<>();
    for (int y = 0; y < map.length; y++) {
      for (int x = 0; x < map[0].length; x++) {
        // Exclude the origin
        if (x == 0 && y == 0) {
          continue;
        }

        slopes.add(Slope.build(x, y));
        slopes.add(Slope.build(-x, y));
      }
    }

    return slopes;
  }

  private static boolean[][] buildMap(String[] inputLines) {
    boolean[][] map = new boolean[inputLines.length][inputLines[0].length()];
    for (int y = 0; y < inputLines.length; y++) {
      for (int x = 0; x < inputLines[0].length(); x++) {
        char c = inputLines[y].charAt(x);
        map[y][x] = c == '#';
      }
    }

    return map;
  }

}
