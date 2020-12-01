package year2019.day13;

import year2019.intcode.InstructionProcessor;

import java.util.List;

import static java.lang.Math.toIntExact;

public class Game {

  private final InstructionProcessor processor;
  private GameObject[][] board;
  private long score;
  private int paddleX;
  private int ballX;

  private Game(long[] program) {
    this.processor = InstructionProcessor.loadProgram(program);
    this.score = 0;
  }

  public static Game init(long[] program) {
    return new Game(program);
  }

  public long play(boolean drawBoard) {
    processor.start();
    List<Long> initialState = processor.awaitNextOutputs();
    initBoard(initialState);
    drawBoard(drawBoard);

    while (!processor.isHalted()) {
      List<Long> updates;
      if (ballX < paddleX) {
        updates = processor.awaitNextOutputs(-1);
      } else if (ballX == paddleX) {
        updates = processor.awaitNextOutputs(0);
      } else {
        updates = processor.awaitNextOutputs(1);
      }

      updateBoard(updates);
      drawBoard(drawBoard);
    }

    return score;
  }

  private void updateBoard(List<Long> updates) {
    int size = updates.size();
    Long[] updateArray = updates.toArray(new Long[size]);
    for (int i = 0; i < updateArray.length / 3; i++) {
      int x = toIntExact(updateArray[3 * i]);
      if (x == -1) {
        this.score = updateArray[3 * i + 2];
        continue;
      }

      int y = toIntExact(updateArray[3 * i + 1]);
      GameObject newObject = new GameObject(x, y, toIntExact(updateArray[3 * i + 2]));
      board[y][x] = newObject;

      if (newObject.getSprite() == Sprite.PADDLE) {
        paddleX = newObject.getX();
      } else if (newObject.getSprite() == Sprite.BALL) {
        ballX = newObject.getX();
      }
    }
  }

  private void drawBoard(boolean drawBoard) {
    if (!drawBoard) {
      return;
    }

    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board[0].length; x++) {
        System.out.print(board[y][x].getSprite().getIcon());
      }
      System.out.println();
    }

    System.out.println("SCORE: " + score);
  }

  private void initBoard(List<Long> data) {
    int size = data.size();
    Long[] dataArray = data.toArray(new Long[size]);
    // -1 to ignore score
    GameObject[] gameObjects = new GameObject[dataArray.length / 3 - 1];
    int maxX = 0;
    int maxY = 0;
    for (int i = 0; i < gameObjects.length; i++) {
      int x = toIntExact(dataArray[3 * i]);
      if (x == -1) {
        this.score = dataArray[3 * i + 2];
        continue;
      }

      int y = toIntExact(dataArray[3 * i + 1]);

      maxX = Math.max(maxX, x);
      maxY = Math.max(maxY, y);
      gameObjects[i] = new GameObject(x, y, toIntExact(dataArray[3 * i + 2]));
    }

    board = new GameObject[maxY + 1][maxX + 1];
    for (GameObject gameObject : gameObjects) {
      board[gameObject.getY()][gameObject.getX()] = gameObject;

      if (gameObject.getSprite() == Sprite.PADDLE) {
        paddleX = gameObject.getX();
      } else if (gameObject.getSprite() == Sprite.BALL) {
        ballX = gameObject.getX();
      }
    }
  }

}
