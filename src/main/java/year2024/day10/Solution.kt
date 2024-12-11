package year2024.day10

import util.InputLoader

fun main() {
  val grid = InputLoader.load("/year2024/day10/input.txt")
    .split("\n")
    .map { it.toCharArray().map { value -> "$value".toInt() } }

  println("Part 1: ${computeTrailheadScoreSum(grid)}")
  println("Part 2: ${countTrails(grid)}")
}

private fun computeTrailheadScoreSum(grid: List<List<Int>>): Int {
  return findTrailheads(grid)
    .sumOf { computeTrailheadScore(it, grid) }
}

private fun computeTrailheadScore(trailhead: Position, grid: List<List<Int>>): Int {
  return findTrails(trailhead.x, trailhead.y, 0, grid)
    .map { trail -> trail.last() }
    .toSet()
    .count()
}

private fun countTrails(grid: List<List<Int>>): Int {
  return findTrailheads(grid)
    .flatMap { findTrails(it.x, it.y, 0, grid) }
    .count()
}

private fun findTrailheads(grid: List<List<Int>>): List<Position> {
  return grid.flatMapIndexed { rowIndex, row ->
    row.mapIndexedNotNull { colIndex, col ->
      if (col == 0) Position(colIndex, rowIndex) else null
    }
  }
}

private fun findTrails(x: Int, y: Int, height: Int, grid: List<List<Int>>): List<ArrayDeque<Position>> {
  if (!isInGrid(x, y, grid) || grid[y][x] != height) {
    return emptyList()
  } else if (height == 9) {
    val deque = ArrayDeque<Position>()
    deque.add(Position(x, y))
    return listOf(deque)
  }

  val peaks = ArrayList<ArrayDeque<Position>>()
  peaks.addAll(findTrails(x + 1, y, height + 1, grid))
  peaks.addAll(findTrails(x - 1, y, height + 1, grid))
  peaks.addAll(findTrails(x, y - 1, height + 1, grid))
  peaks.addAll(findTrails(x, y + 1, height + 1, grid))

  peaks.forEach { it.addFirst(Position(x, y)) }

  return peaks
}

private fun isInGrid(x: Int, y: Int, grid: List<List<Int>>): Boolean {
  return y in grid.indices && x in grid[y].indices
}

data class Position(val x: Int, val y: Int)