package year2024.day5

import util.InputLoader

fun main() {
  val input = InputLoader.load("/year2024/day5/input.txt")
    .split("\n\n")

  val rules = parseRules(input[0])
  val updates = parseUpdates(input[1])

  println("Part 1: " + partOne(rules, updates))
  println("Part 2: " + partTwo(rules, updates))
}

private fun partOne(rules: Map<Int, Set<Int>>, updates: List<List<Int>>): Int {
  return updates.sumOf {
    if (isValid(it, rules)) it.middle() else 0
  }
}

private fun partTwo(rules: Map<Int, Set<Int>>, updates: List<List<Int>>): Int {
  return updates.filter { !isValid(it, rules) }
    .map {
      val filteredRules = filterRules(it, rules)
      it.sortedByDescending { page -> filteredRules[page]!!.size }
    }
    .sumOf { it.middle() }
}

private fun isValid(update: List<Int>, rules: Map<Int, Set<Int>>): Boolean {
  val filteredRules = filterRules(update, rules)
  for (i in 1 until update.size) {
    if (!isCorrectlyOrdered(update[i - 1], update[i], filteredRules)) {
      return false
    }
  }

  return true
}

private fun isCorrectlyOrdered(before: Int, after: Int, filteredRules: Map<Int, Set<Int>>): Boolean {
  return filteredRules[before]!!.contains(after)
}

private fun filterRules(update: List<Int>, rules: Map<Int, Set<Int>>): Map<Int, Set<Int>> {
  val updateSet = update.toSet()
  return rules.filter { updateSet.contains(it.key)  }
    .mapValues { it.value.intersect(updateSet) }
}

private fun parseRules(rulesInput: String): Map<Int, Set<Int>> {
  val pagePairs = rulesInput.split("\n")
    .map { it.split("|").map { value -> value.toInt() } }

  val rules = HashMap<Int, HashSet<Int>>()
  pagePairs.forEach {
    rules.computeIfAbsent(it[0]) { HashSet() }
      .add(it[1])
  }

  return rules
}

private fun parseUpdates(updatesInput: String): List<List<Int>> {
  return updatesInput.split("\n")
    .map { it.split(",").map { value -> value.toInt() } }
}

private fun <T> List<T>.middle(): T {
  return this[size / 2]
}
