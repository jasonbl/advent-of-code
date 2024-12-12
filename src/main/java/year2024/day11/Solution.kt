package year2024.day11

import util.InputLoader

fun main() {
  val stones = InputLoader.load("/year2024/day11/input.txt")
    .split(" ")
    .map { it.toLong() }

  println("Part 1: ${countStones(stones, 25)}")
  println("Part 2: ${countStones(stones, 75)}")
}

private fun countStones(stones: List<Long>, numBlinks: Int): Long {
  val cache = HashMap<CacheKey, Long>()
  return stones.sumOf { countStones(it, numBlinks, cache) }
}

private fun countStones(stone: Long, blinksRemaining: Int, cache: HashMap<CacheKey, Long>): Long {
  val cacheKey = CacheKey(stone, blinksRemaining)
  if (blinksRemaining == 0) {
    return 1
  } else if (cache.containsKey(cacheKey)) {
    return cache[cacheKey]!!
  }

  val count = evolve(stone)
    .sumOf { countStones(it, blinksRemaining - 1, cache) }

  cache[cacheKey] = count
  return count
}

private fun evolve(stone: Long): List<Long> {
  val stoneString = stone.toString()
  return if (stone == 0L) {
    listOf(1)
  } else if (stoneString.length % 2 == 0) {
    listOf(
      stoneString.substring(0, stoneString.length / 2).toLong(),
      stoneString.substring(stoneString.length / 2, stoneString.length).toLong()
    )
  } else {
    listOf(stone * 2024)
  }
}

private data class CacheKey(val stone: Long, val blinksRemaining: Int)
