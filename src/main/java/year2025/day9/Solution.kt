package year2025.day9

import util.InputLoader
import kotlin.math.abs
import kotlin.math.max

fun main() {
  val points = InputLoader.load("/year2025/day9/input.txt")
    .split("\n")
    .map {
      val coords = it.split(",")
      Point(coords[0].toInt(), coords[1].toInt())
    }

  var part1 = 0L
  for (i in points.indices) {
    for (j in i + 1 until points.size) {
      val xDiff = abs(points[i].x - points[j].x + 1)
      val yDiff = abs(points[i].y - points[j].y + 1)
      part1 = max(part1, xDiff.toLong() * yDiff)
    }
  }

  println("Part 1: $part1")
}

private data class Point(val x: Int, val y: Int)
