package year2022.day13

import util.InputLoader

private val DIVIDER_ONE = parseData("[[2]]")
private val DIVIDER_TWO = parseData("[[6]]")

fun main() {
    val pairs = InputLoader.load("/year2022/day13/input.txt").split("\n\n")
        .map {
            val pair = it.split("\n").map { packet -> parseData(packet) }
            pair[0] to pair[1]
        }

    println("Part 1: ${partOne(pairs)}")
    println("Part 2: ${partTwo(pairs)}")
}

private fun partOne(pairs: List<Pair<Data, Data>>): Int {
    var result = 0
    for ((index, pair) in pairs.withIndex()) {
        if (compare(pair.first, pair.second) < 0) {
            result += index + 1
        }
    }

    return result
}

private fun partTwo(pairs: List<Pair<Data, Data>>): Int {
    val packets = pairs.flatMap { it.toList() }.toMutableList()
    packets.addAll(listOf(DIVIDER_ONE, DIVIDER_TWO))
    packets.sortWith { left, right -> compare(left, right) }
    return (packets.indexOf(DIVIDER_ONE) + 1) * (packets.indexOf(DIVIDER_TWO) + 1)
}

private fun compare(left: Data, right: Data): Int {
    if (left is ListData && right is ListData) {
        for (i in left.values.indices) {
            if (!right.values.indices.contains(i)) {
                return 1
            }

            val comparison = compare(left.values[i], right.values[i])
            if (comparison != 0) {
                return comparison
            }
        }

        return if (left.values.size == right.values.size) 0 else -1
    } else if (left is IntData && right is IntData) {
        return left.value.compareTo(right.value)
    } else if (left is IntData && right is ListData) {
        return compare(ListData(mutableListOf(left)), right)
    } else {
        return compare(left, ListData(mutableListOf(right)))
    }
}

private fun parseData(packet: String): Data {
    val stack = ArrayDeque<ListData>()
    var index = 0
    while (index < packet.length) {
        if (packet[index] == '[') {
            stack.addFirst(ListData())
        } else if (packet[index].isDigit()) {
            var intStr = packet[index] + ""
            while (index + 1 < packet.length && packet[index + 1].isDigit()) {
                intStr += packet[index + 1]
                index++
            }

            val intData = IntData(intStr.toInt())
            if (stack.isEmpty()) {
                return intData
            }

            stack.first().values.add(intData)
        } else if (packet[index] == ']') {
            val listData = stack.removeFirst()
            if (stack.isEmpty()) {
                return listData
            }

            stack.first().values.add(listData)
        }

        index++
    }

    return ListData()
}

private abstract class Data

private class ListData(val values: MutableList<Data> = mutableListOf()) : Data()

private class IntData(val value: Int) : Data()