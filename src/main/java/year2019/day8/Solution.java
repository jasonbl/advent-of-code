package year2019.day8;

import util.InputLoader;

public class Solution {

  public static void main(String[] args) {
    String input = InputLoader.load("/year2019/day8/input.txt");
    int[] inputInts = new int[input.length()];
    for (int i = 0; i < input.length(); i++) {
      inputInts[i] = Integer.parseInt(input.charAt(i) + "");
    }

    int[][][] imageData = buildImage(inputInts, 25, 6);
    int partOne = partOne(imageData);
    System.out.println("Part one: " + partOne);
    partTwo(imageData);
  }

  private static int partOne(int[][][] imageData) {
    int fewestZeros = Integer.MAX_VALUE;
    int depthOfFewestZeros = -1;
    for (int i = 0; i < imageData.length; i++) {
      int zeroCount = count(imageData[i], 0);

      if (zeroCount < fewestZeros) {
        fewestZeros = zeroCount;
        depthOfFewestZeros = i;
      }
    }

    int oneCount = count(imageData[depthOfFewestZeros], 1);
    int twoCount = count(imageData[depthOfFewestZeros], 2);
    return oneCount * twoCount;
  }

  private static void partTwo(int[][][] imageData) {
    int[][] flattenedImage = new int[imageData[0].length][imageData[0][0].length];
    for (int y = 0; y < imageData[0].length; y++) {
      for (int x = 0; x < imageData[0][0].length; x++) {
        for (int z = 0; z < imageData.length; z++) {
          if (imageData[z][y][x] != 2) {
            flattenedImage[y][x] = imageData[z][y][x];
            break;
          }
        }
      }
    }

    for (int y = 0; y < flattenedImage.length; y++) {
      for (int x = 0; x < flattenedImage[0].length; x++) {
        if (flattenedImage[y][x] == 1) {
          System.out.print(flattenedImage[y][x]);
        } else {
          System.out.print(" ");
        }
      }
      System.out.println();
    }
  }

  private static int count(int[][] layer, int numberToCount) {
    int count = 0;
    for (int y = 0; y < layer.length; y++) {
      for (int x = 0; x < layer[y].length; x++) {
        if (layer[y][x] == numberToCount) {
          count++;
        }
      }
    }

    return count;
  }

  private static int[][][] buildImage(int[] inputData, int width, int height) {
    int layerSize = width * height;
    int depth = inputData.length / layerSize;
    int[][][] imageData = new int[depth][height][width];
    int inputIndex = 0;
    for (int i = 0; i < depth; i++) {
      for (int j = 0; j < height; j++) {
        for (int k = 0; k < width; k++) {
          imageData[i][j][k] = inputData[inputIndex++];
        }
      }
    }

    return imageData;
  }

}
