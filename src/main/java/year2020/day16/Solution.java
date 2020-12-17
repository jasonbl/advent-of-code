package year2020.day16;

import util.InputLoader;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern RULE_PATTERN = Pattern.compile("^(.+): (\\d+)-(\\d+) or (\\d+)-(\\d+)$");

  public static void main(String[] args) {
    String[] sections = InputLoader.load("/year2020/day16/input.txt").split("\n\n");
    MatchResult[] rules = parseRules(sections[0].split("\n"));
    String myTicket = sections[1].split("\n")[1];
    int[][] nearbyTickets = parseNearbyTickets(sections[2]);
    System.out.println("Part 1: " + partOne(rules, nearbyTickets));
    System.out.println("Part 2: " + partTwo(rules, myTicket, nearbyTickets));
  }

  private static int partOne(MatchResult[] rules, int[][] nearbyTickets) {
    int invalidValuesSum = 0;
    for (int[] nearbyTicket : nearbyTickets) {
      int invalidCount = countInvalidValues(rules, nearbyTicket);
      if (invalidCount != -1) {
        invalidValuesSum += invalidCount;
      }
    }

    return invalidValuesSum;
  }

  private static long partTwo(MatchResult[] rules, String myTicket, int[][] nearbyTickets) {
    List<int[]> validNearbyTickets = Arrays.stream(nearbyTickets)
        .filter((nearbyTicket) -> countInvalidValues(rules, nearbyTicket) == -1)
        .collect(Collectors.toList());

    Map<Integer, Set<String>> possibleArrangements = computePossibleRuleArrangements(rules, validNearbyTickets);

    // Sort the entries of the map based on the number of rules that can be applied to
    // a column (least to greatest)
    ArrayList<Map.Entry<Integer, Set<String>>> ruleEntries = possibleArrangements.entrySet()
        .stream()
        .sorted(new RuleEntryComparator())
        .collect(Collectors.toCollection(ArrayList::new));

    String[] arrangedRules = new String[rules.length];
    for (int i = 0; i < ruleEntries.size(); i++) {
      Map.Entry<Integer, Set<String>> ruleEntry = ruleEntries.get(i);
      // There should be exactly one rule remaining in the set
      String rule = ruleEntry.getValue().stream().findFirst()
          .orElseThrow(() -> new RuntimeException("No rule found"));
      arrangedRules[ruleEntry.getKey()] = rule;
      for (int j = i + 1; j < ruleEntries.size(); j++) {
        ruleEntries.get(j).getValue().remove(rule);
      }
    }

    int[] myTicketVals = parseTicket(myTicket);
    long answer = 1;
    for (int i = 0; i < arrangedRules.length; i++) {
      if (arrangedRules[i].startsWith("departure")) {
        answer *= myTicketVals[i];
      }
    }

    return answer;
  }

  private static Map<Integer, Set<String>> computePossibleRuleArrangements(MatchResult[] rules,
      List<int[]> nearbyTickets) {
    Map<Integer, Set<String>> colToPossibleRules = new HashMap<>();
    for (int col = 0; col < rules.length; col++) {
      for (MatchResult rule : rules) {
        boolean isValidRule = true;
        for (int[] nearbyTicket : nearbyTickets) {
          if (!isValidValue(rule, nearbyTicket[col])) {
            isValidRule = false;
            break;
          }
        }

        if (isValidRule) {
          colToPossibleRules.computeIfAbsent(col, (val) -> new HashSet<>())
              .add(rule.group(1));
        }
      }
    }

    return colToPossibleRules;
  }

  /**
   * Counts the sum of invalid values for this ticket. Returns -1 if no invalid values could be found.
   * This needs to return -1 instead of 0 in a "none found" case because 0 itself can be an invalid value
   */
  private static int countInvalidValues(MatchResult[] rules, int[] nearbyTicket) {
    int invalidValuesSum = 0;
    boolean foundInvalidValue = false;
    for (int value : nearbyTicket) {
      boolean hasValidRule = false;
      for (MatchResult rule : rules) {
        if (isValidValue(rule, value)) {
          hasValidRule = true;
          break;
        }
      }

      if (!hasValidRule) {
        invalidValuesSum += value;
        foundInvalidValue = true;
      }
    }

    return foundInvalidValue ? invalidValuesSum : -1;
  }

  private static boolean isValidValue(MatchResult rule, int value) {
    int lowOne = Integer.parseInt(rule.group(2));
    int highOne = Integer.parseInt(rule.group(3));
    int lowTwo = Integer.parseInt(rule.group(4));
    int highTwo = Integer.parseInt(rule.group(5));
    return (value >= lowOne && value <= highOne) || (value >= lowTwo && value <= highTwo);
  }

  private static MatchResult[] parseRules(String[] rulesSection) {
    MatchResult[] rules = new MatchResult[rulesSection.length];
    for (int i = 0; i < rulesSection.length; i++) {
      Matcher matcher = RULE_PATTERN.matcher(rulesSection[i]);
      if (!matcher.matches()) {
        throw new RuntimeException("Regex failed");
      }

      rules[i] = matcher.toMatchResult();
    }

    return rules;
  }

  private static int[][] parseNearbyTickets(String nearbyTicketsSection) {
    String[] nearbyTicketStrings = nearbyTicketsSection.substring(16).split("\n");
    int[][] nearbyTickets = new int[nearbyTicketStrings.length][];
    for (int i = 0; i < nearbyTicketStrings.length; i++) {
      nearbyTickets[i] = parseTicket(nearbyTicketStrings[i]);
    }

    return nearbyTickets;
  }

  private static int[] parseTicket(String ticket) {
    return Arrays.stream(ticket.split(","))
        .mapToInt(Integer::parseInt)
        .toArray();
  }

  private static class RuleEntryComparator implements Comparator<Map.Entry<Integer, Set<String>>> {

    @Override
    public int compare(Map.Entry<Integer, Set<String>> firstEntry,
                       Map.Entry<Integer, Set<String>> secondEntry) {
      return Integer.compare(firstEntry.getValue().size(), secondEntry.getValue().size());
    }

  }

}
