package year2024.day15

import util.InputLoader
import year2024.shared.Direction

fun main() {
  val inputs = InputLoader.load("/year2024/day15/input.txt")
    .split("\n\n")

  val grid = inputs[0].split("\n")
    .map { it.toCharArray() }

  val moves = inputs[1].filter { it != '\n' }
    .map { Direction.get(it) }

  var x = -1
  var y = -1
  grid.forEachIndexed { rowIndex, row ->
    row.forEachIndexed { colIndex, char ->
      if (char == '@') {
        x = colIndex
        y = rowIndex
      }
    }
  }

//  printGrid(grid)

  for (direction in moves) {
    var xCursor = x + direction.dx
    var yCursor = y + direction.dy
    while (grid[yCursor][xCursor] != '#' && grid[yCursor][xCursor] != '.') {
      // Search forward for the next empty space (if there is one), ignoring boxes
      xCursor += direction.dx
      yCursor += direction.dy
    }

    if (grid[yCursor][xCursor] == '.') {
      // Traverse backwards from the empty space, swapping values until we're back at the robot
      while (xCursor != x || yCursor != y) {
        val prevX = xCursor - direction.dx
        val prevY = yCursor - direction.dy

        swap(xCursor, yCursor, prevX, prevY, grid)

        xCursor = prevX
        yCursor = prevY
      }

      x += direction.dx
      y += direction.dy
    } else {
      // We've hit a wall, no moves to be made
    }

//    printGrid(grid)
  }

  println("Part 1: ${scoreGrid(grid)}")
}

private fun swap(x1: Int, y1: Int, x2: Int, y2: Int, grid: List<CharArray>) {
  val first = grid[y1][x1]
  val second = grid[y2][x2]
  grid[y2][x2] = first
  grid[y1][x1] = second
}

private fun printGrid(grid: List<CharArray>) {
  grid.forEach { println(it) }
  println()
}

private fun scoreGrid(grid: List<CharArray>): Long {
  var score = 0L
  grid.forEachIndexed { rowIndex, row ->
    row.forEachIndexed { colIndex, char ->
      if (char == 'O' || char == '[') {
        score += 100 * rowIndex + colIndex
      }
    }
  }

  return score
}
