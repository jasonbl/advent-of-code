package year2021.day17;

import util.InputLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PATTERN = Pattern.compile("^target area: x=(\\d+)\\.\\.(\\d+), y=(-?\\d+)\\.\\.(-?\\d+)$");

  public static void main(String[] args) {
    String input = InputLoader.load("/year2021/day17/input.txt");
    Matcher matcher = PATTERN.matcher(input);
    if (!matcher.matches()) {
      throw new RuntimeException("No match");
    }

    int xMin = Integer.parseInt(matcher.group(1));
    int xMax = Integer.parseInt(matcher.group(2));
    int yMin = Integer.parseInt(matcher.group(3));
    int yMax = Integer.parseInt(matcher.group(4));

    int maxPeak = -1;
    int count = 0;
    for (int y = yMin; y < -yMin; y++) {
      for (int x = 0; x <= xMax; x++) {
        if (simulate(x, y, xMin, xMax, yMin, yMax)) {
          int peak = y * (y + 1) / 2;
          maxPeak = Math.max(peak, maxPeak);
          count++;
        }
      }
    }

    System.out.println("Part 1: " + maxPeak);
    System.out.println("Part 2: " + count);
  }

  private static boolean simulate(int xVel, int yVel, int xMin, int xMax, int yMin, int yMax) {
    int x = 0;
    int y = 0;
    while (y > yMin) {
      x += xVel;
      y += yVel;
      xVel = xVel == 0 ? 0 : xVel - 1;
      yVel--;

      if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
        return true;
      }
    }

    return false;
  }

}
