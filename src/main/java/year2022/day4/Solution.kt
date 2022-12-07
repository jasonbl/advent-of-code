package year2022.day4

import util.InputLoader

val RANGE_REGEX = Regex("(\\d+)-(\\d+)")

fun main() {
    val pairs = InputLoader.load("/year2022/day4/input.txt").split("\n")
    var numContain = 0
    var numOverlap = 0
    for (pair in pairs) {
        val matches = RANGE_REGEX.findAll(pair).iterator()
        val firstMatch = matches.next()
        val firstRange = firstMatch.groupValues[1].toInt()..firstMatch.groupValues[2].toInt()

        val secondMatch = matches.next()
        val secondRange = secondMatch.groupValues[1].toInt()..secondMatch.groupValues[2].toInt()
        if ((firstRange.contains(secondRange.first) && firstRange.contains(secondRange.last)) ||
            (secondRange.contains(firstRange.first) && secondRange.contains(firstRange.last))
        ) {
            numContain++
            numOverlap++
        } else if (firstRange.contains(secondRange.first) || firstRange.contains(secondRange.last)) {
            numOverlap++
        }
    }

    println("Part 1: $numContain")
    println("Part 2: $numOverlap")
}