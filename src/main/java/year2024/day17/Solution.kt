package year2024.day17

import util.InputLoader
import java.math.BigInteger

val numberRegex = Regex("(\\d+)")

fun main() {
  val inputParts = InputLoader.load("/year2024/day17/input.txt")
    .split("\n\n")

  val registers = numberRegex.findAll(inputParts[0])
    .map { it.groupValues[0].toInt() }
    .toList()

  val instructions = inputParts[1].substringAfter("Program: ")
    .split(",")
    .map { it.toInt() }

  println("Part 1: ${partOne(registers, instructions)}")
  println("Part 2: ${partTwo(instructions)}")
}

private fun partOne(registers: List<Int>, instructions: List<Int>): String {
  val registerArray = registers.map { it.toBigInteger() }
    .toTypedArray()
  return runProgram(registerArray, instructions).joinToString(",")
}

private fun partTwo(instructions: List<Int>): BigInteger {
  // Only 1024 unique bit sequences (first 10 bits) can impact a single output, so start with those possible solutions
  var possibleSolutions = (0 until 1024)
    .map { it.toBigInteger() }
    .toList()

  lateinit var actualSolutions: List<BigInteger>
  var shift = 10
  for (i in instructions.indices) {
    // Find all solutions that output the instruction at index i
    actualSolutions = findSolutions(possibleSolutions, instructions, i)

    val nextPossibleSolutions = ArrayList<BigInteger>()
    for (solution in actualSolutions) {
      // After every output, the value in register A is shifted right by 3 bits (divided by 8). Any valid solution
      // for output n will always work for output n + 1 as long as only the 3 bits before it are altered. Check
      // every possible combination of those 3 bits
      for (j in 0..7) {
        val nextPossibleSolution = (j.toBigInteger() shl shift) + solution
        nextPossibleSolutions.add(nextPossibleSolution)
      }
    }

    shift += 3
    possibleSolutions = nextPossibleSolutions
  }

  return actualSolutions.min()
}

private fun findSolutions(inputs: List<BigInteger>, instructions: List<Int>, targetIndex: Int): List<BigInteger> {
  val outputs = ArrayList<BigInteger>()
  for (input in inputs) {
    val output = runProgram(arrayOf(input, BigInteger.ZERO, BigInteger.ZERO), instructions)
    if (targetIndex in output.indices && output[targetIndex] == instructions[targetIndex].toBigInteger()) {
      outputs.add(input)
    }
  }

  return outputs
}

private fun runProgram(registers: Array<BigInteger>, instructions: List<Int>): List<BigInteger> {
  var instructionPtr = 0
  val outputs = ArrayList<BigInteger>()
  while (instructionPtr < instructions.size) {
    val instruction = instructions[instructionPtr]
    val operand = instructions[instructionPtr + 1]
    var instructionPtrJumped = false
    when (instruction) {
      0 -> registers[0] = registers[0] shr combo(operand, registers).toInt()
      1 -> registers[1] = registers[1] xor operand.toBigInteger()
      2 -> registers[1] = combo(operand, registers) % BigInteger.valueOf(8)
      3 -> {
        if (registers[0] != BigInteger.ZERO) {
          instructionPtr = operand
          instructionPtrJumped = true
        }
      }
      4 -> registers[1] = registers[1] xor registers[2]
      5 -> outputs.add(combo(operand, registers) % BigInteger.valueOf(8))
      6 -> registers[1] = registers[0] shr combo(operand, registers).toInt()
      7 -> registers[2] = registers[0] shr combo(operand, registers).toInt()
      else -> throw IllegalStateException()
    }

    if (!instructionPtrJumped) {
      instructionPtr += 2
    }
  }

  return outputs
}

/**
 * Program = 2,4,1,2,7,5,4,3,0,3,1,7,5,5,3,0
 */
private fun runProgram(registers: Array<BigInteger>): List<BigInteger> {
  // At most, the 10 least-significant bits of A affect the output (1024 possibilities)
  val outputs = ArrayList<BigInteger>()
  while (registers[0] != BigInteger.ZERO) {
    registers[1] = registers[0] and BigInteger.valueOf(7) // B = 0 -> 7
    registers[1] = registers[1] xor BigInteger.valueOf(2) // B = 0 -> 7 Flip the 2nd least bit
    registers[2] = registers[0] shr registers[1].toInt() // C = A >> 0-7 times.
    registers[1] = registers[1] xor registers[2] // B = C xor 0-7
    registers[0] = registers[0] shr 3 // Shift A right by 3 bits (divide by 8)
    registers[1] = registers[1] xor BigInteger.valueOf(7) // B = B ^ 111
    outputs.add(registers[1] and BigInteger.valueOf(7)) // Last 3 bits are output
  }

  return outputs
}

private fun combo(operand: Int, registers: Array<BigInteger>): BigInteger {
  return when (operand) {
    0, 1, 2, 3 -> operand.toBigInteger()
    4 -> registers[0]
    5 -> registers[1]
    6 -> registers[2]
    else -> throw IllegalStateException()
  }
}
