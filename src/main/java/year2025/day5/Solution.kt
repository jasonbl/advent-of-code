package year2025.day5

import util.InputLoader
import kotlin.math.max
import kotlin.math.sign

fun main() {
  val parts = InputLoader.load("/year2025/day5/input.txt").split("\n\n")

  var ranges = parts[0].split("\n")
    .map {
      val range = it.split("-")
      range[0].toLong() to range[1].toLong()
    }

  ranges = sortAndMerge(ranges)

  val ingredients = parts[1].split("\n")
    .map { it.toLong() }

  println("Part 1: ${part1(ranges, ingredients)}")
  println("Part 2: ${part2(ranges)}")
}

private fun part1(ranges: List<Pair<Long, Long>>, ingredients: List<Long>): Int {
  return ingredients.count { isFresh(it, ranges) }
}

private fun part2(ranges: List<Pair<Long, Long>>): Long {
  return ranges.sumOf { it.second - it.first + 1 }
}

private fun isFresh(ingredient: Long, ranges: List<Pair<Long, Long>>): Boolean {
  val index = ranges.binarySearch {
    if (ingredient >= it.first && ingredient <= it.second) 0
    else if (ingredient < it.first) 1
    else -1
  }

  return index >= 0
}

private fun sortAndMerge(ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
  val sorted = ranges.sortedWith { one, two -> (one.first - two.first).sign }

  val merged = arrayListOf<Pair<Long, Long>>()
  var curr = sorted[0]
  for (i in 1 until sorted.size) {
    if (sorted[i].first > curr.second) {
      merged.add(curr)
      curr = sorted[i]
    } else {
      curr = curr.first to max(curr.second, sorted[i].second)
    }
  }

  merged.add(curr)
  return merged
}
