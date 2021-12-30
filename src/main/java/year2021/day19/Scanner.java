package year2021.day19;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Scanner {

  private final Beacon[] beacons;

  private final BigDecimal[][] distanceMatrix;

  private int[] absolutePosition;

  public Scanner(Beacon[] beacons) {
    this.beacons = beacons;
    this.distanceMatrix = computeDistanceMatrix();
    this.absolutePosition = new int[] { 0, 0, 0 };
  }

  public Beacon[] getBeacons() {
    return beacons;
  }

  public BigDecimal[][] getDistanceMatrix() {
    return distanceMatrix;
  }

  public int[] getAbsolutePosition() {
    return absolutePosition;
  }

  public void setAbsolutePosition(int[] absolutePosition) {
    this.absolutePosition = absolutePosition;
  }

  public void rotate(int[][] rotationMatrix) {
    Arrays.stream(beacons)
        .forEach((beacon) -> beacon.rotate(rotationMatrix));
  }

  public void translate(int[] translation) {
    Arrays.stream(beacons)
        .forEach((beacon) -> beacon.translate(translation));
  }

  public Map<Beacon, Beacon> findMatchingBeacons(Scanner that) {
    Map<Beacon, Beacon> matchingBeacons = new HashMap<>();
    for (int i = 0; i < distanceMatrix.length; i++) {
      Set<BigDecimal> distances = Arrays.stream(distanceMatrix[i])
          .collect(Collectors.toSet());

      // Compare distances for beacon i to distances for every beacon j seen by the other scanner
      // If 12 or more distances are shared between beacon i and j, they're the same beacon
      BigDecimal[][] thatDistanceMatrix = that.getDistanceMatrix();
      for (int j = 0; j < thatDistanceMatrix.length; j++) {
        Set<BigDecimal> thatDistances = Arrays.stream(thatDistanceMatrix[j])
            .collect(Collectors.toSet());

        thatDistances.retainAll(distances);
        if (thatDistances.size() >= 12) {
          matchingBeacons.put(beacons[i], that.getBeacons()[j]);
          break;
        }
      }
    }

    return matchingBeacons;
  }

  private BigDecimal[][] computeDistanceMatrix() {
    BigDecimal[][] distances = new BigDecimal[beacons.length][beacons.length];
    for (int i = 0; i < beacons.length; i++) {
      for (int j = i; j < beacons.length; j++) {
        BigDecimal distance = beacons[i].distanceTo(beacons[j]);
        distances[i][j] = distance;
        distances[j][i] = distance;
      }
    }

    return distances;
  }

}
