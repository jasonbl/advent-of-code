package year2019.day11;

public enum Direction {

  UP,
  LEFT,
  DOWN,
  RIGHT;

  public static Direction rotate(Direction currentDirection, Direction directionToTurn) {
    int newDirectionIndex = currentDirection.ordinal();
    if (directionToTurn == LEFT) {
      newDirectionIndex++;
    } else if (directionToTurn == RIGHT) {
      newDirectionIndex--;
    } else {
      throw new IllegalArgumentException("Invalid direction to turn: " + directionToTurn);
    }

    Direction[] directions = Direction.values();
    if (newDirectionIndex >= directions.length) {
      newDirectionIndex = 0;
    } else if (newDirectionIndex < 0) {
      newDirectionIndex = directions.length - 1;
    }

    return directions[newDirectionIndex];
  }

}
