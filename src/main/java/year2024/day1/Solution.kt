package year2024.day1

import util.InputLoader
import kotlin.math.abs

val INPUT_REGEX = Regex("(\\d+)\\s+(\\d+)")

fun main() {
  val inputs = InputLoader.load("/year2024/day1/input.txt")
    .split("\n")

  val matchResults = inputs.map { INPUT_REGEX.matchEntire(it)!! }

  val leftList = Array(inputs.size) { matchResults[it].groupValues[1].toInt() }
  val rightList = Array(inputs.size) { matchResults[it].groupValues[2].toInt() }

  println("Part 1: " + partOne(leftList, rightList))
  println("Part 2: " + partTwo(leftList, rightList))
}

private fun partOne(leftList: Array<Int>, rightList: Array<Int>): Int {
  leftList.sort()
  rightList.sort()

  var diff = 0
  for (i in leftList.indices) {
    diff += abs(rightList[i] - leftList[i])
  }

  return diff
}

private fun partTwo(list1: Array<Int>, list2: Array<Int>): Int {
  val counts = HashMap<Int, Int>()
  list2.forEach {
    val currCount = counts.getOrDefault(it, 0)
    counts[it] = currCount + 1
  }

  return list1.sumOf { it * counts.getOrDefault(it, 0) }
}
