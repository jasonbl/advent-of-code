package year2024.day18

import util.InputLoader
import year2024.shared.Position

private const val GRAPH_MAX_POSITION = 70

fun main() {
  val bytes = InputLoader.load("/year2024/day18/input.txt")
    .split("\n")
    .map { it.split(",") }
    .map { Position(it[0].toInt(), it[1].toInt()) }

  println("Part 1: ${partOne(bytes)}")
  println("Part 2: ${partTwo(bytes)}")
}

private fun partOne(bytes: List<Position>): Int {
  val graph = initGraph()
  for (i in 0 until 1024) {
    graph.remove(bytes[i])
  }

  return findShortestPath(graph)
}

private fun partTwo(bytes: List<Position>): String {
  var minIndex = 0
  var maxIndex = bytes.size - 1
  while (minIndex < maxIndex) {
    val midIndex = (minIndex + maxIndex) / 2

    val graph = initGraph()
    bytes.subList(0, midIndex + 1)
      .forEach { graph.remove(it) }

    if (findShortestPath(graph) == Int.MAX_VALUE) {
      maxIndex = midIndex
    } else {
      minIndex = midIndex + 1
    }
  }

  val byte = bytes[minIndex]
  return "${byte.x},${byte.y}"
}

private fun findShortestPath(graph: Set<Position>): Int {
  val distances = graph.associateWith { Int.MAX_VALUE }
    .toMutableMap()

  distances[Position(0, 0)] = 0

  val unvisited = HashSet(graph)
  while (unvisited.isNotEmpty()) {
    // This could be made more performant with a PriorityQueue
    val position = unvisited.minBy { distances[it]!! }
    unvisited.remove(position)

    if (distances[position] == Int.MAX_VALUE) {
      // All remaining positions aren't reachable
      break
    }

    val unvisitedNeighbors = position.neighbors().filter { unvisited.contains(it) }
    for (neighbor in unvisitedNeighbors) {
      val distanceToNeighbor = distances[position]!! + 1
      if (distanceToNeighbor < distances[neighbor]!!) {
        distances[neighbor] = distanceToNeighbor
      }
    }
  }

  return distances[Position(GRAPH_MAX_POSITION, GRAPH_MAX_POSITION)]!!
}

private fun initGraph(): HashSet<Position> {
  val graph = HashSet<Position>()
  for (y in 0..GRAPH_MAX_POSITION) {
    for (x in 0..GRAPH_MAX_POSITION) {
      graph.add(Position(x, y))
    }
  }

  return graph
}
