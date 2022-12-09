package year2022.day9

import kotlin.math.abs
import kotlin.math.sign

class Rope(val size: Int) {
    private val head = Head()
    private val tails = mutableListOf<Tail>()

    init {
        var prev: Knot = head
        for (i in 2..size) {
            val tail = Tail(prev)
            tails.add(tail)
            prev = tail
        }
    }

    fun move(direction: Char, steps: Int) {
        for (i in 1..steps) {
            head.move(direction)
            tails.forEach { it.move() }
        }
    }

    fun getTailNumVisited(): Int {
        return tails.last().visited.size
    }

}

private abstract class Knot {
    var x = 0
    var y = 0

    fun isTouching(knot: Knot): Boolean {
        val xDist = abs(x - knot.x)
        val yDist = abs(y - knot.y)
        return xDist <= 1 && yDist <= 1
    }
}

private class Head : Knot() {
    fun move(direction: Char) {
        when (direction) {
            'U' -> y++
            'D' -> y--
            'L' -> x--
            'R' -> x++
        }
    }
}

private class Tail(val prev: Knot) : Knot() {
    val visited = mutableSetOf(Position(0, 0))

    fun move() {
        if (!isTouching(prev)) {
            if (x != prev.x) {
                x += (prev.x - x).sign
            }

            if (y != prev.y) {
                y += (prev.y - y).sign
            }

            visited.add(Position(x, y))
        }
    }
}

private data class Position(val x: Int, val y: Int)