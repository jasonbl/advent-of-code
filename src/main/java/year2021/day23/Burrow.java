package year2021.day23;

import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Burrow {

  private static final String[] ROOM_TYPES = new String[] { "A", "B", "C", "D" };

  private static final Map<String, Integer> PER_MOVE_COSTS = ImmutableMap.<String, Integer>builder()
      .put("A", 1)
      .put("B", 10)
      .put("C", 100)
      .put("D", 1000)
      .build();

  private static final Set<Integer> VALID_HALL_POSITIONS = Stream.of(0, 1, 3, 5, 7, 9, 10)
      .collect(Collectors.toSet());

  private final Map<String, Room> rooms;

  private final Amphipod[] hall;

  private final Map<Integer, Amphipod> amphipods;

  public Burrow(String[][] layout) {
    this(buildAmphipods(layout));
  }

  private Burrow(List<Amphipod> amphipodList) {
    int roomSize = amphipodList.size() / 4;
    rooms = ImmutableMap.<String, Room>builder()
        .put("A", new Room(2, "A", new Amphipod[roomSize]))
        .put("B", new Room(4, "B", new Amphipod[roomSize]))
        .put("C", new Room(6, "C", new Amphipod[roomSize]))
        .put("D", new Room(8, "D", new Amphipod[roomSize]))
        .build();

    hall = new Amphipod[11];

    amphipods = new HashMap<>();
    for (Amphipod amphipod : amphipodList) {
      amphipods.put(amphipod.getId(), amphipod);
      if (amphipod.isInHall()) {
        hall[amphipod.getHallPosition()] = amphipod;
      } else {
        requireNonNull(rooms.get(amphipod.getRoomType()))
            .getAmphipods()[amphipod.getRoomPosition()] = amphipod;
      }
    }
  }

  public Burrow copy() {
    List<Amphipod> amphipodCopies = amphipods.values()
        .stream()
        .map(Amphipod::copy)
        .collect(Collectors.toList());
    return new Burrow(amphipodCopies);
  }

  public Map<Integer, Amphipod> getAmphipods() {
    return amphipods;
  }

  public List<Integer> getPossibleHallMoves(Amphipod amphipod) {
    return IntStream.range(0, hall.length)
        .filter((i) -> canMoveToHall(amphipod, i))
        .boxed()
        .collect(Collectors.toList());
  }

  private boolean canMoveToHall(Amphipod amphipod, int position) {
    // Check whether the amphipod is already in the hall or the hall position is invalid
    if (amphipod.isInHall() || !VALID_HALL_POSITIONS.contains(position)) {
      return false;
    }

    // Check whether the amphipod is blocked by another above it, or is already in a valid spot
    Room room = rooms.get(amphipod.getRoomType());
    if (room.hasAmphipodsAbove(amphipod.getRoomPosition()) || room.isOrganizedBelow(amphipod.getRoomPosition())) {
      return false;
    }

    // Index of the room's exit in the hall
    int roomPosition = room.getPosition();

    // Check if the hall is clear between the room's exit position and the desired hall position
    for (int i = Math.min(position, roomPosition); i <= Math.max(position, roomPosition); i++) {
      if (hall[i] != null) {
        return false;
      }
    }

    return true;
  }

  // Returns the cost of the move
  public int moveToHall(int amphipodId, int position) {
    Amphipod amphipod = amphipods.get(amphipodId);
    if (!canMoveToHall(amphipod, position)) {
      throw new IllegalArgumentException("Can't move amphipod to hall");
    }

    // Remove the amphipod from the room
    Room currentRoom = rooms.get(amphipod.getRoomType());
    int currentRoomPosition = amphipod.getRoomPosition();
    currentRoom.getAmphipods()[currentRoomPosition] = null;

    // Add the amphipod to the hall
    hall[position] = amphipod;
    amphipod.moveToHall(position);

    int spacesMoved = Math.abs(currentRoom.getPosition() - position) + currentRoomPosition + 1;
    return spacesMoved * PER_MOVE_COSTS.get(amphipod.getType());
  }

  public boolean canMoveToRoom(Amphipod amphipod) {
    if (amphipod.isInRoom()) {
      return false;
    }

    Room room = rooms.get(amphipod.getType());
    if (room.containsInvalidAmphipods()) {
      return false;
    }

    // Check if the hall is clear between the current position and the desired room's entrance
    int currentPosition = amphipod.getHallPosition();
    int roomPosition = room.getPosition();
    for (int i = Math.min(currentPosition, roomPosition); i <= Math.max(currentPosition, roomPosition); i++) {
      if (hall[i] != null && hall[i] != amphipod) {
        return false;
      }
    }

    return true;
  }

  // Returns the cost of the move
  public int moveToRoom(int amphipodId) {
    Amphipod amphipod = amphipods.get(amphipodId);
    if (!canMoveToRoom(amphipod)) {
      throw new IllegalArgumentException("Can't move amphipod to room");
    }

    // Remove the amphipod from the hall
    int currentPosition = amphipod.getHallPosition();
    hall[currentPosition] = null;

    // Add the amphipod to the room
    Room room = rooms.get(amphipod.getType());
    int lowestRoomPosition = room.getLowestAvailablePosition();
    room.getAmphipods()[lowestRoomPosition] = amphipod;
    amphipod.moveToRoom(lowestRoomPosition);

    int spacesMoved = Math.abs(room.getPosition() - currentPosition) + lowestRoomPosition + 1;
    return spacesMoved * PER_MOVE_COSTS.get(amphipod.getType());
  }

  public boolean isOrganized() {
    return rooms.values()
        .stream()
        .allMatch(Room::isOrganized);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Burrow burrow = (Burrow) o;
    return Objects.equals(toString(), burrow.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(toString());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Amphipod amphipod : hall) {
      String s = amphipod == null ? "." : amphipod.getType();
      sb.append(s);
    }
    sb.append("\n");

    for (int i = 0; i < amphipods.size() / 4; i++) {
      for (int j = 0; j < hall.length; j++) {
        if (VALID_HALL_POSITIONS.contains(j)) {
          sb.append(" ");
        } else {
          Room room = rooms.get(ROOM_TYPES[j / 2 - 1]);
          String s = room.getAmphipods()[i] == null ? "." : room.getAmphipods()[i].getType();
          sb.append(s);
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  private static List<Amphipod> buildAmphipods(String[][] layout) {
    List<Amphipod> amphipods = new ArrayList<>();
    int id = 0;
    for (int i = 0; i < layout.length; i++) {
      for (int j = 0; j < layout[i].length; j++) {
        amphipods.add(new Amphipod(id++, layout[i][j], ROOM_TYPES[i], j, null));
      }
    }

    return amphipods;
  }

}
