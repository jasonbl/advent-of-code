package year2020.day7;

public class BagQuantity {

  private final String bagId;

  private final int quantity;

  public BagQuantity(String bagId, int quantity) {
    this.bagId = bagId;
    this.quantity = quantity;
  }

  public String getBagId() {
    return bagId;
  }

  public int getQuantity() {
    return quantity;
  }

}
