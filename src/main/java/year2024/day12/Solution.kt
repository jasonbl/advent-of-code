package year2024.day12

import util.InputLoader
import year2024.shared.Position

fun main() {
  val grid = InputLoader.load("/year2024/day12/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  val regions = getRegions(grid)
  val partOne = regions.sumOf { it.getPricePartOne() }
  val partTwo = regions.sumOf { it.getPricePartTwo() }

  println("Part 1: $partOne")
  println("Part 2: $partTwo")
}

private fun getRegions(grid: List<CharArray>): List<Region> {
  val visited = HashSet<Position>()
  val regions = ArrayList<Region>()
  for (y in grid.indices) {
    for (x in grid[y].indices) {
      if (visited.contains(Position(x, y))) {
        continue
      }

      val region = HashSet<Position>()
      val type = grid[y][x]
      fillRegion(type, Position(x, y), grid, region)
      visited.addAll(region)
      regions.add(Region(type, region))
    }
  }

  return regions
}

private fun fillRegion(type: Char, pos: Position, grid: List<CharArray>, region: HashSet<Position>) {
  if (!isInGrid(pos, grid) || region.contains(pos) || grid[pos.y][pos.x] != type) {
    return
  }

  region.add(pos)

  fillRegion(type, pos.up(), grid, region)
  fillRegion(type, pos.down(), grid, region)
  fillRegion(type, pos.left(), grid, region)
  fillRegion(type, pos.right(), grid, region)
}

private fun isInGrid(position: Position, grid: List<CharArray>): Boolean {
  return position.y in grid.indices && position.x in grid[position.y].indices
}

private data class Region(val type: Char, val positions: Set<Position>) {

  /*
   * Need to represent space between plants as TWO coordinates because fences next to plants that are diagonal
   * from each other should not be considered contiguous. For example, the fences next to the interior A's
   * below aren't contiguous:
   *
   *  AAAA
   *  AABA
   *  ABAA
   *  AAAA
   *
   * A fence is represented as being either horizontal or vertical and starting at a specific Position
   */
  val horizontalFences = positions.flatMap {
    val fences = ArrayList<Position>()
    if (!positions.contains(it.up())) fences.add(Position(it.x, 2 * it.y))
    if (!positions.contains(it.down())) fences.add(Position(it.x, 2 * it.y + 1))
    fences
  }

  val verticalFences = positions.flatMap {
    val fences = ArrayList<Position>()
    if (!positions.contains(it.left())) fences.add(Position(2 * it.x, it.y))
    if (!positions.contains(it.right())) fences.add(Position(2 * it.x + 1, it.y))
    fences
  }

  val corners = positions.flatMap {
    val currCorners = ArrayList<Position>()
    if (!positions.contains(it.up()) && !positions.contains(it.right()) ||
      (positions.containsAll(listOf(it.up(), it.right())) && !positions.contains(it.up().right()))) {
      // Check up right corner
      currCorners.add(Position(2 * it.x + 1, 2 * it.y))
    }

    if (!positions.contains(it.up()) && !positions.contains(it.left()) ||
      (positions.containsAll(listOf(it.up(), it.left())) && !positions.contains(it.up().left()))) {
      // Check up left corner
      currCorners.add(Position(2 * it.x, 2 * it.y))
    }

    if (!positions.contains(it.down()) && !positions.contains(it.right()) ||
      (positions.containsAll(listOf(it.down(), it.right())) && !positions.contains(it.down().right()))) {
      // Check down right corner
      currCorners.add(Position(2 * it.x + 1, 2 * it.y + 1))
    }

    if (!positions.contains(it.down()) && !positions.contains(it.left()) ||
      (positions.containsAll(listOf(it.down(), it.left())) && !positions.contains(it.down().left()))) {
      // Check down left corner
      currCorners.add(Position(2 * it.x, 2 * it.y + 1))
    }

    currCorners
  }

  fun getPricePartOne(): Int {
    return getArea() * getNumFences()
  }

  fun getPricePartTwo(): Int {
    return getArea() * getNumSides()
  }

  private fun getArea(): Int {
    return positions.size
  }

  private fun getNumFences(): Int {
    return horizontalFences.size + verticalFences.size
  }

  private fun getNumSides(): Int {
    return corners.size
  }
}
