package year2021.day14;

import util.InputLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class Solution {

  private static final Pattern PATTERN = Pattern.compile("^(\\w\\w) -> (\\w)$");

  public static void main(String[] args) {
    String[] parts = InputLoader.load("/year2021/day14/input.txt").split("\n\n");
    String template = parts[0];
    Map<String, List<String>> rules = buildRules(parts[1].split("\n"));

    System.out.println("Part 1: " + partOne(template, rules));
    System.out.println("Part 2: " + partTwo(template, rules));
  }

  private static long partOne(String template, Map<String, List<String>> rules) {
    return simulate(template, rules, 10);
  }

  private static long partTwo(String template, Map<String, List<String>> rules) {
    return simulate(template, rules, 40);
  }

  private static long simulate(String initialTemplateString, Map<String, List<String>> rules, int steps) {
    Map<String, Long> initialTemplate = buildTemplate(initialTemplateString);
    Map<String, Long> template = simulateSteps(initialTemplate, rules, steps);
    long[] counts = getCharCounts(template, initialTemplateString);
    return getAnswer(counts);
  }

  private static Map<String, Long> simulateSteps(Map<String, Long> template, Map<String, List<String>> rules,
      int steps) {
    for (int i = 1; i <= steps; i++) {
      template = simulateStep(template, rules);
    }

    return template;
  }

  private static Map<String, Long> simulateStep(Map<String, Long> template, Map<String, List<String>> rules) {
    Map<String, Long> newTemplate = new HashMap<>();
    for (Map.Entry<String, Long> entry : template.entrySet()) {
      rules.get(entry.getKey())
          .forEach((result) -> newTemplate.merge(result, entry.getValue(), Long::sum));
    }

    return newTemplate;
  }

  private static long getAnswer(long[] counts) {
    long max = Arrays.stream(counts).max().orElseThrow(RuntimeException::new);
    long min = Arrays.stream(counts).filter((val) -> val != 0).min().orElseThrow(RuntimeException::new);
    return max - min;
  }

  private static long[] getCharCounts(Map<String, Long> template, String initialTemplateString) {
    long[] counts = new long[26];
    for (Map.Entry<String, Long> entry : template.entrySet()) {
      for (char c : entry.getKey().toCharArray()) {
        counts[c - 'A'] += entry.getValue();
      }
    }

    char firstChar = initialTemplateString.charAt(0);
    char lastChar = initialTemplateString.charAt(initialTemplateString.length() - 1);
    counts[firstChar - 'A']--;
    counts[lastChar - 'A']--;
    for (int i = 0; i < counts.length; i++) {
      counts[i] /= 2;
    }

    counts[firstChar - 'A']++;
    counts[lastChar - 'A']++;
    return counts;
  }

  private static Map<String, Long> buildTemplate(String template) {
    Map<String, Long> pairCounts = new HashMap<>();
    for (int i = 0; i < template.length() - 1; i++) {
      String key = "" + template.charAt(i) + template.charAt(i + 1);
      pairCounts.merge(key, 1L, Long::sum);
    }

    return pairCounts;
  }

  private static Map<String, List<String>> buildRules(String[] rules) {
    Map<String, List<String>> rulesMap = new HashMap<>();
    for (String rule : rules) {
      Matcher matcher = PATTERN.matcher(rule);
      if (!matcher.matches()) {
        throw new RuntimeException("Illegal match");
      }

      String pair = matcher.group(1);
      String result = matcher.group(2);
      rulesMap.put(pair, asList(pair.charAt(0) + result, result + pair.charAt(1)));
    }

    return rulesMap;
  }
}
