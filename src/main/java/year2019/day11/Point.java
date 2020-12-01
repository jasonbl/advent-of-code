package year2019.day11;

import java.util.Objects;

public class Point {

  private final int x;

  private final int y;

  public Point(int x, int y) {
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
    if (obj == this) {
      return false;
    }

    if (!(obj instanceof Point)) {
      return false;
    }

    Point that = (Point) obj;
    return this.x == that.x && this.y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

}
