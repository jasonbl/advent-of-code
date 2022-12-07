package year2022.day5

import util.InputLoader

val INSTRUCTION_REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")

fun main() {
    val inputs = InputLoader.load("/year2022/day5/input.txt").split("\n\n")
    val stackInput = inputs[0].split("\n")
    val instructions = inputs[1].split("\n")
    println("Part 1: ${run(stackInput, instructions, true)}")
    println("Part 2: ${run(stackInput, instructions, false)}")
}

fun run(stackInput: List<String>, instructions: List<String>, isPartOne: Boolean): String {
    val stacks = buildStacks(stackInput)
    runInstructions(stacks, instructions, isPartOne)
    return buildOutput(stacks)
}

fun buildStacks(stackInput: List<String>): List<ArrayDeque<Char>> {
    val numStacks = stackInput[0].length / 4 + 1
    val stacks : List<ArrayDeque<Char>> = (1..numStacks).map { ArrayDeque() }
    for (i in 0 until stackInput.lastIndex) {
        for (j in stacks.indices) {
            val char = stackInput[i][4 * j + 1]
            if (char != ' ') {
                stacks[j].addLast(char)
            }
        }
    }

    return stacks
}

fun runInstructions(stacks: List<ArrayDeque<Char>>, instructions: List<String>, isPartOne: Boolean) {
    for (instruction in instructions) {
        val matchResult = INSTRUCTION_REGEX.matchEntire(instruction)!!
        val numToMove = matchResult.groupValues[1].toInt()
        val sourceIndex = matchResult.groupValues[2].toInt() - 1
        val destinationIndex = matchResult.groupValues[3].toInt() - 1
        val toAdd = (0 until numToMove).map { stacks[sourceIndex].removeFirst() }
        for (i in 0 until numToMove) {
            if (isPartOne) {
                stacks[destinationIndex].addFirst(toAdd[i])
            } else {
                stacks[destinationIndex].addFirst(toAdd[numToMove - i - 1])
            }
        }
    }
}

fun buildOutput(stacks: List<ArrayDeque<Char>>): String {
    return stacks.filter { it.isNotEmpty() }
        .map { it.first() }
        .toCharArray()
        .concatToString()
}