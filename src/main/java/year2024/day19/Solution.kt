package year2024.day19

import util.InputLoader

fun main() {
  val inputParts = InputLoader.load("/year2024/day19/input.txt")
    .split("\n\n")

  val towels = inputParts[0].split(", ").toSet()
  val designs = inputParts[1].split("\n")

  println("Part 1: ${partOne(towels, designs)}")
  println("Part 2: ${partTwo(towels, designs)}")
}

private fun partOne(towels: Set<String>, designs: List<String>): Int {
  val cache = HashMap<String, Long>()
  return designs.count { countArrangements(it, towels, cache) > 0 }
}

private fun partTwo(towels: Set<String>, designs: List<String>): Long {
  val cache = HashMap<String, Long>()
  return designs.sumOf { countArrangements(it, towels, cache) }
}

private fun countArrangements(design: String, towels: Set<String>, cache: HashMap<String, Long>): Long {
  if (design == "") {
    return 1
  }

  if (cache.containsKey(design)) {
    return cache[design]!!
  }

  val validSubstrings = arrayListOf<String>()
  for (i in 1..design.length) {
    val substring = design.substring(0, i)
    if (towels.contains(substring)) {
      validSubstrings.add(substring)
    }
  }

  val numArrangements = validSubstrings.sumOf {
    countArrangements(design.substring(it.length), towels, cache)
  }

  cache[design] = numArrangements
  return numArrangements
}
