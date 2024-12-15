package year2024.day14

import util.InputLoader

private val coordinateRegex = Regex("(-?\\d+),(-?\\d+)")

fun main() {
  val robots = InputLoader.load("/year2024/day14/input.txt")
    .split("\n")
    .map {
      val matches = coordinateRegex.findAll(it).toList()
      val x = matches[0].groupValues[1].toInt()
      val y = matches[0].groupValues[2].toInt()
      val vx = matches[1].groupValues[1].toInt()
      val vy = matches[1].groupValues[2].toInt()
      Robot(x, y, vx, vy)
    }

  println("Part 1: ${partOne(robots)}")
  partTwo(robots)
}

private fun partOne(robots: List<Robot>): Long {
  val room = Room(robots, width = 101, height = 103)
  (0 until 100).forEach { _ ->
    room.moveRobots()
  }

  return room.getSafetyFactor()
}

private fun partTwo(robots: List<Robot>) {
  val room = Room(robots, width = 101, height = 103)
  (0 until 10000).forEach {
    room.moveRobots()
    if (room.mightHaveEasterEgg()) {
      println("Second: ${it + 1}")
      println(room.toString())
    }
  }
}

private class Room(private var robots: List<Robot>, val width: Int, val height: Int) {

  fun moveRobots() {
    robots = robots.map { moveRobot(it) }
  }

  fun getSafetyFactor(): Long {
    var upLeft = 0
    var upRight = 0
    var downLeft = 0
    var downRight = 0
    robots.forEach {
      if (it.x < width / 2 && it.y < height / 2) upLeft++
      if (it.x > width / 2 && it.y < height / 2) upRight++
      if (it.x < width / 2 && it.y > height / 2) downLeft++
      if (it.x > width / 2 && it.y > height / 2) downRight++
    }

    return upLeft * upRight * downLeft * downRight.toLong()
  }

  fun mightHaveEasterEgg(): Boolean {
    val positions = getPositions()
    return robots.any {
      val p = Position(it.x, it.y)
      positions.containsAll(listOf(p.up(), p.down(), p.left(), p.right(), p.down().left(), p.down().right(),
        p.up().left(), p.up().right()))
    }
  }

  override fun toString(): String {
    val positions = getPositions()

    val stringBuilder = StringBuilder()
    for (y in 0 until height) {
      for (x in 0 until width) {
        if (!positions.contains(Position(x, y))) {
          stringBuilder.append(".")
        } else {
          stringBuilder.append("X")
        }
      }

      stringBuilder.append("\n")
    }

    return stringBuilder.toString()
  }

  private fun moveRobot(robot: Robot): Robot {
    val nextX = (width + robot.x + robot.vx) % width
    val nextY = (height + robot.y + robot.vy) % height
    return Robot(nextX, nextY, robot.vx, robot.vy)
  }

  private fun getPositions(): Set<Position> {
    return robots.map { Position(it.x, it.y) }.toSet()
  }
}

private data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int)

private data class Position(val x: Int, val y: Int) {
  fun up(): Position { return Position(x, y - 1) }
  fun down(): Position { return Position(x, y + 1) }
  fun left(): Position { return Position(x - 1, y) }
  fun right(): Position { return Position(x + 1, y) }
}
