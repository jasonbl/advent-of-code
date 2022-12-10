package year2022.day10

import util.InputLoader

fun main() {
    val instructions = InputLoader.load("/year2022/day10/input.txt").split("\n")
        .toCollection(ArrayDeque())

    var x = 1
    var toAdd: Int? = null
    var cycles = 1
    var signalStrength = 0
    while (cycles <= 240) {
        val crtPosition = (cycles - 1) % 40
        if ((cycles - 20) % 40 == 0) {
            signalStrength += cycles * x
        } else if (crtPosition == 0) {
            println()
        }

        if ((x-1..x+1).contains(crtPosition)) {
            print("X")
        } else {
            print(" ")
        }

        if (toAdd != null) {
            x += toAdd
            toAdd = null
            cycles++
            continue
        }

        val parts = instructions.removeFirst().split(" ")
        if (parts[0] == "addx") {
            toAdd = parts[1].toInt()
        }

        cycles++
    }

    println("\n\nPart 1: $signalStrength")
}