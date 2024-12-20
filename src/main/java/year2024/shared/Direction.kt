package year2024.shared

enum class Direction(val dx: Int, val dy: Int, val char: Char) {
  UP(0, -1, '^'),
  DOWN(0, 1, 'v'),
  LEFT(-1, 0, '<'),
  RIGHT(1, 0, '>');

  fun clockwise(): Direction {
    return when (this) {
      UP -> RIGHT
      RIGHT -> DOWN
      DOWN -> LEFT
      LEFT -> UP
    }
  }

  fun counterClockwise(): Direction {
    return when (this) {
      UP -> LEFT
      LEFT -> DOWN
      DOWN -> RIGHT
      RIGHT -> UP
    }
  }

  companion object {
    private val directionMap = Direction.values().associateBy { it.char }

    fun get(char: Char): Direction {
      return directionMap[char]!!
    }
  }
}