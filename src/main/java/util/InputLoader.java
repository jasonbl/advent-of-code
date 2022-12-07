package util;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InputLoader {

  public static String load(String fullPath) {
    try (InputStream inputStream = InputLoader.class.getResourceAsStream(fullPath)) {
      return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    } catch (Exception e) {
      System.out.println("Failed to read file at path: " + fullPath);
      throw new RuntimeException(e);
    }
  }

}
