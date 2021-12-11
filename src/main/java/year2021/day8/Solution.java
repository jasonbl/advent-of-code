package year2021.day8;

import util.InputLoader;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Solution {

  private static final Pattern PATTERN = Pattern.compile("([a-g]+)");

  private static final Set<Character> ALL_LETTERS = new HashSet<>(asList('a', 'b', 'c', 'd', 'e', 'f', 'g'));

  private static final String[] REAL_DIGITS = {
      "abcefg",
      "cf",
      "acdeg",
      "acdfg",
      "bcdf",
      "abdfg",
      "abdefg",
      "acf",
      "abcdefg",
      "abcdfg",
  };

  private static final Map<String, Integer> REAL_DIGIT_MAP = buildRealDigitMap();

  private static final Map<Character, Integer> REAL_SEGMENT_COUNTS = getSegmentCounts(REAL_DIGITS);

  private static final Set<Integer> UNIQUE_SEGMENT_COUNTS = new HashSet<>(asList(2, 3, 4, 7));

  public static void main(String[] args) {
    String[] entries = InputLoader.load("/year2021/day8/input.txt").split("\n");
    String[][] digits = buildDigits(entries);
    System.out.println("Part 1: " + partOne(digits));
    System.out.println("Part 2: " + partTwo(digits));
  }

  private static int partOne(String[][] digits) {
    int uniqueDigitCount = 0;
    for (String[] entry : digits) {
      for (int i = 10; i < entry.length; i++) {
        if (UNIQUE_SEGMENT_COUNTS.contains(entry[i].length())) {
          uniqueDigitCount++;
        }
      }
    }

    return uniqueDigitCount;
  }

  private static int partTwo(String[][] digits) {
    int outputSum = 0;
    for (String[] entry : digits) {
      Map<Character, Character> segmentMap = computeSegmentMap(entry);
      Map<String, Integer> digitMap = computeDigitMap(entry, segmentMap);
      outputSum += computeOutput(entry, digitMap);
    }

    return outputSum;
  }

  private static Map<Character, Character> computeSegmentMap(String[] entry) {
    Map<Character, Set<Character>> possibleSegmentsByLength = computePossibleSegmentsByLength(entry);
    Map<Character, Set<Character>> possibleSegmentsByCount = computePossibleSegmentsByCount(entry);
    Map<Character, Set<Character>> mergedPossibleSegments = new HashMap<>();
    for (Map.Entry<Character, Set<Character>> possibleSegments : possibleSegmentsByCount.entrySet()) {
      Set<Character> intersection = new HashSet<>(possibleSegments.getValue());
      intersection.retainAll(possibleSegmentsByLength.get(possibleSegments.getKey()));
      mergedPossibleSegments.put(possibleSegments.getKey(), intersection);
    }

    reducePossibleSegments(mergedPossibleSegments);

    Map<Character, Character> finalMapping = new HashMap<>();
    for (Map.Entry<Character, Set<Character>> segments : mergedPossibleSegments.entrySet()) {
      if (segments.getValue().size() != 1) {
        throw new RuntimeException("Unknown segment mapping");
      }

      finalMapping.put(segments.getKey(), segments.getValue().stream().findFirst().get());
    }

    return finalMapping;
  }

  private static Map<Character, Set<Character>> computePossibleSegmentsByLength(String[] entry) {
    Map<Character, Set<Character>> possibleSegments = initPossibleSegments();
    for (int i = 0; i < 10; i++) {
      String digit = entry[i];
      Set<Character> possibleChars = Arrays.stream(REAL_DIGITS)
          .filter((realDigit) -> digit.length() == realDigit.length())
          .flatMap((realDigit) -> realDigit.chars().mapToObj((letter) -> (char) letter))
          .collect(Collectors.toSet());

      for (char c : digit.toCharArray()) {
        possibleSegments.get(c).retainAll(possibleChars);
      }
    }

    return possibleSegments;
  }

  private static Map<Character, Set<Character>> computePossibleSegmentsByCount(String[] entry) {
    Map<Character, Integer> segmentCounts = getSegmentCounts(entry);
    Map<Character, Set<Character>> possibleSegments = new HashMap<>();
    for (Map.Entry<Character, Integer> segmentCount : segmentCounts.entrySet()) {
      for (Map.Entry<Character, Integer> realSegmentCount : REAL_SEGMENT_COUNTS.entrySet()) {
        if (segmentCount.getValue().equals(realSegmentCount.getValue())) {
          possibleSegments.computeIfAbsent(segmentCount.getKey(), HashSet::new)
              .add(realSegmentCount.getKey());
        }
      }
    }

    return possibleSegments;
  }

  private static void reducePossibleSegments(Map<Character, Set<Character>> possibleSegments) {
    for (Map.Entry<Character, Set<Character>> segments : possibleSegments.entrySet()) {
      if (segments.getValue().size() == 1) {
        for (int letter = 'a'; letter <= 'g'; letter++) {
          if (letter == segments.getKey()) {
            continue;
          }

          possibleSegments.get((char) letter).removeAll(segments.getValue());
        }
      }
    }
  }

  private static Map<Character, Integer> getSegmentCounts(String[] entry) {
    Map<Character, Integer> segmentCounts = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      for (char c : entry[i].toCharArray()) {
        segmentCounts.merge(c, 1, Integer::sum);
      }
    }

    return segmentCounts;
  }

  private static Map<Character, Set<Character>> initPossibleSegments() {
    return ALL_LETTERS.stream()
        .collect(Collectors.toMap(Function.identity(), (letter) -> new HashSet<>(ALL_LETTERS)));
  }

  private static int computeOutput(String[] entry, Map<String, Integer> digitMap) {
    int output = 0;
    for (int i = 10; i < entry.length; i++) {
      output *= 10;
      output += digitMap.get(entry[i]);
    }

    return output;
  }

  private static Map<String, Integer> computeDigitMap(String[] entry, Map<Character, Character> segmentMap) {
    Map<String, Integer> digitMap = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      String digit = entry[i];
      StringBuilder realDigitBuilder = new StringBuilder();
      for (char c : digit.toCharArray()) {
        realDigitBuilder.append(segmentMap.get(c));
      }

      String realDigit = alphabetize(realDigitBuilder.toString());
      digitMap.put(digit, REAL_DIGIT_MAP.get(realDigit));
    }

    return digitMap;
  }

  private static String[][] buildDigits(String[] entries) {
    String[][] digits = new String[entries.length][];
    for (int i = 0; i < entries.length; i++) {
      digits[i] = getEntryDigits(entries[i]);
    }

    return digits;
  }

  private static String[] getEntryDigits(String entry) {
    Matcher matcher = PATTERN.matcher(entry);
    String[] digits = new String[14];
    for (int i = 0; i < digits.length; i++) {
      if (!matcher.find()) {
        throw new RuntimeException();
      }

      digits[i] = alphabetize(matcher.group(1));
    }

    return digits;
  }

  private static Map<String, Integer> buildRealDigitMap() {
    Map<String, Integer> map = new HashMap<>();
    for (int i = 0; i < REAL_DIGITS.length; i++) {
      map.put(REAL_DIGITS[i], i);
    }

    return map;
  }

  private static String alphabetize(String s) {
    char[] realDigitsChars = s.toCharArray();
    Arrays.sort(realDigitsChars);
    return new String(realDigitsChars);
  }
}
