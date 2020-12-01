package year2019.day10;

import java.util.Comparator;

public class SlopeComparator implements Comparator<Slope> {

  @Override
  public int compare(Slope one, Slope two) {
    double angleOne = Math.atan2(one.getRise(), one.getRun());
    double angleTwo = Math.atan2(two.getRise(), two.getRun());
    return Double.compare(angleOne, angleTwo);
  }

}
