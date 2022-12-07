package year2022.day6

import util.InputLoader

fun main() {
    val input = InputLoader.load("/year2022/day6/input.txt")
    println("Part 1: ${findMarkerEnd(input, 4)}")
    println("Part 2: ${findMarkerEnd(input, 14)}")
}

// Technically the "right" way to do this (no revisiting previously seen characters), though a much
// cleaner way is just to convert the deque to a set on every iteration and check the size
fun findMarkerEnd(input: String, markerSize: Int): Int {
    val deque = ArrayDeque<Char>()
    val map = mutableMapOf<Char, Int>()
    for (i in input.indices) {
        if (map.size == markerSize) {
            return i
        }

        val char = input[i]
        if (deque.size == markerSize) {
            val removed = deque.removeFirst()
            when (map[removed]) {
                1 -> map.remove(removed)
                else -> map[removed] = map[removed]!! - 1
            }
        }

        deque.addLast(char)
        map.merge(char, 1, Int::plus)
    }

    return -1
}