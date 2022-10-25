package year2021.day22;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class Cuboid {

  private static final Pattern PATTERN =
      Pattern.compile("^(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)$");

  private final boolean on;

  private final Range xRange;

  private final Range yRange;

  private final Range zRange;

  public Cuboid(boolean on, Range xRange, Range yRange, Range zRange) {
    this.on = on;
    this.xRange = xRange;
    this.yRange = yRange;
    this.zRange = zRange;
  }

  public Cuboid(boolean on, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
    this.on = on;
    this.xRange = new Range(xMin, xMax);
    this.yRange = new Range(yMin, yMax);
    this.zRange = new Range(zMin, zMax);
  }

  public boolean isOn() {
    return on;
  }

  public Range getxRange() {
    return xRange;
  }

  public Range getyRange() {
    return yRange;
  }

  public Range getzRange() {
    return zRange;
  }

  public boolean contains(Cuboid cuboid) {
    return xRange.contains(cuboid.getxRange())
        && yRange.contains(cuboid.getyRange())
        && zRange.contains(cuboid.getzRange());
  }

  public boolean overlaps(Cuboid cuboid) {
    return xRange.overlaps(cuboid.getxRange())
        && yRange.overlaps(cuboid.getyRange())
        && zRange.overlaps(cuboid.getzRange());
  }

  public List<Cuboid> subtract(Cuboid toRemove) {
    if (!overlaps(toRemove)) {
      return singletonList(this);
    }

    // At most there can only be 6 remaining cuboids after a subtraction, so we'll treat each on a case by case basis
    // Bottom
    int bottomZMin = zRange.getMin();
    int bottomZMax = toRemove.zRange.getMin() - 1;
    Cuboid bottom = bottomZMax < bottomZMin ? null :
        new Cuboid(on, xRange, yRange, new Range(bottomZMin, bottomZMax));

    // Left
    int leftXMin = xRange.getMin();
    int leftXMax = toRemove.xRange.getMin() - 1;
    int leftZMin = bottom == null ? zRange.getMin() : bottomZMax + 1;
    int leftZMax = zRange.getMax();
    Cuboid left = leftXMax < leftXMin ? null :
        new Cuboid(on, new Range(leftXMin, leftXMax), yRange, new Range(leftZMin, leftZMax));

    // Right
    int rightXMin = toRemove.xRange.getMax() + 1;
    int rightXMax = xRange.getMax();
    Cuboid right = rightXMax < rightXMin ? null :
        new Cuboid(on, new Range(rightXMin, rightXMax), yRange, new Range(leftZMin, leftZMax));

    // Back
    int backXMin = left == null ? xRange.getMin() : leftXMax + 1;
    int backXMax = right == null ? xRange.getMax() : rightXMin - 1;
    int backYMin = toRemove.getyRange().getMax() + 1;
    int backYMax = yRange.getMax();
    Cuboid back = backYMax < backYMin ? null :
        new Cuboid(on, new Range(backXMin, backXMax), new Range(backYMin, backYMax),
            new Range(leftZMin, leftZMax));

    // Front
    int frontYMin = yRange.getMin();
    int frontYMax = toRemove.getyRange().getMin() - 1;
    Cuboid front = frontYMax < frontYMin ? null :
        new Cuboid(on, new Range(backXMin, backXMax), new Range(frontYMin, frontYMax),
            new Range(leftZMin, leftZMax));

    // Top
    int topYMin = front == null ? yRange.getMin() : frontYMax + 1;
    int topYMax = back == null ? yRange.getMax() : backYMin - 1;
    int topZMin = toRemove.zRange.getMax() + 1;
    int topZMax = zRange.getMax();
    Cuboid top = topZMax < topZMin ? null :
        new Cuboid(on, new Range(backXMin, backXMax), new Range(topYMin, topYMax), new Range(topZMin, topZMax));

    return Stream.of(bottom, left, right, back, front, top)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cuboid cuboid = (Cuboid) o;
    return on == cuboid.on
        && xRange.equals(cuboid.xRange)
        && yRange.equals(cuboid.yRange)
        && zRange.equals(cuboid.zRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(on, xRange, yRange, zRange);
  }

  public static Cuboid fromString(String s) {
    Matcher matcher = PATTERN.matcher(s);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid reboot step");
    }

    boolean on = matcher.group(1).equals("on");
    int xMin = Integer.parseInt(matcher.group(2));
    int xMax = Integer.parseInt(matcher.group(3));
    int yMin = Integer.parseInt(matcher.group(4));
    int yMax = Integer.parseInt(matcher.group(5));
    int zMin = Integer.parseInt(matcher.group(6));
    int zMax = Integer.parseInt(matcher.group(7));
    return new Cuboid(on, new Range(xMin, xMax), new Range(yMin, yMax), new Range(zMin, zMax));
  }

}
