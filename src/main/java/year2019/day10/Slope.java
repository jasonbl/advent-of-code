package year2019.day10;

import java.util.Objects;

public class Slope {

  private final int run;

  private final int rise;

  private Slope(int run, int rise) {
    this.run = run;
    this.rise = rise;
  }

  public static Slope build(int run, int rise) {
    if (run == 0 && rise == 0) {
      throw new IllegalArgumentException("Undefined slope");
    } else if (run == 0) {
      return new Slope(0, 1);
    } else if (rise == 0) {
      return new Slope(1, 0);
    }

    // If both numbers are negative this will return a negative value (forcing run and rise to be positive)
    int gcd = findGcd(run, rise);
    if (gcd != 1) {
      run /= gcd;
      rise /= gcd;
    }

    // If signs are different, make sure run is always positive. This helps fix an issue
    // where the slopes 1,-1 and -1,1 equal each other don't have the same hashcode
    if (run < 0) {
      run = -run;
      rise = -rise;
    }

    return new Slope(run, rise);
  }

  public int getRun() {
    return run;
  }

  public int getRise() {
    return rise;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Slope)) {
      return false;
    }

    Slope that = (Slope) obj;
    return (this.run == that.run && this.rise == that.rise)
        || (this.run == -that.run && this.rise == -that.rise);
  }

  @Override
  public int hashCode() {
    return Objects.hash(run, rise);
  }

  private static int findGcd(int a, int b) {
    return a < b ? gcd(b, a) : gcd(a, b);
  }

  private static int gcd(int a, int b) {
    if (b == 0) {
      return a;
    }

    return gcd(b, a % b);
  }

}
