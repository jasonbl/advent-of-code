package year2024.day6

import util.InputLoader
import year2024.shared.Direction
import year2024.shared.Direction.*
import year2024.shared.Position

private val directions = listOf(UP, RIGHT, DOWN, LEFT)
private val directionToCharMap = Direction.values()
  .associateWith { it.char }

fun main() {
  val grid = InputLoader.load("/year2024/day6/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  var x = -1
  var y = -1
  grid.forEachIndexed { rowIndex, row ->
    row.forEachIndexed { colIndex, col ->
      if (col == UP.char) {
        x = colIndex
        y = rowIndex
        grid[y][x] = '.'
      }
    }
  }

  val visited = HashMap<Position, HashSet<Direction>>()
  var direction = UP
  var obstructions = 0
  while (true) {
    // For debugging purposes only
    printGrid(grid)

    visited.computeIfAbsent(Position(x, y)) { HashSet() }
      .add(direction)

    val nextX = x + direction.dx
    val nextY = y + direction.dy
    if (!isInGrid(nextX, nextY, grid)) {
      break
    }

    if (grid[nextY][nextX] == '#') {
      direction = direction.clockwise()
    } else {
      // For debugging purposes only
      grid[y][x] = directionToCharMap[direction]!!

      if (!visited.contains(Position(nextX, nextY))) {
        grid[nextY][nextX] = '#'
        if (hasLoop(x, y, direction.clockwise(), visited, grid)) {
          obstructions++
        }
        grid[nextY][nextX] = '.'
      }

      x = nextX
      y = nextY
    }
  }

  println("Part 1: ${visited.size}")
  println("Part 2: $obstructions")
}

private fun hasLoop(
  initX: Int,
  initY: Int,
  initDirection: Direction,
  initVisited: Map<Position, HashSet<Direction>>,
  grid: List<CharArray>
): Boolean {
  val visited = initVisited.mapValues { HashSet(it.value) }.toMutableMap()

  var x = initX
  var y = initY
  var direction = initDirection
  while (true) {
    val currPosition = Position(x, y)
    if (visited.contains(currPosition) && visited[currPosition]!!.contains(direction)) {
      return true
    }

    visited.computeIfAbsent(currPosition) { HashSet() }
      .add(direction)

    val nextX = x + direction.dx
    val nextY = y + direction.dy
    if (!isInGrid(nextX, nextY, grid)) {
      return false
    }

    if (grid[nextY][nextX] == '#') {
      direction = direction.clockwise()
    } else {
      x = nextX
      y = nextY
    }
  }
}

private fun isInGrid(x: Int, y: Int, grid: List<CharArray>): Boolean {
  return y in grid.indices && x in grid[y].indices
}

private fun printGrid(grid: List<CharArray>) {
  grid.forEach { println(it) }
  println()
}

private fun nextDirectionIndex(directionIndex: Int): Int {
  return (directionIndex + 1) % directions.size
}
