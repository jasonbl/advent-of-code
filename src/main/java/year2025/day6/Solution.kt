package year2025.day6

import util.InputLoader

fun main() {
  println("Part 1: ${part1()}")
  println("Part 2: ${part2()}")
}

private fun part1(): Long {
  val input = InputLoader.load("/year2025/day6/input.txt").split("\n")
  val numbers = input.subList(0, input.size - 1)
    .map {
      "\\d+".toRegex().findAll(it)
        .map { it.groupValues[0].toLong() }.toList()
    }

  val ops = input[input.size - 1].replace(" ", "").toCharArray()

  var total = 0L
  for (col in ops.indices) {
    var colTotal = numbers[0][col]
    for (row in 1 until numbers.size) {
      if (ops[col] == '*') {
        colTotal *= numbers[row][col]
      } else {
        colTotal += numbers[row][col]
      }
    }

    total += colTotal
  }

  return total
}

private fun part2(): Long {
  // Make sure trailing space isn't removed from the input by the IDE
  val input = InputLoader.load("/year2025/day6/input.txt").split("\n")
  val ops = input[input.size - 1]

  var problemStart = 0
  var curr = 1
  var answer = 0L
  while (true) {
    while (curr < ops.length && ops[curr] == ' ') {
      curr++
    }

    if (curr == ops.length) {
      answer += solveProblem(input, problemStart, curr - 1)
      break
    } else {
      answer += solveProblem(input, problemStart, curr - 2)
      problemStart = curr++
    }
  }

  return answer
}

private fun solveProblem(input: List<String>, problemStart: Int, problemEnd: Int): Long {
  val op = input[input.size - 1][problemStart]
  val numbers = arrayListOf<Long>()
  for (col in problemEnd downTo problemStart) {
    var number = 0L
    for (row in 0 until input.size - 1) {
      if (input[row][col] != ' ') {
        number *= 10
        number += input[row][col].digitToInt()
      }
    }
    
    numbers.add(number)
  }
  
  return if (op == '*') numbers.reduce { acc, i -> acc * i } else numbers.sum()
}
