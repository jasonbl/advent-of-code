package year2021.day21;

import java.util.Objects;

public class GameState {

  private final int playerOnePosition;
  private final int playerTwoPosition;
  private final int playerOneScoreRemaining;
  private final int playerTwoScoreRemaining;
  private final boolean playerOnesTurn;

  public GameState(int playerOnePosition, int playerTwoPosition, int playerOneScoreRemaining,
      int playerTwoScoreRemaining, boolean playerOnesTurn) {
    this.playerOnePosition = playerOnePosition;
    this.playerTwoPosition = playerTwoPosition;
    this.playerOneScoreRemaining = playerOneScoreRemaining;
    this.playerTwoScoreRemaining = playerTwoScoreRemaining;
    this.playerOnesTurn = playerOnesTurn;
  }

  public int getPlayerOnePosition() {
    return playerOnePosition;
  }

  public int getPlayerTwoPosition() {
    return playerTwoPosition;
  }

  public int getPlayerOneScoreRemaining() {
    return playerOneScoreRemaining;
  }

  public int getPlayerTwoScoreRemaining() {
    return playerTwoScoreRemaining;
  }

  public boolean isPlayerOnesTurn() {
    return playerOnesTurn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameState gameState = (GameState) o;
    return playerOnePosition == gameState.playerOnePosition
        && playerTwoPosition == gameState.playerTwoPosition
        && playerOneScoreRemaining == gameState.playerOneScoreRemaining
        && playerTwoScoreRemaining == gameState.playerTwoScoreRemaining
        && playerOnesTurn == gameState.playerOnesTurn;
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerOnePosition, playerTwoPosition, playerOneScoreRemaining, playerTwoScoreRemaining,
        playerOnesTurn);
  }
}
