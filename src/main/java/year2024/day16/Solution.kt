package year2024.day16

import util.InputLoader
import year2024.shared.Direction
import year2024.shared.Position

fun main() {
  val grid = InputLoader.load("/year2024/day16/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  lateinit var start: Position
  lateinit var end: Position
  grid.forEachIndexed { rowIndex, row ->
    row.forEachIndexed { colIndex, char ->
      val position = Position(colIndex, rowIndex)
      if (char == 'S') {
        start = position
      } else if (char == 'E') {
        end = position
      }
    }
  }

  val startNode = GraphNode(start, Direction.RIGHT)

  // Map from node to the shortest distance to that node
  val distances = hashMapOf(startNode to 0)
  val toVisit = hashSetOf(startNode)
  val visited = HashSet<GraphNode>()

  // Map from target node to the nodes immediately before it in the shortest path from a source to that target.
  // There can be multiple shortest paths, hence keeping track of multiple previous nodes
  val paths = hashMapOf(startNode to HashSet<GraphNode>())

  while (toVisit.isNotEmpty()) {
    // This could be made more performant with a PriorityQueue
    val node = toVisit.minBy { distances.getOrDefault(it, Int.MAX_VALUE) }
    toVisit.remove(node)

    val position = node.position
    val direction = node.direction

    // All neighboring nodes mapped to the cost to reach them
    val neighborNodesToCostMap = mapOf(
      GraphNode(position.neighborIn(direction.clockwise()), direction.clockwise()) to 1001,
      GraphNode(position.neighborIn(direction.counterClockwise()), direction.counterClockwise()) to 1001,
      GraphNode(position.neighborIn(direction), direction) to 1
    )

    // Visit neighbor nodes, ignoring walls and nodes we've already visited
    for (neighborNodeToCostEntry in neighborNodesToCostMap) {
      val neighborNode = neighborNodeToCostEntry.key
      if (grid[neighborNode.position.y][neighborNode.position.x] == '#' || visited.contains(neighborNode)) {
        continue
      }

      val cost = neighborNodeToCostEntry.value
      val distanceToNeighborNode = distances[node]!! + cost
      if (distanceToNeighborNode < distances.getOrDefault(neighborNode, Int.MAX_VALUE)) {
        distances[neighborNode] = distanceToNeighborNode
        paths[neighborNode] = hashSetOf(node)
      } else if (distanceToNeighborNode == distances.getOrDefault(neighborNode, Int.MAX_VALUE)) {
        paths[neighborNode]!!.add(node)
      }

      toVisit.add(neighborNode)
    }

    visited.add(node)
  }

  val endNodes = Direction.values().map { GraphNode(end, it) }
  val minCost = endNodes.minOf { distances[it] ?: Int.MAX_VALUE }
  println("Part 1: $minCost")

  val minEndNodes = endNodes.filter { distances[it] == minCost }
  val nodesInShortestPaths = computeNodesInShortestPaths(minEndNodes, paths)

  val positionsInShortestPaths = nodesInShortestPaths.map { it.position }.toSet()
  println("Part 2: ${positionsInShortestPaths.size}")
}

private fun computeNodesInShortestPaths(
  nodes: List<GraphNode>,
  paths: Map<GraphNode, Set<GraphNode>>
): HashSet<GraphNode> {
  if (nodes.isEmpty()) {
    return HashSet()
  }

  val previousNodes = nodes.mapNotNull { paths[it] }
    .flatten()

  val set = computeNodesInShortestPaths(previousNodes, paths)
  set.addAll(nodes)

  return set
}

private data class GraphNode(val position: Position, val direction: Direction)
