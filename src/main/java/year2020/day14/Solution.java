package year2020.day14;

import util.InputLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern MEM_PATTERN = Pattern.compile("^mem\\[(\\d+)] = (\\d+)$");

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2020/day14/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(inputs));
    System.out.println("Part 2: " + partTwo(inputs));
  }

  private static long partOne(String[] inputs) {
    String mask = "";
    Map<Long, Long> memory = new HashMap<>();
    for (String input : inputs) {
      if (input.startsWith("mask")) {
        mask = input.substring(7);
        continue;
      }

      Matcher memMatcher = MEM_PATTERN.matcher(input);
      if (!memMatcher.matches()) {
        throw new IllegalArgumentException("Regex failed");
      }

      long memAddress = Long.parseLong(memMatcher.group(1));
      long value = Long.parseLong(memMatcher.group(2));
      long maskedValue = maskValue(value, mask);
      memory.put(memAddress, maskedValue);
    }

    return sumMemValues(memory);
  }

  private static long partTwo(String[] inputs) {
    String mask = "";
    Map<Long, Long> memory = new HashMap<>();
    for (String input : inputs) {
      if (input.startsWith("mask")) {
        mask = input.substring(7);
        continue;
      }

      Matcher memMatcher = MEM_PATTERN.matcher(input);
      if (!memMatcher.matches()) {
        throw new IllegalArgumentException("Regex failed");
      }

      long memAddress = Long.parseLong(memMatcher.group(1));
      long value = Long.parseLong(memMatcher.group(2));
      maskMemAddress(memAddress, mask)
          .forEach((maskedMemAddress) -> memory.put(maskedMemAddress, value));
    }

    return sumMemValues(memory);
  }

  private static long maskValue(long value, String mask) {
    char[] binaryValue = longToPaddedBinary(value, mask.length())
        .toCharArray();
    for (int i = 0; i < mask.length(); i++) {
      char bit = mask.charAt(i);
      if (bit == 'X') {
        continue;
      }

      binaryValue[i] = bit;
    }

    return binaryToLong(String.copyValueOf(binaryValue));
  }

  private static List<Long> maskMemAddress(long memAddress, String mask) {
    char[] binaryMemAddress = longToPaddedBinary(memAddress, mask.length())
        .toCharArray();
    List<String> seenMemAddresses = new ArrayList<>();
    seenMemAddresses.add("");
    for (int i = 0; i < mask.length(); i++) {
      char bit = mask.charAt(i);
      List<String> nextMemAddresses = new ArrayList<>();
      for (String seenMemAddress : seenMemAddresses) {
        if (bit == '0') {
          nextMemAddresses.add(seenMemAddress + binaryMemAddress[i]);
        } else if (bit == '1') {
          nextMemAddresses.add(seenMemAddress + '1');
        } else {
          nextMemAddresses.add(seenMemAddress + "0");
          nextMemAddresses.add(seenMemAddress + "1");
        }
      }

      seenMemAddresses = nextMemAddresses;
    }

    return seenMemAddresses.stream()
        .map(Solution::binaryToLong)
        .collect(Collectors.toList());
  }

  private static String longToPaddedBinary(long value, int desiredLength) {
    String binaryValue = Long.toBinaryString(value);
    StringBuilder pad = new StringBuilder();
    for (int i = 0; i < desiredLength - binaryValue.length(); i++) {
      pad.append('0');
    }

    return pad.append(binaryValue)
        .toString();
  }

  private static long binaryToLong(String binary) {
    return Long.parseLong(binary, 2);
  }

  private static long sumMemValues(Map<Long, Long> memory) {
    return memory.values()
        .stream()
        .mapToLong((val) -> val)
        .sum();
  }

}
