package year2025.day7

import util.InputLoader

fun main() {
  val grid = InputLoader.load("/year2025/day7/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  var start = 0
  for (i in grid.indices) {
    if (grid[0][i] == 'S') {
      start = i
      break
    }
  }

  val part2 = fireBeam(grid, 1, start)

  println("Part 1: ${splitterToTimelineCount.size}")
  println("Part 2: $part2")
}

val splitterToTimelineCount = hashMapOf<Int, Long>()

// Return number of timelines
private fun fireBeam(grid: List<CharArray>, startRow: Int, startCol: Int): Long {
  var row = startRow
  while (row < grid.size && grid[row][startCol] == '.') {
    row++
  }

  if (row >= grid.size) {
    return 1
  }

  val splitterId = grid[0].size * row + startCol
  if (splitterToTimelineCount.contains(splitterId)) {
    return splitterToTimelineCount[splitterId]!!
  }

  val timelines = fireBeam(grid, row, startCol - 1) + fireBeam(grid, row, startCol + 1)
  splitterToTimelineCount[splitterId] = timelines
  return timelines
}
