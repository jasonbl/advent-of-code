package year2021.day20;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String[] input = InputLoader.load("/year2021/day20/input.txt").split("\n\n");
    String algorithm = input[0];
    char[][] image = initImage(input[1]);
    System.out.println("Part 1: " + partOne(algorithm, image));
    System.out.println("Part 2: " + partTwo(algorithm, image));
  }

  private static int partOne(String algorithm, char[][] image) {
    char[][] enhancedImage = enhanceImage(algorithm, image, 2);
    return countLitPixels(enhancedImage);
  }

  private static int partTwo(String algorithm, char[][] image) {
    char[][] enhancedImage = enhanceImage(algorithm, image, 50);
    return countLitPixels(enhancedImage);
  }

  private static int countLitPixels(char[][] image) {
    int count = 0;
    for (char[] row : image) {
      for (char col : row) {
        if (col == '#') {
          count++;
        }
      }
    }

    return count;
  }

  private static char[][] enhanceImage(String algorithm, char[][] image, int steps) {
    for (int i = 0; i < steps; i++) {
      image = enhanceImage(algorithm, image);
    }

    return image;
  }

  private static char[][] enhanceImage(String algorithm, char[][] image) {
    // Enhance the image
    char[][] enhancedImage = new char[image.length][image[0].length];
    for (int y = 0; y < image.length; y++) {
      for (int x = 0; x < image[0].length; x++) {
        enhancedImage[y][x] = enhancePixel(algorithm, image, x, y);
      }
    }

    // Pad the image with the border that expands out infinitely
    char paddingVal = image[0][0] == '.' ? algorithm.charAt(0) : algorithm.charAt(algorithm.length() - 1);
    return padImage(enhancedImage, paddingVal);
  }

  private static char enhancePixel(String algorithm, char[][] image, int x, int y) {
    StringBuilder binaryString = new StringBuilder();
    for (int i = y - 1; i <= y + 1; i++) {
      for (int j = x - 1; j <= x + 1; j++) {
        // If pixel is out of bounds of the image, use the infinite border value
        char pixelVal = isOutOfBounds(image, j, i) ? image[0][0] : image[i][j];
        if (pixelVal == '#') {
          binaryString.append("1");
        } else {
          binaryString.append("0");
        }
      }
    }

    int index = Integer.parseInt(binaryString.toString(), 2);
    return algorithm.charAt(index);
  }

  private static boolean isOutOfBounds(char[][] image, int x, int y) {
    return y < 0 || y >= image.length || x < 0 || x >= image[0].length;
  }

  private static char[][] initImage(String imageString) {
    String[] rows = imageString.split("\n");
    char[][] image = new char[rows.length][rows[0].length()];
    for (int y = 0; y < image.length; y++) {
      for (int x = 0; x < rows[0].length(); x++) {
        image[y][x] = rows[y].charAt(x);
      }
    }

    // Pad the image with the border that expands out infinitely
    return padImage(image, '.');
  }

  private static char[][] padImage(char[][] image, char paddingVal) {
    // Initialize padded image with padding value
    char[][] paddedImage = new char[image.length + 2][image[0].length + 2];
    fillImage(paddedImage, paddingVal);

    // Copy in original image
    int imageWidth = image[0].length + 2;
    for (int y = 1; y < paddedImage.length - 1; y++) {
      for (int x = 1; x < imageWidth - 1; x++) {
        paddedImage[y][x] = image[y - 1][x - 1];
      }
    }

    return paddedImage;
  }

  private static void fillImage(char[][] image, char value) {
    for (int y = 0; y < image.length; y++) {
      for (int x = 0; x < image[0].length; x++) {
        image[y][x] = value;
      }
    }
  }

}
