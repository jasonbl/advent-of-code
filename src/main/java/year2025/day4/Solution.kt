package year2025.day4

import util.InputLoader

fun main() {
  val grid = InputLoader.load("/year2025/day4/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  println("Part 1: ${part1(grid)}")
  println("Part 2: ${part2(grid)}")
}

private fun part1(grid: List<CharArray>): Int {
  var count = 0
  for (row in grid.indices) {
    for (col in grid[row].indices) {
      if (grid[row][col] == '@' && canRemove(grid, row, col)) {
        count++
      }
    }
  }

  return count
}

private fun part2(grid: List<CharArray>): Int {
  var removedCount = 0
  do {
    var removed = false
    for (row in grid.indices) {
      for (col in grid[row].indices) {
        if (grid[row][col] == '@' && canRemove(grid, row, col)) {
          grid[row][col] = '.'
          removedCount++
          removed = true
        }
      }
    }
  } while (removed)

  return removedCount
}

private fun canRemove(grid: List<CharArray>, row: Int, col: Int): Boolean {
  return countNeighbors(grid, row, col) < 4
}

private fun countNeighbors(grid: List<CharArray>, row: Int, col: Int): Int {
  val neighbors = listOf(
    row - 1 to col - 1,
    row - 1 to col,
    row - 1 to col + 1,
    row to col - 1,
    row to col + 1,
    row + 1 to col - 1,
    row + 1 to col,
    row + 1 to col + 1
  )

  return neighbors.filter { it.first >= 0 && it.first < grid.size && it.second >= 0 && it.second < grid[0].size }
    .count { grid[it.first][it.second] == '@' }
}
