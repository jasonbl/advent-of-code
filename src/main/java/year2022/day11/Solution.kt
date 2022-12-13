package year2022.day11

import util.InputLoader

fun main() {
    val monkeyInputs = InputLoader.load("/year2022/day11/input.txt").split("\n\n")
    println("Part 1: ${simulate(20, monkeyInputs, true)}")
    println("Part 2: ${simulate(10_000, monkeyInputs, false)}")
}

fun simulate(numRounds: Int, monkeyInputs: List<String>, isPartOne: Boolean): Long {
    val monkeys = monkeyInputs.map { Monkey.fromString(it) }
    val lcm = monkeys.map { it.testVal.toLong() }.reduce { acc, i -> acc * i }
    for (i in 1..numRounds) {
        for (monkey in monkeys) {
            while (monkey.hasItems()) {
                val worryLevel = monkey.inspectItem()
                val newWorryLevel = if (isPartOne) worryLevel / 3 else worryLevel % lcm
                val nextMonkey = monkey.getNextMonkey(newWorryLevel)
                monkey.throwItem(newWorryLevel, monkeys[nextMonkey])
            }
        }
    }

    val numInspects = monkeys.map { it.numInspects }.sortedDescending()
    return numInspects[0] * numInspects[1]
}
