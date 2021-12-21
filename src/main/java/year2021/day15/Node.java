package year2021.day15;

import java.util.Objects;

public class Node implements Comparable<Node> {

  private final int x;

  private final int y;

  private final int risk;

  private int distanceTo;

  public Node(int x, int y, int risk) {
    this.x = x;
    this.y = y;
    this.risk = risk;
    this.distanceTo = Integer.MAX_VALUE;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getRisk() {
    return risk;
  }

  public int getDistanceTo() {
    return distanceTo;
  }

  public void setDistanceTo(int distanceTo) {
    this.distanceTo = distanceTo;
  }

  @Override
  public int compareTo(Node that) {
    return this.distanceTo - that.distanceTo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node position = (Node) o;
    return x == position.x &&
        y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

}

