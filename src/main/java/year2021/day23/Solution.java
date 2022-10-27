package year2021.day23;

public class Solution {

  public static void main(String[] args) {
    System.out.println("Part 1: " + new AmphipodOrganizer(initPartOne()).organize());
    System.out.println("Part 2: " + new AmphipodOrganizer(initPartTwo()).organize());
  }

  private static Burrow initPartOne() {
    return new Burrow(
        new String[][] {
            { "C", "C" },
            { "B", "D" },
            { "A", "A" },
            { "D", "B" },
        }
    );
  }

  private static Burrow initPartTwo() {
    return new Burrow(
        new String[][] {
            { "C", "D", "D", "C" },
            { "B", "C", "B", "D" },
            { "A", "B", "A", "A" },
            { "D", "A", "C", "B" },
        }
    );
  }

}
