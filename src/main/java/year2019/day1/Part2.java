package year2019.day1;

import util.InputLoader;

public class Part2 {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2019/day1/Input.txt")
        .split("\n");

    int solution = 0;
    for (String input : inputs) {
      solution += fuelRequired(Integer.parseInt(input));
    }

    System.out.println(solution);
  }

  private static int fuelRequired(int mass) {
    int fuelRequired = 0;
    int fuel;
    while ((fuel = convertToFuel(mass)) > 0) {
      fuelRequired += fuel;
      mass = fuel;
    }

    return fuelRequired;
  }

  private static int convertToFuel(int mass) {
    return (mass / 3) - 2;
  }

}
