package year2022.day16

private val REGEX = Regex("^Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.*)$")

data class Valve(val id: String, val flowRate: Int, val neighborIds: List<String>) {
    companion object {
        fun fromString(value: String): Valve {
            val match = REGEX.matchEntire(value)!!
            val id = match.groupValues[1]
            val flowRate = match.groupValues[2].toInt()
            val neighborIds = match.groupValues[3].split(", ")
            return Valve(id, flowRate, neighborIds)
        }
    }
}