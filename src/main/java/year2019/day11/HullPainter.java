package year2019.day11;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static year2019.day11.Direction.*;

public class HullPainter {

  // Odd number so that robot starts exactly in the middle
  private static final int HULL_SIZE = 101;

  private static final int BLACK = 0;
  private static final int WHITE = 1;

  private final InstructionProcessor processor;
  private final boolean[][] hull;

  private int x;
  private int y;
  private Direction direction;
  private Set<Point> panelsPainted;

  public HullPainter(long[] program) {
    this.processor = InstructionProcessor.loadProgram(program);
    this.hull = new boolean[HULL_SIZE][HULL_SIZE];
    this.x = HULL_SIZE / 2;
    this.y = HULL_SIZE / 2;
    this.direction = UP;
    this.panelsPainted = new HashSet<>();
  }

  public int paint() {
    // For part 2
    hull[y][x] = true;

    Future<?> processorTask = processor.start();
    while (!processorTask.isDone()) {
      int currentPaint = hull[y][x] ? WHITE : BLACK;
      processor.submitInput(currentPaint);
      long color;
      long directionToRotate;
      try {
       color = processor.awaitOutput();
       directionToRotate = processor.awaitOutput();
      } catch (TimeoutException e) {
        break;
      }

      paintCurrentPanel(color);
      rotate(directionToRotate);
      moveForward();
    }

    printHull();
    return panelsPainted.size();
  }

  private void paintCurrentPanel(long color) {
    if (color == WHITE) {
      hull[y][x] = true;
    } else if (color == BLACK) {
      hull[y][x] = false;
    } else {
      throw new IllegalArgumentException("Unexpected color: " + color);
    }

    panelsPainted.add(new Point(x, y));
  }

  private void rotate(long directionToRotate) {
    if (directionToRotate == 0) {
      this.direction = Direction.rotate(this.direction, LEFT);
    } else {
      this.direction = Direction.rotate(this.direction, RIGHT);
    }
  }

  private void moveForward() {
    switch (this.direction) {
      case UP:
        y--;
        break;
      case LEFT:
        x--;
        break;
      case DOWN:
        y++;
        break;
      case RIGHT:
        x++;
        break;
      default:
        throw new IllegalArgumentException("Unexpected direction: " + this.direction);
    }
  }

  private void printHull() {
    for (int y = 0; y < hull.length; y++) {
      for (int x = 0; x < hull[0].length; x++) {
        if (hull[y][x]) {
          System.out.print("X");
        } else {
          System.out.print(" ");
        }
      }
      System.out.println();
    }
  }

}
