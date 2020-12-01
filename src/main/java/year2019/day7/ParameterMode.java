package year2019.day7;

public enum ParameterMode {

  POSITION(0),
  IMMEDIATE(1);

  private final int code;

  ParameterMode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static ParameterMode valueOf(int code) {
    for (ParameterMode parameterMode : ParameterMode.values()) {
      if (parameterMode.getCode() == code) {
        return parameterMode;
      }
    }

    throw new IllegalArgumentException("Unknown code: " + code);
  }

}
