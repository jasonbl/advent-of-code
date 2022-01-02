package year2021.day21;

import util.InputLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

  private static final Pattern PATTERN = Pattern.compile(": (\\d+)");

  private static final int[] QUANTUM_DIE_OUTCOMES = computeQuantumDieOutcomes();

  public static void main(String[] args) {
    String input = InputLoader.load("/year2021/day21/input.txt");
    Matcher matcher = PATTERN.matcher(input);
    matcher.find();
    int playerOnePosition = Integer.parseInt(matcher.group(1));
    matcher.find();
    int playerTwoPosition =  Integer.parseInt(matcher.group(1));

    System.out.println("Part 1: " + partOne(playerOnePosition, playerTwoPosition));
    System.out.println("Part 2: " + partTwo(playerOnePosition, playerTwoPosition));
  }

  private static int partOne(int playerOnePosition, int playerTwoPosition) {
    return simulatePartOne(playerOnePosition, playerTwoPosition, 1000);
  }

  private static int simulatePartOne(int playerOnePosition, int playerTwoPosition, int scoreToWin) {
    int playerOneScore = 0;
    int playerTwoScore = 0;
    Die die = new Die();

    while (true) {
      playerOnePosition = movePlayer(playerOnePosition, die);
      playerOneScore += playerOnePosition;
      if (playerOneScore >= scoreToWin) {
        return playerTwoScore * die.getNumRolls();
      }

      playerTwoPosition = movePlayer(playerTwoPosition, die);
      playerTwoScore += playerTwoPosition;
      if (playerTwoScore >= scoreToWin) {
        return playerOneScore * die.getNumRolls();
      }
    }
  }

  private static long partTwo(int playerOnePosition, int playerTwoPosition) {
    GameState initialState = new GameState(playerOnePosition, playerTwoPosition, 21, 21, true);
    long[] wins = simulatePartTwo(initialState, new HashMap<>());
    return Math.max(wins[0], wins[1]);
  }

  private static long[] simulatePartTwo(GameState state, Map<GameState, long[]> results) {
    int playerOnePosition = state.getPlayerOnePosition();
    int playerTwoPosition = state.getPlayerTwoPosition();
    int playerOneScoreRemaining = state.getPlayerOneScoreRemaining();
    int playerTwoScoreRemaining = state.getPlayerTwoScoreRemaining();
    boolean isPlayerOnesTurn = state.isPlayerOnesTurn();

    if (playerOneScoreRemaining == 0) {
      return new long[] { 1, 0 };
    } else if (playerTwoScoreRemaining == 0) {
      return new long[] { 0, 1 };
    } else if (results.containsKey(state)) {
      return results.get(state);
    }

    long[] totalWins = new long[2];
    for (int roll : QUANTUM_DIE_OUTCOMES) {
      long[] wins;
      if (isPlayerOnesTurn) {
        int nextPlayerOnePosition = movePlayer(playerOnePosition, roll);
        int nextPlayerOneScoreRemaining = Math.max(playerOneScoreRemaining - nextPlayerOnePosition, 0);
        GameState nextGameState = new GameState(nextPlayerOnePosition, playerTwoPosition,
            nextPlayerOneScoreRemaining, playerTwoScoreRemaining, false);
        wins = simulatePartTwo(nextGameState, results);
      } else {
        int nextPlayerTwoPosition = movePlayer(playerTwoPosition, roll);
        int nextPlayerTwoScoreRemaining = Math.max(playerTwoScoreRemaining - nextPlayerTwoPosition, 0);
        GameState nextGameState = new GameState(playerOnePosition, nextPlayerTwoPosition,
            playerOneScoreRemaining, nextPlayerTwoScoreRemaining, true);
        wins = simulatePartTwo(nextGameState, results);
      }

      totalWins[0] += wins[0];
      totalWins[1] += wins[1];
    }

    results.put(state, totalWins);
    return totalWins;
  }

  private static int movePlayer(int position, Die die) {
    return movePlayer(position, die.roll() + die.roll() + die.roll());
  }

  private static int movePlayer(int position, int numSpaces) {
    position += numSpaces;
    return ((position - 1) % 10) + 1;
  }

  private static int[] computeQuantumDieOutcomes() {
    int[] die = new int[] { 1, 2, 3 };
    int[] possibilities = new int[27];
    int index = 0;
    for (int roll1 : die) {
      for (int roll2 : die) {
        for (int roll3 : die) {
          possibilities[index++] = roll1 + roll2 + roll3;
        }
      }
    }

    return possibilities;
  }

}
