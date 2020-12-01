package year2019.day13;

public class GameObject {

  private final int x;
  private final int y;
  private final Sprite sprite;

  public GameObject(int x, int y, int spriteCode) {
    this.x = x;
    this.y = y;
    this.sprite = Sprite.valueOf(spriteCode);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Sprite getSprite() {
    return sprite;
  }

}
