package year2024.day7

import util.InputLoader

fun main() {
  val equations = InputLoader.load("/year2024/day7/input.txt")
    .split("\n")
    .associate {
      val parts = it.split(": ")
      parts[0].toLong() to parts[1].split(" ").map { value -> value.toLong() }
    }

  val part1 = equations.filter { isValid(it.key, it.value, false) }.entries
    .sumOf { it.key }

  val part2 = equations.filter { isValid(it.key, it.value, true) }.entries
    .sumOf { it.key }

  println("Part 1: $part1")
  println("Part 1: $part2")
}

private fun isValid(target: Long, values: List<Long>, includeOr: Boolean): Boolean {
  return isValid(values[0], target, 1, values, includeOr)
}

private fun isValid(currentValue: Long, target: Long, index: Int, values: List<Long>, includeOr: Boolean): Boolean {
  if (currentValue == target && index == values.size) {
    return true
  } else if (currentValue > target || index >= values.size) {
    return false
  }

  return isValid(currentValue + values[index], target, index + 1, values, includeOr) ||
    isValid(currentValue * values[index], target, index + 1, values, includeOr) ||
    (includeOr && isValid("$currentValue${values[index]}".toLong(), target, index + 1, values, true))
}
