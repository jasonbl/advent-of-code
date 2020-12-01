package year2019.day14;

import java.util.Objects;

public class Resource {

  private final String id;

  private final long quantity;

  public Resource(String id, long quantity) {
    this.id = id;
    this.quantity = quantity;
  }

  public static Resource fromString(String value) {
    String[] split = value.split(" ");
    return new Resource(split[1], Integer.parseInt(split[0]));
  }

  public String getId() {
    return id;
  }

  public long getQuantity() {
    return quantity;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    Resource that = (Resource) obj;
    return this.getId().equals(that.getId())
        && this.getQuantity() == that.getQuantity();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, quantity);
  }

}
