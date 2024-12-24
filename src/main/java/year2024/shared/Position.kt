package year2024.shared

import kotlin.math.abs

data class Position(val x: Int, val y: Int) {
  fun up(): Position {
    return Position(x, y - 1)
  }

  fun down(): Position {
    return Position(x, y + 1)
  }

  fun left(): Position {
    return Position(x - 1, y)
  }

  fun right(): Position {
    return Position(x + 1, y)
  }

  fun neighbors(): List<Position> {
    return listOf(up(), down(), left(), right())
  }

  fun neighborIn(direction: Direction): Position {
    return when (direction) {
      Direction.UP -> up()
      Direction.DOWN -> down()
      Direction.LEFT -> left()
      Direction.RIGHT -> right()
    }
  }

  fun manhattanDistanceTo(position: Position): Int {
    return abs(position.x - this.x) + abs(position.y - this.y)
  }
}