package year2021.day5;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point position = (Point) o;
    return x == position.x &&
        y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

}
