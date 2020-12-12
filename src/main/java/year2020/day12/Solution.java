package year2020.day12;

import util.InputLoader;

public class Solution {

  public static void main (String[] args) {
    String[] instructions = InputLoader.load("/year2020/day12/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(instructions));
    System.out.println("Part 2: " + partTwo(instructions));
  }

  private static long partOne(String[] instructions) {
    int degrees = 0;
    double shipX = 0;
    double shipY = 0;
    for (String instruction : instructions) {
      char action = instruction.charAt(0);
      int value = Integer.parseInt(instruction.substring(1));
      switch (action) {
        case 'N':
          shipY += value;
          break;
        case 'S':
          shipY -= value;
          break;
        case 'E':
          shipX += value;
          break;
        case 'W':
          shipX -= value;
          break;
        case 'L':
          degrees += value;
          break;
        case 'R':
          degrees -= value;
          break;
        case 'F':
          double radians = Math.toRadians(degrees);
          shipX += value * Math.cos(radians);
          shipY += value * Math.sin(radians);
          break;
      }
    }

    return Math.round(Math.abs(shipX) + Math.abs(shipY));
  }

  private static long partTwo(String[] instructions) {
    double waypointRelX = 10;
    double waypointRelY = 1;
    double shipX = 0;
    double shipY = 0;
    for (String instruction : instructions) {
      char action = instruction.charAt(0);
      int value = Integer.parseInt(instruction.substring(1));
      int turnDirection = 1;
      switch (action) {
        case 'N':
          waypointRelY += value;
          break;
        case 'S':
          waypointRelY -= value;
          break;
        case 'E':
          waypointRelX += value;
          break;
        case 'W':
          waypointRelX -= value;
          break;
        case 'R':
          turnDirection = -1;
        case 'L':
          double waypointRelAngle = Math.atan2(waypointRelY, waypointRelX);
          double newAngle = waypointRelAngle + Math.toRadians(turnDirection * value);
          double relativeDist = Math.sqrt(Math.pow(waypointRelX, 2) + Math.pow(waypointRelY, 2));
          waypointRelX = relativeDist * Math.cos(newAngle);
          waypointRelY = relativeDist * Math.sin(newAngle);
          break;
        case 'F':
          shipX += value * waypointRelX;
          shipY += value * waypointRelY;
          break;
      }
    }

    return Math.round(Math.abs(shipX) + Math.abs(shipY));
  }

}
