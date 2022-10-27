package year2021.day23;

import java.util.Arrays;
import java.util.Objects;

public class Room {

  private final int position;

  private final String type;

  private final Amphipod[] amphipods;

  public Room(int position, String type, Amphipod... amphipods) {
    this.position = position;
    this.type = type;
    this.amphipods = amphipods;
  }

  public int getPosition() {
    return position;
  }

  public String getType() {
    return type;
  }

  public Amphipod[] getAmphipods() {
    return amphipods;
  }

  public boolean hasAmphipodsAbove(int position) {
    for (int i = position - 1; i >= 0; i--) {
      if (amphipods[i] != null) {
        return true;
      }
    }

    return false;
  }

  public boolean containsInvalidAmphipods() {
    return Arrays.stream(amphipods)
        .filter(Objects::nonNull)
        .anyMatch((amphipod) -> !type.equals(amphipod.getType()));
  }

  public boolean isOrganized() {
    return isOrganizedBelow(0);
  }

  public boolean isOrganizedBelow(int position) {
    for (int i = position; i < amphipods.length; i++) {
      if (amphipods[i] == null || !amphipods[i].getType().equals(type)) {
        return false;
      }
    }

    return true;
  }

  public int getLowestAvailablePosition() {
    for (int i = amphipods.length - 1; i >= 0; i--) {
      if (amphipods[i] == null) {
        return i;
      }
    }

    return -1;
  }

}
