package year2022.day2

import util.InputLoader

val YOUR_HAND_TO_VALUE = mapOf("A" to 0, "B" to 1, "C" to 2)
val MY_HAND_TO_VALUE = mapOf("X" to 0, "Y" to 1, "Z" to 2)

fun main() {
    val rounds = InputLoader.load("/year2022/day2/input.txt").split("\n")
    println("Part 1: ${part1(rounds)}")
    println("Part 2: ${part2(rounds)}")
}

fun part1(rounds: List<String>): Int {
    var score = 0
    for (round in rounds) {
        val hands = round.split(" ")
        val yourValue = YOUR_HAND_TO_VALUE[hands[0]]!!
        val myValue = MY_HAND_TO_VALUE[hands[1]]!!
        score += when (yourValue) {
            myValue -> myValue + 4
            (myValue + 1) % 3 -> myValue + 1
            else -> myValue + 7
        }
    }

    return score
}

fun part2(rounds: List<String>): Int {
    var score = 0
    for (round in rounds) {
        val hands = round.split(" ")
        val yourValue = YOUR_HAND_TO_VALUE[hands[0]]!!
        score += when (hands[1]) {
            "X" -> (yourValue + 2) % 3 + 1
            "Y" -> yourValue + 4
            else -> (yourValue + 1) % 3 + 7
        }
    }

    return score
}