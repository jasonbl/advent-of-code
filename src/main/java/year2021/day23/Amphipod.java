package year2021.day23;

public class Amphipod {

  private final int id;

  private final String type;

  private String roomType;

  private Integer roomPosition;

  private Integer hallPosition;

  public Amphipod(int id, String type, String roomType, Integer roomPosition, Integer hallPosition) {
    this.id = id;
    this.type = type;
    this.roomType = roomType;
    this.roomPosition = roomPosition;
    this.hallPosition = hallPosition;
  }

  public Amphipod copy() {
    return new Amphipod(id, type, roomType, roomPosition, hallPosition);
  }

  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getRoomType() {
    return roomType;
  }

  public Integer getRoomPosition() {
    return roomPosition;
  }

  public Integer getHallPosition() {
    return hallPosition;
  }

  public boolean isInHall() {
    return hallPosition != null;
  }

  public boolean isInRoom() {
    return roomPosition != null;
  }

  public void moveToHall(int position) {
    hallPosition = position;
    roomType = null;
    roomPosition = null;
  }

  public void moveToRoom(int position) {
    roomPosition = position;
    roomType = type;
    hallPosition = null;
  }

}
