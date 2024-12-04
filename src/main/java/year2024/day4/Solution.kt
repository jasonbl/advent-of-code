package year2024.day4

import util.InputLoader

fun main() {
  val grid = InputLoader.load("/year2024/day4/input.txt")
    .split("\n")
    .map { it.toCharArray() }
    .toTypedArray()

  println("Part 1: " + countXmas(grid))
  println("Part 2: " + countMasX(grid))
}

private fun countXmas(grid: Array<CharArray>): Int {
  var count = 0
  for (y in grid.indices) {
    for (x in grid[0].indices) {
      if (grid[y][x] == 'X') {
        count += hasXmas(grid, x, y, 0, 1).toInt() +
          hasXmas(grid, x, y, 1, 1).toInt() +
          hasXmas(grid, x, y, 1, 0).toInt() +
          hasXmas(grid, x, y, 1, -1).toInt() +
          hasXmas(grid, x, y, 0, -1).toInt() +
          hasXmas(grid, x, y, -1, -1).toInt() +
          hasXmas(grid, x, y, -1, 0).toInt() +
          hasXmas(grid, x, y, -1, 1).toInt()
      }
    }
  }

  return count
}

private fun hasXmas(grid: Array<CharArray>, x: Int, y: Int, delX: Int, delY: Int): Boolean {
  return isInGrid(grid, x + 3 * delX, y + 3 * delY) &&
    grid[y + delY][x + delX] == 'M' &&
    grid[y + 2 * delY][x + 2 * delX] == 'A' &&
    grid[y + 3 * delY][x + 3 * delX] == 'S'
}

private fun countMasX(grid: Array<CharArray>): Int {
  var count = 0
  for (y in grid.indices) {
    for (x in grid[0].indices) {
      if (grid[y][x] == 'A') {
        count += hasMasX(grid, x, y).toInt()
      }
    }
  }

  return count
}

private fun hasMasX(grid: Array<CharArray>, x: Int, y: Int): Boolean {
  val fitsInGrid = y >= 1 && y < grid.size - 1 && x >= 1 && x < grid[0].size - 1
  if (!fitsInGrid) {
    return false
  }

  val upLeft = grid[y + 1][x - 1]
  val upRight = grid[y + 1][x + 1]
  val downLeft = grid[y - 1][x - 1]
  val downRight = grid[y - 1][x + 1]
  val xList = listOf(upLeft, upRight, downLeft, downRight)

  return xList.count { it == 'M' } == 2 && xList.count { it == 'S'} == 2 && upLeft != downRight
}

private fun isInGrid(grid: Array<CharArray>, x: Int, y: Int): Boolean {
  return y in grid.indices && x in grid[y].indices
}

private fun Boolean.toInt(): Int {
  return if (this) 1 else 0
}
