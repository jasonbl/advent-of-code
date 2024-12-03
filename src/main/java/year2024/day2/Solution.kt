package year2024.day2

import util.InputLoader

fun main() {
  val reports = InputLoader.load("/year2024/day2/input.txt")
    .split("\n")
    .map { it.split(" ").map { value -> value.toInt() } }

  println("Part 1: " + partOne(reports))
  println("Part 2: " + partTwo(reports))
}

private fun partOne(reports: List<List<Int>>): Int {
  return reports.count { isSafe(it) }
}

private fun partTwo(reports: List<List<Int>>): Int {
  return reports.count { isSafeWithTolerance(it) }
}

private fun isSafe(report: List<Int>): Boolean {
  val direction = if (report[1] > report[0]) 1 else -1

  return (0 until report.size - 1)
    .all { isSafeHelper(report, direction, it, it + 1) }
}

private fun isSafeWithTolerance(report: List<Int>): Boolean {
  for (skipIndex in report.indices) {
    val copy = ArrayList(report)
    copy.removeAt(skipIndex)
    if (isSafe(copy)) {
      return true
    }
  }

  return false
}

private fun isSafeHelper(report: List<Int>, direction: Int, index1: Int, index2: Int): Boolean {
  val diff = direction * (report[index2] - report[index1])
  return diff in 1..3
}
