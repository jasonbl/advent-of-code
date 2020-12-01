package year2019.day13;

public enum Sprite {

  EMPTY(0, "."),
  WALL(1, "|"),
  BLOCK(2, "X"),
  PADDLE(3, "_"),
  BALL(4, "o");

  private final int code;
  private final String icon;

  Sprite(int code, String icon) {
    this.code = code;
    this.icon = icon;
  }

  public static Sprite valueOf(int code) {
    for (Sprite sprite : Sprite.values()) {
      if (sprite.getCode() == code) {
        return sprite;
      }
    }

    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public int getCode() {
    return code;
  }

  public String getIcon() {
    return icon;
  }

}
