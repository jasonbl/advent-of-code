package year2022.day16

class ValveTraverser(valveList: List<Valve>) {
    private val valves = valveList.associateBy { it.id }
    private val statesSeen = mutableMapOf<TraversalState, TraversalResult>()

    fun getMaxFlow(numMinutes: Int): TraversalResult {
        return getMaxFlow(TraversalState(valves["AA"]!!, mutableSetOf(), numMinutes))
    }

    private fun getMaxFlow(state: TraversalState): TraversalResult {
        if (statesSeen.containsKey(state)) {
            return statesSeen[state]!!
        } else if (state.minutesRemaining == 0) {
            val result = TraversalResult(0, setOf())
            statesSeen[state] = result
            return result
        }

        val curr = state.curr
        var maxResult = TraversalResult(0, setOf())
        var wasOpened = false
        if (curr.flowRate > 0 && !state.opened.contains(curr.id)) {
            val openedCopy = copySetAndAdd(state.opened, curr.id)
            val result = getMaxFlow(TraversalState(curr, openedCopy, state.minutesRemaining - 1))
            if (result.maxFlow > maxResult.maxFlow) {
                maxResult = result
                wasOpened = true
            }
        }

        for (neighbor in curr.neighborIds.map { valves[it]!! }) {
            val result = getMaxFlow(TraversalState(neighbor, state.opened, state.minutesRemaining - 1))
            if (result.maxFlow > maxResult.maxFlow) {
                maxResult = result
                wasOpened = false
            }
        }

        val maxFlow = maxResult.maxFlow + computeFlowRate(state.opened)
        val newOpened = if (wasOpened) copySetAndAdd(maxResult.opened, curr.id) else maxResult.opened
        val result = TraversalResult(maxFlow, newOpened)
        statesSeen[state] = result
        return result
    }

    private fun computeFlowRate(valveIds: Set<String>): Int {
        return valveIds.map { valves[it]!! }.sumOf { it.flowRate }
    }
}

private data class TraversalState(val curr: Valve, val opened: Set<String>, val minutesRemaining: Int)

class TraversalResult(val maxFlow: Int, val opened: Set<String>)

private fun copySetAndAdd(set: Set<String>, valveId: String): Set<String> {
    val copied = set.toMutableSet()
    copied.add(valveId)
    return copied
}