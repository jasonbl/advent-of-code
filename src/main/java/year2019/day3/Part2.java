package year2019.day3;

import util.InputLoader;

import java.util.HashMap;
import java.util.Map;

public class Part2 {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2019/day3/Input.txt")
        .split("\n");

    String[] wireOne = inputs[0].split(",");
    String[] wireTwo = inputs[1].split(",");

    Map<String, Coordinate> wireOneCoordinates = plotWire(wireOne);
    Map<String, Coordinate> wireTwoCoordinates = plotWire(wireTwo);

    int bestSteps = Integer.MAX_VALUE;
    for (Coordinate coordinate : wireOneCoordinates.values()) {
      if (wireTwoCoordinates.containsKey(buildKey(coordinate)) && coordinate.getX() != 0 && coordinate.getY() != 0) {
        bestSteps = Math.min(bestSteps,
            coordinate.getWireLength() + wireTwoCoordinates.get(buildKey(coordinate)).getWireLength());
      }
    }

    System.out.println(bestSteps);
  }

  private static Map<String, Coordinate> plotWire(String[] wire) {
    int x = 0;
    int y = 0;
    int wireLength = 0;
    Map<String, Coordinate> coordinates = new HashMap<>();
    for (String s : wire) {
      char direction = s.charAt(0);
      int amount = Integer.parseInt(s.substring(1));
      if (direction == 'U') {
        for (int i = 0; i < amount; i++) {
          // really could have done all this without the Coordinate class, using coordinate key -> wireLength map
          Coordinate coordinate = new Coordinate(x, y++, wireLength++);
          coordinates.putIfAbsent(buildKey(coordinate), coordinate);
        }
      } else if (direction == 'R') {
        for (int i = 0; i < amount; i++) {
          Coordinate coordinate = new Coordinate(x++, y, wireLength++);
          coordinates.putIfAbsent(buildKey(coordinate), coordinate);
        }
      } else if (direction == 'D') {
        for (int i = 0; i < amount; i++) {
          Coordinate coordinate = new Coordinate(x, y--, wireLength++);
          coordinates.putIfAbsent(buildKey(coordinate), coordinate);
        }
      } else if (direction == 'L') {
        for (int i = 0; i < amount; i++) {
          Coordinate coordinate = new Coordinate(x--, y, wireLength++);
          coordinates.putIfAbsent(buildKey(coordinate), coordinate);
        }
      }
    }

    return coordinates;
  }

  private static String buildKey(Coordinate coordinate) {
    return coordinate.getX() + "-" + coordinate.getY();
  }

  private static class Coordinate {

    private int x;
    private int y;
    private int wireLength;

    public Coordinate(int x, int y, int wireLength) {
      this.x = x;
      this.y = y;
      this.wireLength = wireLength;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public int getWireLength() {
      return wireLength;
    }

  }
}

