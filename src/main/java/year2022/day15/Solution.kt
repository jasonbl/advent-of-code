package year2022.day15

import util.InputLoader
import kotlin.math.abs

private val COORDINATES = Regex("x=(-?\\d+), y=(-?\\d+)")

fun main() {
    val sensors = InputLoader.load("/year2022/day15/input.txt").split("\n")
        .map { buildSensor(it) }
    println("Part 1: ${partOne(sensors)}")
    println("Part 2: ${partTwo(sensors)}")
}

private fun partOne(sensors: List<Sensor>): Int {
    val minX = sensors.minOf { it.position.x - it.distanceToBeacon() }
    val maxX = sensors.maxOf { it.position.x + it.distanceToBeacon() }
    return scanPositions(sensors, minX, maxX, 2000000).second
}

private fun partTwo(sensors: List<Sensor>): Long {
    for (y in 0..4000000) {
        val results = scanPositions(sensors, 0, 4000000, y)
        if (results.first.isNotEmpty()) {
            val validPosition = results.first.first()
            return 4000000L * validPosition.x + validPosition.y
        }
    }

    throw IllegalArgumentException()
}

private fun scanPositions(sensors: List<Sensor>, minX: Int, maxX: Int, y: Int): Pair<Set<Position>, Int> {
    val validPositions = mutableSetOf<Position>()
    var invalidPositions = 0
    var x = minX
    while (x <= maxX) {
        val position = Position(x, y)
        val visibleSensors = sensors.filter { !it.isValidBeaconPosition(position) }
        if (visibleSensors.isNotEmpty()) {
            val maxSensor = visibleSensors.maxBy { it.getMaxVisibleX(y)!! }
            val maxVisibleX = maxSensor.getMaxVisibleX(y)!!
            invalidPositions +=
                if (maxSensor.closestBeacon.y == y && (x..maxVisibleX).contains(maxSensor.closestBeacon.x))
                    maxVisibleX - x
                else
                    maxVisibleX - x + 1

            x = maxVisibleX + 1
        } else {
            validPositions.add(Position(x, y))
            x++
        }
    }

    return validPositions to invalidPositions
}

private fun buildSensor(sensorString: String): Sensor {
    val matches = COORDINATES.findAll(sensorString).iterator()
    val firstMatch = matches.next()
    val position = Position(firstMatch.groupValues[1].toInt(), firstMatch.groupValues[2].toInt())
    val secondMatch = matches.next()
    val closestBeacon = Position(secondMatch.groupValues[1].toInt(), secondMatch.groupValues[2].toInt())
    return Sensor(position, closestBeacon)
}

private class Sensor(val position: Position, val closestBeacon: Position) {
    fun distanceToBeacon(): Int {
        return position.distanceTo(closestBeacon)
    }

    fun isValidBeaconPosition(that: Position): Boolean {
        return position.distanceTo(that) > distanceToBeacon() || that == closestBeacon
    }

    fun getMaxVisibleX(y: Int): Int? {
        val distToRow = position.distanceTo(Position(position.x, y))
        if (distToRow > distanceToBeacon()) {
            return null
        }

        val remainingDist = distanceToBeacon() - distToRow
        return position.x + remainingDist
    }
}

private data class Position(val x: Int, val y: Int) {
    fun distanceTo(that: Position): Int {
        return abs(that.x - this.x) + abs(that.y - this.y)
    }
}