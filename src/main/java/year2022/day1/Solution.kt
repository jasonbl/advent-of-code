package year2022.day1

import util.InputLoader

fun main() {
    val elves = InputLoader.load("/year2022/day1/input.txt").split("\n\n")
    val sortedCalories = getSortedCalories(elves)
    println("Part 1: ${sortedCalories[0]}")
    println("Part 2: ${sortedCalories.subList(0, 3).sum()}")
}

fun getSortedCalories(elves: List<String>): List<Int> {
    return elves.map { elf -> elf.split("\n").sumOf { e -> e.toInt() } }
        .sortedDescending()
}