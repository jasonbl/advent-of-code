package year2020.day11;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String input = InputLoader.load("/year2020/day11/input.txt");
    char[][] seats = buildSeats(input);
    System.out.println("Part 1: " + partOne(seats));
    System.out.println("Part 2: " + partTwo(seats));
  }

  private static int partOne(char[][] seats) {
    int seatsChanged;
    do {
      seatsChanged = 0;
      char[][] nextSeats = new char[seats.length][seats[0].length];
      for (int row = 0; row < seats.length; row++) {
        for (int col = 0; col < seats[row].length; col++) {
          int occupiedAdjacent = countOccupiedAdjacent(seats, row, col);
          if (isEmpty(seats, row, col) && occupiedAdjacent == 0) {
            nextSeats[row][col] = '#';
            seatsChanged++;
          } else if (isOccupied(seats, row, col) && occupiedAdjacent >= 4) {
            nextSeats[row][col] = 'L';
            seatsChanged++;
          } else {
            nextSeats[row][col] = seats[row][col];
          }
        }
      }

      seats = nextSeats;
    } while (seatsChanged > 0);

    return countOccupied(seats);
  }

  private static int partTwo(char[][] seats) {
    int seatsChanged;
    do {
      seatsChanged = 0;
      char[][] nextSeats = new char[seats.length][seats[0].length];
      for (int row = 0; row < seats.length; row++) {
        for (int col = 0; col < seats[row].length; col++) {
          int occupiedVisibleAdjacent = countOccupiedVisibleAdjacent(seats, row, col);
          if (isEmpty(seats, row, col) && occupiedVisibleAdjacent == 0) {
            nextSeats[row][col] = '#';
            seatsChanged++;
          } else if (isOccupied(seats, row, col) && occupiedVisibleAdjacent >= 5) {
            nextSeats[row][col] = 'L';
            seatsChanged++;
          } else {
            nextSeats[row][col] = seats[row][col];
          }
        }
      }

      seats = nextSeats;
    } while (seatsChanged > 0);

    return countOccupied(seats);
  }

  private static int countOccupiedAdjacent(char[][] seats, int row, int col) {
    int[] rows = { row - 1, row, row + 1 };
    int[] cols = { col - 1, col, col + 1};
    int occupied = 0;
    for (int adjRow : rows) {
      for (int adjCol : cols) {
        if (adjRow == row && adjCol == col) {
          continue;
        }

        if (isOccupied(seats, adjRow, adjCol)) {
          occupied++;
        }
      }
    }

    return occupied;
  }

  private static int countOccupiedVisibleAdjacent(char[][] seats, int row, int col) {
    int[] slopes = { 0, 1, -1 };
    int occupied = 0;
    for (int delX : slopes) {
      for (int delY : slopes) {
        if (delX == 0 && delY == 0) {
          continue;
        }

        if (hasOccupiedOnSlope(seats, row, col, delX, delY)) {
          occupied++;
        }
      }
    }

    return occupied;
  }

  private static boolean hasOccupiedOnSlope(char[][] seats, int row, int col, int delRow, int delCol) {
    int currRow = row + delRow;
    int currCol = col + delCol;
    while (isPositionValid(seats, currRow, currCol)) {
      if (isOccupied(seats, currRow, currCol)) {
        return true;
      } else if (isEmpty(seats, currRow, currCol)) {
        return false;
      }

      currRow += delRow;
      currCol += delCol;
    }

    return false;
  }

  private static boolean isOccupied(char[][] seats, int row, int col) {
    return isPositionValid(seats, row, col) && seats[row][col] == '#';
  }

  private static boolean isEmpty(char[][] seats, int row, int col) {
    return isPositionValid(seats, row, col) && seats[row][col] == 'L';
  }

  private static boolean isPositionValid(char[][] seats, int row, int col) {
    return row >= 0 && row < seats.length && col >= 0 && col < seats[0].length;
  }

  private static int countOccupied(char[][] seats) {
    int occupied = 0;
    for (char[] row : seats) {
      for (char c : row) {
        if (c == '#') {
          occupied++;
        }
      }
    }

    return occupied;
  }

  private static char[][] buildSeats(String input) {
    String[] rows = input.split("\n");
    char[][] seats = new char[rows.length][rows[0].length()];
    for (int i = 0; i < rows.length; i++) {
      for (int j = 0; j < rows[i].length(); j++) {
        seats[i][j] = rows[i].charAt(j);
      }
    }

    return seats;
  }

}
