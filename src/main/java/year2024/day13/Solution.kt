package year2024.day13

import util.InputLoader

private val numberRegex = Regex("(\\d+)")

fun main() {
  val machines = InputLoader.load("/year2024/day13/input.txt")
    .split("\n\n")
    .map {
      val machineValues = numberRegex.findAll(it)
        .map { matchResults -> matchResults.groupValues[0].toLong() }
        .toList()

      Machine(machineValues)
    }

  val partOne = machines.sumOf { solveMachine(it, false) }
  val partTwo = machines.sumOf { solveMachine(it, true) }

  println("Part 1: $partOne")
  println("Part 2: $partTwo")
}

private fun solveMachine(machine: Machine, isPart2: Boolean): Long {
  val n = if (isPart2) 10000000000000 + machine.n else machine.n
  val m = if (isPart2) 10000000000000 + machine.m else machine.m

  val determinant = machine.a * machine.d - machine.b * machine.c
  val xNumerator = machine.d * n - machine.c * m
  val yNumerator = machine.a * m - machine.b * n

  return if (xNumerator % determinant != 0L || yNumerator % determinant != 0L) {
    0
  } else {
    3 * xNumerator / determinant + yNumerator / determinant
  }
}

private class Machine(values: List<Long>) {
  val a = values[0]
  val b = values[1]
  val c = values[2]
  val d = values[3]
  val n = values[4]
  val m = values[5]
}
