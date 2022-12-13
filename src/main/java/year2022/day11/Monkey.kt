package year2022.day11

class Monkey(
    private val items: ArrayDeque<Long>,
    private val onInspect: (Long) -> Long,
    val getNextMonkey: (Long) -> Int,
    val testVal: Int,
) {
    var numInspects = 0L

    fun hasItems(): Boolean {
        return items.isNotEmpty()
    }

    fun inspectItem(): Long {
        numInspects++
        return onInspect(items[0])
    }

    fun throwItem(value: Long, monkey: Monkey) {
        items.removeFirst()
        monkey.items.addLast(value)
    }

    companion object {
        private val NUMBER_REGEX = Regex("\\d+")
        private val OPERATION_REGEX = Regex("new = old ([+\\-*/]) (\\d+|old)")

        fun fromString(input: String): Monkey {
            val lines = input.split("\n")
            val items = NUMBER_REGEX.findAll(lines[1]).map { it.value.toLong() }.toCollection(ArrayDeque())
            val operationMatch = OPERATION_REGEX.find(lines[2])!!
            val onInspect = getOnInspect(operationMatch.groupValues[1][0], operationMatch.groupValues[2])
            val testVal = NUMBER_REGEX.find(lines[3])!!.value.toInt()
            val trueVal = NUMBER_REGEX.find(lines[4])!!.value.toInt()
            val falseVal = NUMBER_REGEX.find(lines[5])!!.value.toInt()
            val getNextMonkey = { value: Long -> if (value % testVal == 0L) trueVal else falseVal }
            return Monkey(items, onInspect, getNextMonkey, testVal)
        }

        private fun getOnInspect(operation: Char, value: String): (Long) -> Long {
            val useInputVal = value == "old"
            return when (operation) {
                '+' -> { input -> input + if (useInputVal) input else value.toLong() }
                '-' -> { input -> input - if (useInputVal) input else value.toLong()  }
                '*' -> { input -> input * if (useInputVal) input else value.toLong()  }
                '/' -> { input -> input / if (useInputVal) input else value.toLong()  }
                else -> throw IllegalArgumentException("Invalid operation: $operation")
            }
        }
    }
}