package year2025.day3

import util.InputLoader

fun main() {
  val input = InputLoader.load("/year2025/day3/input.txt")
    .split("\n")
    .map { it.toCharArray().map { it.digitToInt() } }

  println("Part 1: " + input.sumOf { getMaxJoltage(it, 2) })
  println("Part 2: " + input.sumOf { getMaxJoltage(it, 12) })
}

private fun getMaxJoltage(batteries: List<Int>, numDigits: Int): Long {
  var i = 0
  var joltage = 0L
  for (digit in 0 until numDigits) {
    for (j in i..batteries.size - numDigits + digit) {
      if (batteries[j] > batteries[i]) {
        i = j
      }
    }

    joltage *= 10
    joltage += batteries[i++]
  }

  return joltage
}
