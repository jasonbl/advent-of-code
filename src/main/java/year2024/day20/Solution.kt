package year2024.day20

import util.InputLoader
import year2024.shared.Position

fun main() {
  val grid = InputLoader.load("/year2024/day20/input.txt")
    .split("\n")
    .map { it.toCharArray() }

  val graph = initGraph(grid)
  val traversalSummary = traverse(graph)

  println("Part 1: ${countCheats(graph, traversalSummary, 2)}")
  println("Part 2: ${countCheats(graph, traversalSummary, 20)}")
}

private fun countCheats(graph: Graph, traversalSummary: TraversalSummary, cheatDuration: Int): Int {
  val distanceTo = traversalSummary.distanceTo
  var cheats = 0
  for (node in traversalSummary.shortestPath) {
    // Compute all positions within cheatDuration Manhattan distance to start node
    val cheatNodes = bfs(node, cheatDuration)
      .filter { graph.nodes.contains(it) }

    cheats += cheatNodes.map { distanceTo[it]!! - distanceTo[node]!! - node.manhattanDistanceTo(it) }
      .count { it >= 100 }
  }

  return cheats
}

private fun bfs(start: Position, maxDistance: Int): Set<Position> {
  val visited = hashSetOf(start)
  val queue = ArrayDeque(listOf(start to 0))
  while (queue.isNotEmpty()) {
    val pair = queue.removeFirst()
    val node = pair.first
    val distance = pair.second
    if (distance == maxDistance) {
      break
    }

    node.neighbors()
      .filter { !visited.contains(it) }
      .forEach {
        visited.add(it)
        queue.add(it to distance + 1)
      }
  }

  return visited
}

private fun traverse(graph: Graph): TraversalSummary {
  val distanceTo = graph.nodes.associateWith { Int.MAX_VALUE }
    .toMutableMap()
  distanceTo[graph.start] = 0

  // Map from position to the position before it in the shortest path
  val pathTo = hashMapOf<Position, Position?>(graph.start to null)

  val unvisited = HashSet(graph.nodes)
  while (unvisited.isNotEmpty()) {
    // TODO This could be made more performant with a PriorityQueue
    val position = unvisited.minBy { distanceTo[it]!! }

    val unvisitedNeighbors = position.neighbors().filter { unvisited.contains(it) }
    for (neighbor in unvisitedNeighbors) {
      val distanceToNeighbor = distanceTo[position]!! + 1
      if (distanceToNeighbor < distanceTo[neighbor]!!) {
        distanceTo[neighbor] = distanceToNeighbor
        pathTo[neighbor] = position
      }
    }

    unvisited.remove(position)
  }

  val shortestPath = ArrayDeque<Position>()
  var currNode: Position? = graph.end
  while (currNode != null) {
    shortestPath.addFirst(currNode)
    currNode = pathTo[currNode]
  }

  return TraversalSummary(shortestPath, distanceTo)
}

private fun initGraph(grid: List<CharArray>): Graph {
  val nodes = HashSet<Position>()
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

      if (char != '#') {
        nodes.add(position)
      }
    }
  }

  return Graph(start, end, nodes)
}

private data class Graph(val start: Position, val end: Position, val nodes: Set<Position>)

private data class TraversalSummary(val shortestPath: List<Position>, val distanceTo: Map<Position, Int>)
