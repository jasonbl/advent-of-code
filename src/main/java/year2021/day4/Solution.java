package year2021.day4;

import util.InputLoader;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Solution {

  public static void main(String[] args) {
    String[] inputs = InputLoader.load("/year2021/day4/input.txt").split("\n\n");

    int[] calledNumbers = Arrays.stream(inputs[0].split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    BingoBoard[] bingoBoards = new BingoBoard[inputs.length - 1];
    for (int i = 1; i < inputs.length; i++) {
      bingoBoards[i - 1] = buildBingoBoard(inputs[i]);
    }

    Deque<BingoBoard> winners = computeWinners(calledNumbers, bingoBoards);

    System.out.println("Part 1: " + winners.getFirst().computeScore());
    System.out.println("Part 2: " + winners.getLast().computeScore());
  }

  private static Deque<BingoBoard> computeWinners(int[] calledNumbers, BingoBoard[] bingoBoards) {
    Deque<BingoBoard> winners = new ArrayDeque<>();
    for (int calledNumber : calledNumbers) {
      for (BingoBoard bingoBoard : bingoBoards) {
        if (!bingoBoard.hasWon() && bingoBoard.markNumber(calledNumber)) {
          winners.addLast(bingoBoard);
        }
      }
    }

    return winners;
  }

  private static BingoBoard buildBingoBoard(String board) {
    String[] rows = board.split("\n");
    int[][] numbers = new int[rows.length][rows.length];
    for (int row = 0; row < rows.length; row++) {
      String[] cols = rows[row].trim().split(" +");
      for (int col = 0; col < rows.length; col++) {
        numbers[row][col] = Integer.parseInt(cols[col]);
      }
    }

    return new BingoBoard(numbers);
  }

}
