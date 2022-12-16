package year2022.day12

import util.InputLoader
import kotlin.math.min

fun main() {
    val grid = InputLoader.load("/year2022/day12/input.txt").split("\n")
        .map { it.toList() }
    println("Part 1: ${partOne(grid)}")
    println("Part 2: ${partTwo(grid)}")
}

private fun partOne(grid: List<List<Char>>): Int {
    return traverse(findStart(grid), grid)
}

// Can solve this faster with reverse traversal (from 'E' to any 'a'), but this is much easier
private fun partTwo(grid: List<List<Char>>): Int {
    var min = Int.MAX_VALUE
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 'S' || grid[y][x] == 'a') {
                val distance = traverse(Position(x, y), grid)
                min = if (distance != -1) min(min, distance) else min
            }
        }
    }

    return min
}

private fun traverse(start: Position, grid: List<List<Char>>): Int {
    val queue = mutableListOf(start to 0)
    val visited = mutableSetOf(start)
    while (queue.isNotEmpty()) {
        val currNode = queue.removeFirst()
        val currChar = grid[currNode.first.y][currNode.first.x]
        if (currChar == 'E') {
            return currNode.second
        }

        getVisitableNeighbors(currNode.first, grid)
            .filter { !visited.contains(it) }
            .forEach {
                visited.add(it)
                queue.add(it to currNode.second + 1)
            }
    }

    return -1
}

private fun findStart(grid: List<List<Char>>): Position {
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 'S') {
                return Position(x, y)
            }
        }
    }

    throw IllegalArgumentException()
}

private fun getVisitableNeighbors(p: Position, grid: List<List<Char>>): List<Position> {
    return getNeighbors(p)
        .filter { grid.indices.contains(it.y) && grid[0].indices.contains(it.x) }
        .filter { canMove(grid[p.y][p.x], grid[it.y][it.x]) }
}

private fun getNeighbors(p: Position): List<Position> {
    return mutableListOf(
        Position(p.x + 1, p.y),
        Position(p.x - 1, p.y),
        Position(p.x, p.y + 1),
        Position(p.x, p.y - 1)
    )
}

private fun canMove(from: Char, to: Char): Boolean {
    val fromVal = if (from == 'S') 0 else if (from == 'E') 25 else from - 'a'
    val toVal = if (to == 'S') 0 else if (to == 'E') 25 else to - 'a'
    return fromVal + 1 >= toVal
}

private data class Position(val x: Int, val y: Int)