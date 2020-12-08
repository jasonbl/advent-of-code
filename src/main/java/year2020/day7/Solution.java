package year2020.day7;

import util.InputLoader;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

  private static final Pattern PATTERN = Pattern.compile("^([0-9]+) (.*) bag.*$");

  public static void main(String[] args) {
    String[] rules = InputLoader.load("/year2020/day7/input.txt").split("\n");
    List<BagRule> bagRules = buildRules(rules);
    System.out.println("Part 1: " + partOne(bagRules));
    System.out.println("Part 2: " + partTwo(bagRules));
  }

  private static int partOne(List<BagRule> rules) {
    return computeUniqueContainingTypes(rules, "shiny gold");
  }

  private static int partTwo(List<BagRule> rules) {
    return computeNumBagsInside(rules, "shiny gold");
  }

  private static int computeUniqueContainingTypes(List<BagRule> rules, String type) {
    // Maps from bag types to the bag types that contain that type
    Map<String, Set<String>> typeToContainingTypes = new HashMap<>();
    for (BagRule rule : rules) {
      for (String childRule : rule.getChildBagIds()) {
        typeToContainingTypes.computeIfAbsent(childRule, (val) -> new HashSet<>())
            .add(rule.getId());
      }
    }

    Set<String> uniqueContainingTypes = new HashSet<>();
    Set<String> containingTypes = typeToContainingTypes.getOrDefault(type, new HashSet<>());
    while (!containingTypes.isEmpty()) {
      uniqueContainingTypes.addAll(containingTypes);
      containingTypes = containingTypes.stream()
          .map(typeToContainingTypes::get)
          .filter(Objects::nonNull)
          .flatMap(Set::stream)
          .collect(Collectors.toSet());
    }

    return uniqueContainingTypes.size();
  }

  private static int computeNumBagsInside(List<BagRule> rules, String type) {
    Map<String, BagRule> rulesMap = rules.stream()
        .collect(Collectors.toMap(BagRule::getId, Function.identity()));
    return computeNumBagsInside(rulesMap, type);
  }

  private static int computeNumBagsInside(Map<String, BagRule> rules, String type) {
    int numBags = 0;
    for (BagQuantity childBag : rules.get(type).getChildBags()) {
      numBags += childBag.getQuantity() * (1 + computeNumBagsInside(rules, childBag.getBagId()));
    }

    return numBags;
  }

  private static List<BagRule> buildRules(String[] rules) {
    List<BagRule> bagRules = new ArrayList<>();
    for (String rule : rules) {
      String[] ruleHalves = rule.split(" contain ");
      String leftHalf = ruleHalves[0].substring(0, ruleHalves[0].indexOf(" bags"));
      BagRule bagRule = new BagRule(leftHalf);
      String[] rightHalfParts = ruleHalves[1].split(", ");
      for (String rightHalfPart : rightHalfParts) {
        Matcher matcher = PATTERN.matcher(rightHalfPart);
        if (matcher.matches()) {
          int bagQuantity = Integer.parseInt(matcher.group(1));
          String bagType = matcher.group(2);
          bagRule.addChildBag(new BagQuantity(bagType, bagQuantity));
        }
      }

      bagRules.add(bagRule);
    }

    return bagRules;
  }

}
