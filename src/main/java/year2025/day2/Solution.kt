package year2025.day2

import util.InputLoader
import java.util.regex.Pattern

fun main() {
  val input = InputLoader.load("/year2025/day2/input.txt")
    .split(",")
    .map { it.split("-").map { it.toLong() } }

  println(input.sumOf { countInvalid(it[0], it[1], ::isInvalidPartOne) })
  println(input.sumOf { countInvalid(it[0], it[1], ::isInvalidPartTwo) })
}

private fun countInvalid(min: Long, max: Long, isInvalid: (Long) -> Boolean): Long {
  return (min..max)
    .sumOf { if (isInvalid(it)) it else 0L }
}

private fun isInvalidPartOne(value: Long): Boolean {
  val s = "" + value
  if (s.length % 2 == 1) return false

  for (i in 0 until s.length / 2) {
    if (s[i] != s[s.length / 2 + i]) {
      return false
    }
  }

  return true
}


private val PATTERN = Pattern.compile("(\\d+)\\1+")

private fun isInvalidPartTwo(value: Long): Boolean {
  return PATTERN.matcher("" + value)
    .matches()
}
