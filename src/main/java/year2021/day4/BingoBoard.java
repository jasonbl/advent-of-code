package year2021.day4;

public class BingoBoard {

  private final int[][] numbers;

  private final boolean[][] marked;

  private boolean hasWon;

  private int winningNumber;

  public BingoBoard(int[][] numbers) {
    this.numbers = numbers;
    this.marked = new boolean[numbers.length][numbers[0].length];
  }

  public boolean markNumber(int number) {
    if (hasWon) {
      throw new RuntimeException("Board has already won");
    }

    for (int row = 0; row < numbers.length; row++) {
      for (int col = 0; col < numbers[0].length; col++) {
        if (numbers[row][col] == number) {
          marked[row][col] = true;

          if (checkWinningRow(row) || checkWinningCol(col)) {
            hasWon = true;
            winningNumber = number;
            return true;
          }
        }
      }
    }

    return false;
  }

  public boolean hasWon() {
    return hasWon;
  }

  public int computeScore() {
    return hasWon ? getUnmarkedSum() * winningNumber : -1;
  }

  private int getUnmarkedSum() {
    int sum = 0;
    for (int row = 0; row < marked.length; row++) {
      for (int col = 0; col < marked[0].length; col++) {
        if (!marked[row][col]) {
          sum += numbers[row][col];
        }
      }
    }

    return sum;
  }

  private boolean checkWinningRow(int row) {
    for (int col = 0; col < marked[0].length; col++) {
      if (!marked[row][col]) {
        return false;
      }
    }

    return true;
  }

  private boolean checkWinningCol(int col) {
    for (int row = 0; row < marked.length; row++) {
      if (!marked[row][col]) {
        return false;
      }
    }

    return true;
  }

}
