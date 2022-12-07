package year2022.day3

import util.InputLoader

fun main() {
    val bags = InputLoader.load("/year2022/day3/input.txt").split("\n")
    println("Part 1: ${partOne(bags)}")
    println("Part 2: ${partTwo(bags)}")
}

fun partOne(bags: List<String>): Int {
    var prioritySum = 0
    for (bag in bags) {
        val duplicate = bag.substring(0 until bag.length / 2).toHashSet()
            .intersect(bag.substring(bag.length / 2 until bag.length).toHashSet())
            .first()

        prioritySum += getPriority(duplicate)
    }

    return prioritySum
}

fun partTwo(bags: List<String>): Int {
    var prioritySum = 0
    for (i in bags.indices step 3) {
        val badge = bags[i].toHashSet()
            .intersect(bags[i + 1].toHashSet())
            .intersect(bags[i + 2].toHashSet())
            .first()

        prioritySum += getPriority(badge)
    }

    return prioritySum
}

fun getPriority(letter: Char): Int {
    return if (letter.isLowerCase()) letter - 'a' + 1 else letter - 'A' + 27
}