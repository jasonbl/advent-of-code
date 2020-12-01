package year2019.day3;

import util.InputLoader;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Part1 {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2019/day3/Input.txt")
        .split("\n");

    String[] wireOne = inputs[0].split(",");
    String[] wireTwo = inputs[1].split(",");

    Set<Coordinate> wireOneCoordinates = plotWire(wireOne);
    Set<Coordinate> wireTwoCoordinates = plotWire(wireTwo);

    int shortestDistance = Integer.MAX_VALUE;
    for (Coordinate coordinate : wireOneCoordinates) {
      if (wireTwoCoordinates.contains(coordinate) && coordinate.getX() != 0 && coordinate.getY() != 0) {
        shortestDistance = Math.min(shortestDistance, Math.abs(coordinate.getX()) + Math.abs(coordinate.getY()));
      }
    }

    System.out.println(shortestDistance);
  }

  private static Set<Coordinate> plotWire(String[] wire) {
    int x = 0;
    int y = 0;
    Set<Coordinate> coordinates = new HashSet<>();
    for (String s : wire) {
      char direction = s.charAt(0);
      int amount = Integer.parseInt(s.substring(1));
      if (direction == 'U') {
        for (int i = 0; i < amount; i++) {
          coordinates.add(new Coordinate(x, y++));
        }
      } else if (direction == 'R') {
        for (int i = 0; i < amount; i++) {
          coordinates.add(new Coordinate(x++, y));
        }
      } else if (direction == 'D') {
        for (int i = 0; i < amount; i++) {
          coordinates.add(new Coordinate(x, y--));
        }
      } else if (direction == 'L') {
        for (int i = 0; i < amount; i++) {
          coordinates.add(new Coordinate(x--, y));
        }
      }
    }

    return coordinates;
  }

  private static class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }

      if (!(obj instanceof Coordinate)) {
        return false;
      }

      Coordinate that = (Coordinate) obj;
      return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }
}
