package year2021.day16;

import java.util.List;

public class Packet {

  private final long version;

  private final long type;

  private final long length;

  private final long value;

  private final List<Packet> subPackets;

  private Packet(Builder builder) {
    this.version = builder.version;
    this.type = builder.type;
    this.length = builder.length;
    this.value = builder.value;
    this.subPackets = builder.subPackets;
  }

  public long getVersion() {
    return version;
  }

  public long getType() {
    return type;
  }

  public long getLength() {
    return length;
  }

  public long getValue() {
    return value;
  }

  public List<Packet> getSubPackets() {
    return subPackets;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private long version;

    private long type;

    private long length;

    private long value;

    private List<Packet> subPackets;

    public Builder version(long version) {
      this.version = version;
      return this;
    }

    public Builder type(long type) {
      this.type = type;
      return this;
    }

    public Builder length(long length) {
      this.length = length;
      return this;
    }

    public Builder value(long value) {
      this.value = value;
      return this;
    }

    public Builder subPackets(List<Packet> subPackets) {
      this.subPackets = subPackets;
      return this;
    }

    public Packet build() {
      return new Packet(this);
    }

  }

}
