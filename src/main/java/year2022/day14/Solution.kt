package year2022.day14

import util.InputLoader
import kotlin.math.max
import kotlin.math.min

fun main() {
    val paths = InputLoader.load("/year2022/day14/input.txt").split("\n")
    println("Part 1: ${simulate(buildCave(paths, false))}")
    println("Part 2: ${simulate(buildCave(paths, true))}")
}

private fun simulate(cave: Array<Array<Char>>): Int {
    var count = 0
    while (dropSand(cave)) {
        count++
    }

    return count
}

private fun dropSand(cave: Array<Array<Char>>): Boolean {
    var x = 500
    var y = 0
    if (cave[y][x] != '.') {
        return false
    }

    while (canMoveSand(x, y, cave)) {
        if (cave[y + 1][x] == '.') {
            y++
        } else if (cave[y + 1][x - 1] == '.') {
            x--
            y++
        } else {
            x++
            y++
        }
    }

    cave[y][x] = 'o'
    return y < cave.lastIndex
}

private fun canMoveSand(x: Int, y: Int, cave: Array<Array<Char>>): Boolean {
    return y + 1 < cave.size && (x - 1..x + 1).any { cave[y + 1][it] == '.' }
}

private fun buildCave(paths: List<String>, partTwo: Boolean): Array<Array<Char>> {
    val cave = Array(1000) { Array(1000) { '.' } }
    var maxY = Int.MIN_VALUE
    for (path in paths) {
        val points = path.split(" -> ").map {
            val coordinates = it.split(",")
            coordinates[0].toInt() to coordinates[1].toInt()
        }

        for (i in 0 until points.lastIndex) {
            val x1 = points[i].first
            val y1 = points[i].second
            val x2 = points[i + 1].first
            val y2 = points[i + 1].second
            maxY = max(maxY, max(y1, y2))
            if (x1 == x2) {
                (min(y1, y2)..max(y1, y2))
                    .forEach { cave[it][x1] = '#' }
            } else {
                (min(x1, x2)..max(x1, x2))
                    .forEach { cave[y1][it] = '#' }
            }
        }
    }

    if (partTwo) {
        val floorY = maxY + 2
        cave[floorY].indices.forEach { cave[floorY][it] = '#' }
    }

    return cave
}