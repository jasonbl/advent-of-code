package year2022.day9

import util.InputLoader

fun main() {
    val moves = InputLoader.load("/year2022/day9/input.txt").split("\n")
    println("Part 1: ${simulate(moves, 2)}")
    println("Part 2: ${simulate(moves, 10)}")
}

fun simulate(moves: List<String>, ropeSize: Int): Int {
    val rope = Rope(ropeSize)
    for (move in moves) {
        val parts = move.split(" ")
        rope.move(parts[0][0], parts[1].toInt())
    }

    return rope.getTailNumVisited()
}
