package year2021.day19;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Beacon {

  private int x;

  private int y;

  private int z;

  public Beacon(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
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

  public void rotate(int[][] rotationMatrix) {
    int newX = rotationMatrix[0][0] * x + rotationMatrix[0][1] * y + rotationMatrix[0][2] * z;
    int newY = rotationMatrix[1][0] * x + rotationMatrix[1][1] * y + rotationMatrix[1][2] * z;
    int newZ = rotationMatrix[2][0] * x + rotationMatrix[2][1] * y + rotationMatrix[2][2] * z;
    this.x = newX;
    this.y = newY;
    this.z = newZ;
  }

  public void translate(int[] translation) {
    x += translation[0];
    y += translation[1];
    z += translation[2];
  }

  public BigDecimal distanceTo(Beacon p) {
    int xDiff = p.getX() - this.x;
    int yDiff = p.getY() - this.y;
    int zDiff = p.getZ() - this.z;
    double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    return new BigDecimal(distance)
        .setScale(5, RoundingMode.HALF_UP);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Beacon beacon = (Beacon) o;
    return x == beacon.x && y == beacon.y && z == beacon.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return x + "," + y + "," + z;
  }

}
