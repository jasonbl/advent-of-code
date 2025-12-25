package year2025.day8

import util.InputLoader
import kotlin.math.sign
import kotlin.math.sqrt

fun main() {
  val points = InputLoader.load("/year2025/day8/input.txt")
    .split("\n")
    .map {
      val coords = it.split(",")
      Point(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
    }

  val connections = arrayListOf<Pair<Int, Int>>()
  for (i in points.indices) {
    for (j in i + 1 until points.size) {
      connections.add(i to j)
    }
  }

  connections.sortWith { one, two ->
    val oneDist = points[one.first].distanceTo(points[one.second])
    val twoDist = points[two.first].distanceTo(points[two.second])
    (oneDist - twoDist).sign.toInt()
  }

  println("Part 1: ${part1(points, connections.subList(0, 1000))}")
  println("Part 2: ${part2(points, connections)}")
}

private fun part1(points: List<Point>, connections: List<Pair<Int, Int>>): Long {
  val uf = UnionFind()
  for (connection in connections) {
    uf.union(connection.first, connection.second)
  }

  val sizes = IntArray(points.size)
  for (i in points.indices) {
    sizes[uf.find(i)]++
  }

  sizes.sortDescending()
  return sizes[0].toLong() * sizes[1] * sizes[2]
}

private fun part2(points: List<Point>, connections: List<Pair<Int, Int>>): Long {
  val uf = UnionFind()
  for (connection in connections) {
    val rootId = uf.union(connection.first, connection.second)
    if (uf.getSize(rootId) == points.size) {
      return points[connection.first].x.toLong() * points[connection.second].x
    }
  }

  return -1
}

private data class Point(val x: Int, val y: Int, val z: Int) {

  fun distanceTo(point: Point): Double {
    val xDiff = (this.x - point.x).toLong()
    val yDiff = (this.y - point.y).toLong()
    val zDiff = (this.z - point.z).toLong()
    return sqrt((xDiff * xDiff + yDiff * yDiff + zDiff * zDiff).toDouble())
  }
}

private class UnionFind {

  private val parents = hashMapOf<Int, Int>()
  private val rootSizes = hashMapOf<Int, Int>()

  fun union(idOne: Int, idTwo: Int): Int {
    val rootOne = find(idOne)
    val rootTwo = find(idTwo)
    if (rootOne != rootTwo) {
      val sizeOne = rootSizes.getOrDefault(rootOne, 1)
      val sizeTwo = rootSizes.getOrDefault(rootTwo, 1)

      parents[rootOne] = rootTwo
      rootSizes.remove(rootOne)
      rootSizes[rootTwo] = sizeOne + sizeTwo
    }

    return rootTwo
  }

  fun find(id: Int): Int {
    val parent = parents.getOrDefault(id, id)
    if (parent == id) {
      return id
    }

    val rootId = find(parent)
    parents[id] = rootId
    return rootId
  }

  fun getSize(id: Int): Int {
    val rootId = find(id)
    return rootSizes.getOrDefault(rootId, 1)
  }
}