package year2025.day1

import util.InputLoader

fun main() {
  val input = InputLoader.load("/year2025/day1/input.txt")
    .split("\n")

  var position = 50
  var part1 = 0
  var part2 = 0
  for (s in input) {
    val direction = if (s[0] == 'L') -1 else 1
    var amount = s.substring(1).toInt()
    part2 += amount / 100
    amount %= 100

    val newPosition = position + direction * amount
    if (position != 0 && newPosition !in 1 until 100) {
      part2 += 1
    }

    position = rotate(position, direction, amount)
    if (position == 0) {
      part1++
    }
  }

  println("Part 1: $part1")
  println("Part 2: $part2")
}

private fun rotate(position: Int, direction: Int, amount: Int): Int {
  return (100 + position + direction * amount) % 100
}
