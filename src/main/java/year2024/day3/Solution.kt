package year2024.day3

import util.InputLoader

const val MUL_REGEX_PATTERN = "mul\\((\\d{1,3}),(\\d{1,3})\\)"
val MUL_REGEX = Regex(MUL_REGEX_PATTERN)

const val DO_REGEX_PATTERN = "do\\(\\)"
val DO_REGEX = Regex(DO_REGEX_PATTERN)

const val DONT_REGEX_PATTERN = "don\'t\\(\\)"
val DONT_REGEX = Regex(DONT_REGEX_PATTERN)

val FULL_REGEX = Regex("($MUL_REGEX_PATTERN)|($DO_REGEX_PATTERN)|($DONT_REGEX_PATTERN)")

fun main() {
  val input = InputLoader.load("/year2024/day3/input.txt")

  println("Part 1: " + partOne(input))
  println("Part 2: " + partTwo(input))
}

private fun partOne(input: String): Int {
  return MUL_REGEX.findAll(input)
    .sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
}

private fun partTwo(input: String): Int {
  var doMul = true
  var sum = 0
  for (match in FULL_REGEX.findAll(input)) {
    if (DO_REGEX.matches(match.value)) {
      doMul = true
    } else if (DONT_REGEX.matches(match.value)) {
      doMul = false
    } else {
      if (doMul) {
        sum += match.groupValues[2].toInt() * match.groupValues[3].toInt()
      }
    }
  }

  return sum
}
