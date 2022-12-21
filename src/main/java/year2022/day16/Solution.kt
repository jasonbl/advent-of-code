package year2022.day16

import util.InputLoader

fun main() {
    val valves = InputLoader.load("/year2022/day16/input.txt").split("\n")
        .map { Valve.fromString(it) }

    println("Part 1: ${partOne(valves)}")
    println("Part 2: ${partTwo(valves)}")
}

fun partOne(valves: List<Valve>): Int {
    val result = ValveTraverser(valves).getMaxFlow(30)
    return result.maxFlow
}

fun partTwo(valves: List<Valve>): Int {
    val firstResult = ValveTraverser(valves).getMaxFlow(26)
    val updatedValves = mutableListOf<Valve>()
    for (valve in valves) {
        if (firstResult.opened.contains(valve.id)) {
            updatedValves.add(Valve(valve.id, 0, valve.neighborIds))
        } else {
            updatedValves.add(valve)
        }
    }

    val secondResult = ValveTraverser(updatedValves).getMaxFlow(26)
    return firstResult.maxFlow + secondResult.maxFlow
}
