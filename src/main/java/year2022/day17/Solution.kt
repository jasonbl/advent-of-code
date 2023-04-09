package year2022.day17

import util.InputLoader

private val LINE = Shape(ShapeNode(right = ShapeNode(right = ShapeNode(right = ShapeNode()))))
private val CROSS = Shape(ShapeNode(up = ShapeNode(left = ShapeNode(), right = ShapeNode(), up = ShapeNode())))
private val REVERSE_L = Shape(ShapeNode(right = ShapeNode(right = ShapeNode(up = ShapeNode(up = ShapeNode())))))
private val UP_LINE = Shape(ShapeNode(up = ShapeNode(up = ShapeNode(up = ShapeNode()))))
private val SQUARE = Shape(ShapeNode(up = ShapeNode(right = ShapeNode()), right = ShapeNode()))
private val SHAPE_ORDER = listOf(LINE, CROSS, REVERSE_L, UP_LINE, SQUARE)

fun main() {
    val input = InputLoader.load("/year2022/day17/input.txt")
    println("Part 1: ${simulate(input, 2022)}")
    // println("Part 2: ${simulate(input, 1000000000000)}")
}

private fun simulate(input: String, numRocks: Long): Int {
    val grid: Array<ArrayDeque<ShapeNode?>> = Array(7) { ArrayDeque() }
    var inputIndex = 0
    var rocksDropped = 0L
    while (rocksDropped < numRocks) {
        val shapeIndex = (rocksDropped % SHAPE_ORDER.size).toInt()
        val shape = dropShape(SHAPE_ORDER[shapeIndex], grid)
        while (moveShape(shape, input[inputIndex++ % input.length], grid)) { }
        addShape(shape, grid)

        // printGrid(grid)
        rocksDropped++
    }

    return getHeight(grid)
}

private fun dropShape(shape: Shape, grid: Array<ArrayDeque<ShapeNode?>>): Shape {
    val startX = 2 - shape.getLeftMost().x
    val startY = getHeight(grid) + 3
    return Shape(shape.startNode, Position(startX, startY))
}

private fun moveShape(shape: Shape, direction: Char, grid: Array<ArrayDeque<ShapeNode?>>): Boolean {
    when (direction) {
        '<' -> {
            shape.moveLeft()
            if (isInvalid(shape, grid)) {
                shape.moveRight()
            }
        }
        '>' -> {
            shape.moveRight()
            if (isInvalid(shape, grid)) {
                shape.moveLeft()
            }
        }
    }

    shape.moveDown()
    if (isInvalid(shape, grid)) {
        shape.moveUp()
        return false
    }

    return true
}

private fun addShape(shape: Shape, grid: Array<ArrayDeque<ShapeNode?>>) {
    for (position in shape.nodePositions) {
        while (position.y >= grid[position.x].size) {
            grid[position.x].add(null)
        }

        grid[position.x][position.y] = ShapeNode()
    }
}

private fun getHeight(grid: Array<ArrayDeque<ShapeNode?>>): Int {
    return grid.maxOf { it.size }
}

private fun isInvalid(shape: Shape, grid: Array<ArrayDeque<ShapeNode?>>): Boolean {
    if (shape.getLeftMost().x < 0 || shape.getRightMost().x >= grid.size || shape.getBottomMost().y < 0) {
        return true
    }

    return shape.nodePositions
        .any { it.y < grid[it.x].size && grid[it.x][it.y] != null}
}

private fun printGrid(grid: Array<ArrayDeque<ShapeNode?>>) {
    val maxHeight = getHeight(grid)
    for (y in maxHeight downTo 0) {
        for (x in grid.indices) {
            if (y < grid[x].size && grid[x][y] != null) {
                print('#')
            } else {
                print('.')
            }
        }
        println()
    }
    println()
}

private class Shape(val startNode: ShapeNode, startPosition: Position = Position(0, 0)) {
    var nodePositions = getPositions(startPosition)

    fun getBottomMost(): Position {
        return nodePositions.minBy { it.y }
    }

    fun getLeftMost(): Position {
        return nodePositions.minBy { it.x }
    }

    fun getRightMost(): Position {
        return nodePositions.maxBy { it.x }
    }

    fun moveLeft() {
        nodePositions = nodePositions.map { Position(it.x - 1, it.y) }
    }

    fun moveRight() {
        nodePositions = nodePositions.map {  Position(it.x + 1, it.y) }
    }

    fun moveDown() {
        nodePositions = nodePositions.map { Position(it.x, it.y - 1) }
    }

    fun moveUp() {
        nodePositions = nodePositions.map { Position(it.x, it.y + 1) }
    }

    private fun getPositions(startPosition: Position): List<Position> {
        val positions = mutableListOf<Position>()
        val queue = mutableListOf(startNode to startPosition)
        while (queue.isNotEmpty()) {
            val nodeToPosition = queue.removeFirst()
            val node = nodeToPosition.first
            val position = nodeToPosition.second
            positions.add(position)

            if (node.left != null) queue.add(node.left to Position(position.x - 1, position.y))
            if (node.right != null) queue.add(node.right to Position(position.x + 1, position.y))
            if (node.up != null) queue.add(node.up to Position(position.x, position.y + 1))
            if (node.down != null) queue.add(node.down to Position(position.x, position.y - 1))
        }

        return positions
    }
}

private class ShapeNode(val left: ShapeNode? = null, val up: ShapeNode? = null, val right: ShapeNode? = null,
                        val down: ShapeNode? = null)

private class Position(val x: Int, val y: Int)