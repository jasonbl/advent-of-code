package year2021.day22;

import java.util.Objects;

public class Range {

  private final int min;

  private final int max;

  public Range(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public boolean contains(Range range) {
    return min <= range.getMin() && range.getMax() <= max;
  }

  public boolean overlaps(Range range) {
    int rangeMin = range.getMin();
    int rangeMax = range.getMax();
    return contains(range) || (rangeMin <= min && min <= rangeMax) || (rangeMin <= max && max <= rangeMax);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Range range = (Range) o;
    return min == range.min && max == range.max;
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }
}
