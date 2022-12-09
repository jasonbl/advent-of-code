package year2022.day8

import util.InputLoader

fun main() {
    val grid = InputLoader.load("/year2022/day8/input.txt").split("\n")
        .map { row -> row.map { it.digitToInt() } }

    val gridStats = getGridStats(grid)

    println("Part 1: ${countVisible(gridStats)}")
    println("Part 2: ${getMaxScenicScore(gridStats)}")
}

fun countVisible(gridStats: List<List<TreeStats>>): Int {
    return gridStats.flatten().count { it.isVisible }
}

fun getMaxScenicScore(gridStats: List<List<TreeStats>>): Int {
    return gridStats.flatten().maxOf { it.scenicScore }
}

fun getGridStats(grid: List<List<Int>>): List<List<TreeStats>> {
    val gridStats = ArrayList<ArrayList<TreeStats>>()
    for (row in grid.indices) {
        gridStats.add(ArrayList())
        for (col in grid[0].indices) {
            gridStats[row].add(getTreeStats(grid, row, col))
        }
    }

    return gridStats
}

fun getTreeStats(grid: List<List<Int>>, row: Int, col: Int): TreeStats {
    val left = checkDirection(grid, row, col, intArrayOf(-1, 0))
    val right = checkDirection(grid, row, col, intArrayOf(1, 0))
    val up = checkDirection(grid, row, col, intArrayOf(0, 1))
    val down = checkDirection(grid, row, col, intArrayOf(0, -1))


    val isVisible = left.isVisible || right.isVisible || up.isVisible|| down.isVisible
    val scenicScore = left.scenicScore * right.scenicScore * up.scenicScore * down.scenicScore
    return TreeStats(isVisible, scenicScore)
}

fun checkDirection(grid: List<List<Int>>, row: Int, col: Int, direction: IntArray): TreeStats {
    var x = col + direction[0]
    var y = row + direction[1]
    var isVisible = true
    var numSeen = 0
    while (isInGrid(grid, x, y)) {
        numSeen++
        if (grid[y][x] >= grid[row][col]) {
            isVisible = false
            break
        }

        x += direction[0]
        y += direction[1]
    }

    return TreeStats(isVisible, numSeen)
}

fun isInGrid(grid: List<List<Int>>, x: Int, y: Int): Boolean {
    return grid.indices.contains(y) && grid[0].indices.contains(x)
}

class TreeStats(val isVisible: Boolean, val scenicScore: Int)