package year2021.day25;

public class SeaCucumbers {

  private char[][] grid;

  public SeaCucumbers(char[][] grid) {
    this.grid = grid;
  }

  public int move() {
    // printGrid();
    int numMoved;
    int numSteps = 0;
    do {
      numMoved = moveRight() + moveDown();
      numSteps++;
      // printGrid();
    } while (numMoved != 0);

    return numSteps;
  }

  private int moveRight() {
    char[][] nextGrid = new char[grid.length][grid[0].length];
    int numMoved = 0;
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[y].length; x++) {
        int rightIndex = (x + 1) % grid[y].length;
        if (grid[y][x] == '>' && grid[y][rightIndex] == '.') {
          nextGrid[y][x] = '.';
          nextGrid[y][rightIndex] = '>';
          numMoved++;
          x++;
        } else {
          nextGrid[y][x] = grid[y][x];
        }
      }
    }

    this.grid = nextGrid;
    return numMoved;
  }

  private int moveDown() {
    char[][] nextGrid = new char[grid.length][grid[0].length];
    int numMoved = 0;
    for (int x = 0; x < grid[0].length; x++) {
      for (int y = 0; y < grid.length; y++) {
        int downIndex = (y + 1) % grid.length;
        if (grid[y][x] == 'v' && grid[downIndex][x] == '.') {
          nextGrid[y][x] = '.';
          nextGrid[downIndex][x] = 'v';
          numMoved++;
          y++;
        } else {
          nextGrid[y][x] = grid[y][x];
        }
      }
    }

    this.grid = nextGrid;
    return numMoved;
  }

  private void printGrid() {
    for (char[] row : grid) {
      for (char c : row) {
        System.out.print(c);
      }
      System.out.println();
    }
    System.out.println();
  }

}
