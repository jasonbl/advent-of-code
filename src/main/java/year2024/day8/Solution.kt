package year2024.day8

import com.google.common.math.IntMath.gcd
import util.InputLoader
import year2024.shared.Position
import kotlin.math.abs

fun main() {
  val grid = InputLoader.load("/year2024/day8/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  val antennae = HashMap<Char, ArrayList<Position>>()
  grid.forEachIndexed { rowIndex, row ->
    row.forEachIndexed { colIndex, col ->
      if (col != '.') {
        antennae.computeIfAbsent(col) { ArrayList() }
          .add(Position(colIndex, rowIndex))
      }
    }
  }

  println("Part 1: ${partOne(antennae, grid)}")
  println("Part 2: ${partTwo(antennae, grid)}")
}

private fun partOne(antennae: HashMap<Char, ArrayList<Position>>, grid: List<CharArray>): Int {
  return antennae.values.flatMap { computeAntiNodesPartOne(it, grid) }
    .toSet()
    .count()
}

private fun partTwo(antennae: HashMap<Char, ArrayList<Position>>, grid: List<CharArray>): Int {
  return antennae.values.flatMap { computeAntiNodesPartTwo(it, grid) }
    .toSet()
    .count()
}

private fun computeAntiNodesPartOne(antennaPositions: List<Position>, grid: List<CharArray>): List<Position> {
  val antiNodes = ArrayList<Position>()
  for (p1 in antennaPositions) {
    for (p2 in antennaPositions) {
      if (p1 == p2) {
        continue
      }

      val dx = p2.x - p1.x
      val dy = p2.y - p1.y
      val antiNode1 = Position(p1.x - dx, p1.y - dy)
      val antiNode2 = Position(p2.x + dx, p2.y + dy)
      if (isInGrid(antiNode1, grid)) antiNodes.add(antiNode1)
      if (isInGrid(antiNode2, grid)) antiNodes.add(antiNode2)
    }
  }

  return antiNodes
}

private fun computeAntiNodesPartTwo(antennaPositions: List<Position>, grid: List<CharArray>): List<Position> {
  val antiNodes = ArrayList<Position>()
  for (p1 in antennaPositions) {
    for (p2 in antennaPositions) {
      if (p1 == p2) {
        continue
      }

      val dx = p2.x - p1.x
      val dy = p2.y - p1.y
      val gcd = gcd(abs(dx), abs(dy))
      val unitDx = dx / gcd
      val unitDy = dy / gcd

      var currP1 = p1
      var currP2 = p2
      while (isInGrid(currP1, grid) || isInGrid(currP2, grid)) {
        if (isInGrid(currP1, grid)) antiNodes.add(currP1)
        if (isInGrid(currP2, grid)) antiNodes.add(currP2)

        currP1 = Position(currP1.x - unitDx, currP1.y - unitDy)
        currP2 = Position(currP2.x + unitDx, currP2.y + unitDy)
      }
    }
  }

  return antiNodes
}

private fun isInGrid(position: Position, grid: List<CharArray>): Boolean {
  return position.y in grid.indices && position.x in grid[position.y].indices
}
