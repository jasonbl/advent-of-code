package year2020.day17;

import java.util.Objects;

public class Position {

  private final int x;

  private final int y;

  private final int z;

  private final int w;

  public Position(int x, int y, int z, int w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public int getW() {
    return w;
  }

  public Position add(Position pos) {
    return new Position(this.getX() + pos.getX(), this.getY() + pos.getY(), this.getZ() + pos.getZ(),
        this.getW() + pos.getW());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return x == position.x &&
        y == position.y &&
        z == position.z &&
        w == position.w;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z, w);
  }

}
