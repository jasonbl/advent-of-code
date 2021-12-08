package year2021.day5;

import util.InputLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern REGEX = Pattern.compile("^(\\d+),(\\d+) -> (\\d+),(\\d+)$");

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2021/day5/input.txt").split("\n");
    System.out.println("Answer: " + solve(inputs));
  }

  private static long solve(String[] inputs) {
    Map<Point, Integer> pointCounts = new HashMap<>();
    for (String input : inputs) {
      Matcher matcher = REGEX.matcher(input);
      if (!matcher.matches()) {
        throw new RuntimeException("Pattern match failure");
      }

      int x1 = Integer.parseInt(matcher.group(1));
      int y1 = Integer.parseInt(matcher.group(2));
      int x2 = Integer.parseInt(matcher.group(3));
      int y2 = Integer.parseInt(matcher.group(4));
//      if (x1 == x2 || y1 == y2) {
        getPointsOnLine(new Point(x1, y1), new Point(x2, y2))
            .forEach((point) -> pointCounts.merge(point, 1, Integer::sum));
//      }
    }

    return pointCounts.entrySet()
        .stream()
        .filter((entry) -> entry.getValue() > 1)
        .count();
  }

  private static Set<Point> getPointsOnLine(Point p1, Point p2) {
    int x = p1.getX();
    int y = p1.getY();
    int delX = Integer.signum(p2.getX() - p1.getX());
    int delY = Integer.signum(p2.getY() - p1.getY());
    Set<Point> pointsOnLine = new HashSet<>();
    while (x != p2.getX() || y != p2.getY()) {
      pointsOnLine.add(new Point(x, y));
      x += delX;
      y += delY;
    }

    pointsOnLine.add(new Point(p2.getX(), p2.getY()));
    return pointsOnLine;
  }

}
