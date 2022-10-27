package year2021.day23;

import java.util.HashMap;
import java.util.Map;

public class AmphipodOrganizer {

  private final Map<Burrow, Integer> cache;

  private final Burrow burrow;

  public AmphipodOrganizer(Burrow burrow) {
    this.cache = new HashMap<>();
    this.burrow = burrow;
  }

  public int organize() {
    return organize(burrow);
  }

  private int organize(Burrow burrow) {
    Integer cost = cache.get(burrow);
    if (cost != null) {
      return cost;
    }

    // Burrow is already organized, so there's no extra cost to organize it
    if (burrow.isOrganized()) {
      return 0;
    }

    // Iterate through all possible moves and call organize() on the burrow after making that move
    // If cost of organize() is -1, ignore it (it's not possible to organize after that move)
    // Otherwise, compute the minimum cost based on the current move's cost + organize() cost
    int minCost = Integer.MAX_VALUE;
    for (Amphipod amphipod : burrow.getAmphipods().values()) {
      if (amphipod.isInRoom()) {
        for (int position : burrow.getPossibleHallMoves(amphipod)) {
          // Create a deep copy so manipulations of the burrow don't propagate across recursive calls
          Burrow burrowCopy = burrow.copy();
          int moveCost = burrowCopy.moveToHall(amphipod.getId(), position);
          int organizeCost = organize(burrowCopy);
          if (organizeCost != -1) {
            minCost = Math.min(minCost, moveCost + organizeCost);
          }
        }
      } else if (burrow.canMoveToRoom(amphipod)) {
        // Create a deep copy so manipulations of the burrow don't propagate across recursive calls
        Burrow burrowCopy = burrow.copy();
        int moveCost = burrowCopy.moveToRoom(amphipod.getId());
        int organizeCost = organize(burrowCopy);
        if (organizeCost != -1) {
          minCost = Math.min(minCost, moveCost + organizeCost);
        }
      }
    }

    int result = minCost == Integer.MAX_VALUE ? -1 : minCost;
    cache.put(burrow, result);
    return result;
  }

}
