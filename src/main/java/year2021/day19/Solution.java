package year2021.day19;

import util.InputLoader;

import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public class Solution {

  public static void main(String[] args) {
    Scanner[] scanners = buildScanners();
    Set<Integer> locatedScanners = new HashSet<>();
    locatedScanners.add(0);
    Deque<Integer> toVisit = new ArrayDeque<>();
    toVisit.add(0);
    while (!toVisit.isEmpty()) {
      int scannerIndex = toVisit.remove();
      // Relocate all overlapping scanners, skipping this scanner and scanners that have already been located
      for (int i = 0; i < scanners.length; i++) {
        if (i == scannerIndex || locatedScanners.contains(i)) {
          continue;
        }

        Map<Beacon, Beacon> matchingBeacons = scanners[scannerIndex].findMatchingBeacons(scanners[i]);
        if (matchingBeacons.isEmpty()) {
          // Scanners don't overlap
          continue;
        }

//        System.out.println("Match between scanners " + scannerIndex + " and " + i);
//        printMatchingBeacons(matchingBeacons);

        int[][] rotationMatrix = computeRotationMatrix(matchingBeacons);
        scanners[i].rotate(rotationMatrix);
//        printMatchingBeacons(matchingBeacons);

        int[] translation = computeTranslation(matchingBeacons);
        scanners[i].translate(translation);
        scanners[i].setAbsolutePosition(translation);
//        printMatchingBeacons(matchingBeacons);

        locatedScanners.add(i);
        toVisit.add(i);
      }
    }

    Set<Beacon> uniqueBeacons = new HashSet<>();
    for (Scanner scanner : scanners) {
      uniqueBeacons.addAll(Arrays.asList(scanner.getBeacons()));
    }

    System.out.println("Part 1: " + uniqueBeacons.size());

    int maxDistance = -1;
    for (int i = 0; i < scanners.length; i++) {
      for (int j = i + 1; j < scanners.length; j++) {
        int[] from = scanners[i].getAbsolutePosition();
        int[] to = scanners[j].getAbsolutePosition();
        int distance = abs(from[0] - to[0]) + abs(from[1] - to[1]) + abs(from[2] - to[2]);
        maxDistance = Math.max(maxDistance, distance);
      }
    }

    System.out.println("Part 2: " + maxDistance);
  }

  private static int[][] computeRotationMatrix(Map<Beacon, Beacon> matchingBeacons) {
    Iterator<Map.Entry<Beacon, Beacon>> iterator =  matchingBeacons.entrySet().iterator();
    Map.Entry<Beacon, Beacon> firstEntry = iterator.next();
    Beacon s1b1 = firstEntry.getKey();
    Beacon s2b1 = firstEntry.getValue();

    Map.Entry<Beacon, Beacon> secondEntry = iterator.next();
    Beacon s1b2 = secondEntry.getKey();
    Beacon s2b2 = secondEntry.getValue();

    // Positional diffs within scanner 1's frame of reference
    int s1XDiff = s1b2.getX() - s1b1.getX();
    int s1YDiff = s1b2.getY() - s1b1.getY();
    int s1ZDiff = s1b2.getZ() - s1b1.getZ();

    // Positional diffs within scanner 2's frame of reference
    int s2XDiff = s2b2.getX() - s2b1.getX();
    int s2YDiff = s2b2.getY() - s2b1.getY();
    int s2ZDiff = s2b2.getZ() - s2b1.getZ();

    // Use diffs to determine orientation
    int[] xRot;
    int[] yRot;
    int[] zRot;
    if (abs(s1XDiff) == abs(s2XDiff)) {
      xRot = new int[] { s1XDiff / s2XDiff, 0, 0 };
    } else if (abs(s1XDiff) == abs(s2YDiff)) {
      xRot = new int[] { 0, s1XDiff / s2YDiff, 0 };
    } else {
      xRot = new int[] { 0, 0,  s1XDiff / s2ZDiff };
    }

    if (abs(s1YDiff) == abs(s2YDiff)) {
      yRot = new int[] { 0, s1YDiff / s2YDiff, 0 };
    } else if (abs(s1YDiff) == abs(s2XDiff)) {
      yRot = new int[] { s1YDiff / s2XDiff, 0 , 0 };
    } else {
      yRot = new int[] { 0, 0,  s1YDiff / s2ZDiff };
    }

    if (abs(s1ZDiff) == abs(s2ZDiff)) {
      zRot = new int[] { 0, 0, s1ZDiff / s2ZDiff };
    } else if (abs(s1ZDiff) == abs(s2XDiff)) {
      zRot = new int[] { s1ZDiff / s2XDiff, 0 , 0 };
    } else {
      zRot = new int[] { 0, s1ZDiff / s2YDiff, 0 };
    }

    return new int[][] { xRot, yRot, zRot };
  }

  private static int[] computeTranslation(Map<Beacon, Beacon> matchingBeacons) {
    Map.Entry<Beacon, Beacon> match = matchingBeacons.entrySet().stream().findFirst().get();
    Beacon key = match.getKey();
    Beacon value = match.getValue();
    return new int[] { key.getX() - value.getX(), key.getY() - value.getY(), key.getZ() - value.getZ() };
  }

  private static void printMatchingBeacons(Map<Beacon, Beacon> matchingBeacons) {
    for (Map.Entry<Beacon, Beacon> matchingBeacon : matchingBeacons.entrySet()) {
      Beacon b1 = matchingBeacon.getKey();
      Beacon b2 = matchingBeacon.getValue();
      System.out.println(b1 + " -> " + b2);
    }

    System.out.println();
  }

  private static Scanner[] buildScanners() {
    String[] scannerStrings = InputLoader.load("/year2021/day19/input.txt")
        .split("\\n?--- scanner \\d+ ---\\n");

    Scanner[] scanners = new Scanner[scannerStrings.length - 1];
    for (int i = 1; i < scannerStrings.length; i++) {
      String[] beaconStrings = scannerStrings[i].split("\n");
      Beacon[] beacons = new Beacon[beaconStrings.length];
      for (int j = 0; j < beaconStrings.length; j++) {
        String[] coords = beaconStrings[j].split(",");
        Beacon beacon = new Beacon(parseInt(coords[0]), parseInt(coords[1]), parseInt(coords[2]));
        beacons[j] = beacon;
      }

      scanners[i - 1] = new Scanner(beacons);
    }

    return scanners;
  }

}
