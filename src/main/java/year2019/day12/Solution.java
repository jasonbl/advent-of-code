package year2019.day12;

import util.InputLoader;

import java.util.Arrays;

public class Solution {

  public static void main(String[] args) {
    String[] planets = InputLoader.load("/year2019/day12/input.txt")
        .split("\n");

    int[][] positions = new int[planets.length][];
    for (int i = 0; i < planets.length; i++) {
      positions[i] = buildInitialPosition(planets[i]);
    }

    int[][] velocities = new int[planets.length][positions[0].length];

    simulate(positions, velocities, 1000000);
    System.out.println(computeEnergy(positions, velocities));
  }

  private static void simulate(int[][] positions, int[][] velocities, int numTimeSteps) {
    int[][] initialPositions = positions.clone();
    for (int i = 0; i < initialPositions.length; i++) {
      initialPositions[i] = positions[i].clone();
    }

    int[][] initialVelocities = velocities.clone();
    for (int i = 0; i < initialVelocities.length; i++) {
      initialVelocities[i] = velocities[i].clone();
    }

    long xRepeat = 0;
    long yRepeat = 0;
    long zRepeat = 0;
    for (int i = 0; i < numTimeSteps; i++) {
      if (xRepeat == 0 && isRepeat(initialPositions, initialVelocities, positions, velocities, 0)) {
        xRepeat = i;
      }

      if (yRepeat == 0 && isRepeat(initialPositions, initialVelocities, positions, velocities, 1)) {
        yRepeat = i;
      }

      if (zRepeat == 0 && isRepeat(initialPositions, initialVelocities, positions, velocities, 2)) {
        zRepeat = i;
      }

      updateVelocities(positions, velocities);
      updatePositions(positions, velocities);
    }

    System.out.println("Repeat x: " + xRepeat);
    System.out.println("Repeat y: " + yRepeat);
    System.out.println("Repeat z: " + zRepeat);
    // Answer is least common multiple of the above
  }

  private static boolean isRepeat(int[][] initialPositions, int[][] initialVelocities,
                                  int[][] currentPositions, int[][] currentVelocities,
                                  int coordIndex) {
    for (int i = 0; i < initialPositions.length; i++) {
      if (initialPositions[i][coordIndex] != currentPositions[i][coordIndex]) {
        return false;
      }
    }

    for (int i = 0; i < initialVelocities.length; i++) {
      if (initialVelocities[i][coordIndex] != currentVelocities[i][coordIndex]) {
        return false;
      }
    }

    return true;
  }

  private static int computeEnergy(int[][] positions, int[][] velocities) {
    int totalEnergy = 0;
    for (int i = 0; i < positions.length; i++) {
      totalEnergy += computeEnergy(positions[i]) * computeEnergy(velocities[i]);
    }

    return totalEnergy;
  }

  private static int computeEnergy(int[] vector) {
    return Arrays.stream(vector)
        .map(Math::abs)
        .sum();
  }

  private static void updateVelocities(int[][] positions, int[][] velocities) {
    for (int planetIndex = 0; planetIndex < positions.length; planetIndex++) {
      for (int i = planetIndex + 1; i < positions.length; i++) {
        for (int j = 0; j < positions[i].length; j++) {
          int srcPlanetCoord = positions[planetIndex][j];
          int otherPlanetCoord = positions[i][j];
          if (srcPlanetCoord > otherPlanetCoord) {
            velocities[planetIndex][j]--;
            velocities[i][j]++;
          } else if (srcPlanetCoord < otherPlanetCoord) {
            velocities[planetIndex][j]++;
            velocities[i][j]--;
          }
        }
      }
    }
  }

  private static void updatePositions(int[][] positions, int[][] velocities) {
    for (int i = 0; i < positions.length; i++) {
      for (int j = 0; j < positions[i].length; j++) {
        positions[i][j] += velocities[i][j];
      }
    }
  }

  private static int[] buildInitialPosition(String initialPosition) {
    String[] coords = initialPosition.substring(1, initialPosition.length() - 1)
        .split(", ");
    int[] position = new int[coords.length];
    for (int i = 0; i < coords.length; i++) {
      String coord = coords[i];
      position[i] = Integer.parseInt(coord.substring(coord.indexOf("=") + 1));
    }

    return position;
  }

}
