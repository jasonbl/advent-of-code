package year2021.day21;

public class Die {

  private int numRolls;

  public int roll() {
    numRolls++;
    return ((numRolls - 1) % 100) + 1;
  }

  public int getNumRolls() {
    return numRolls;
  }

}
