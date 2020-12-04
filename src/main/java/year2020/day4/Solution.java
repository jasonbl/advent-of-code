package year2020.day4;

import util.InputLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Solution {

  public static void main(String[] args) {
    String[] passports = InputLoader.load("/year2020/day4/input.txt").split("\n\n");
    System.out.println("Part 1: " + partOne(passports));
    System.out.println("Part 2: " + partTwo(passports));
  }

  private static int partOne(String[] passports) {
    int validPassports = 0;
    for (String passport : passports) {
      String[] fields = passport.split("\\s");
      Set<String> fieldKeys = new HashSet<>();
      for (String field : fields) {
        fieldKeys.add(field.split(":")[0]);
      }

      if (hasRequiredFields(fieldKeys)) {
        validPassports++;
      }
    }

    return validPassports;
  }

  private static int partTwo(String[] passports) {
    int validPassports = 0;
    for (String passport : passports) {
      String[] fields = passport.split("\\s");
      Set<String> fieldKeys = new HashSet<>();
      boolean hasInvalidField = false;
      for (String field : fields) {
        String[] splitField = field.split(":");
        String key = splitField[0];
        String value = splitField[1];
        if (!isValidField(key, value)) {
          hasInvalidField = true;
          break;
        }

        fieldKeys.add(key);
      }

      if (!hasInvalidField && hasRequiredFields(fieldKeys)) {
        validPassports++;
      }
    }

    return validPassports;
  }

  private static boolean isValidField(String key, String value) {
    int intValue;
    Pattern pattern;
    switch (key) {
      case "byr":
        intValue = Integer.parseInt(value);
        return intValue >= 1920 && intValue <= 2002;
      case "iyr":
        intValue = Integer.parseInt(value);
        return intValue >= 2010 && intValue <= 2020;
      case "eyr":
        intValue = Integer.parseInt(value);
        return intValue >= 2020 && intValue <= 2030;
      case "hgt":
        if (value.endsWith("cm") || value.endsWith("in")) {
          intValue = Integer.parseInt(value.substring(0, value.length() - 2));
          if (value.endsWith("cm")) {
            return intValue >= 150 && intValue <= 193;
          } else {
            return intValue >= 59 && intValue <= 76;
          }
        } else {
          return false;
        }
      case "hcl":
        pattern = Pattern.compile("^#[a-f0-9]{6}$");
        return pattern.matcher(value).matches();
      case "ecl":
        pattern = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");
        return pattern.matcher(value).matches();
      case "pid":
        pattern = Pattern.compile("^[0-9]{9}$");
        return pattern.matcher(value).matches();
      case "cid":
        return true;
      default:
        return false;
    }
  }

  private static boolean hasRequiredFields(Set<String> keys) {
    return keys.size() == 8 || (keys.size() == 7 && !keys.contains("cid"));
  }

}
