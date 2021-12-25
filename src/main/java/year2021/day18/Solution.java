package year2021.day18;

import util.InputLoader;

import java.util.Stack;

public class Solution {

  public static void main(String[] args) {
    String[] numbers = InputLoader.load("/year2021/day18/input.txt").split("\n");
    System.out.println("Part 1: " + partOne(numbers));
    System.out.println("Part 2: " + partTwo(numbers));
  }

  private static int partOne(String[] numbers) {
    Node sum = parseNumber(numbers[0]);
    for (int i = 1; i < numbers.length; i++) {
      sum = add(sum, parseNumber(numbers[i]));
      reduce(sum);
    }

    return computeMagnitude(sum);
  }

  private static int partTwo(String[] numbers) {
    int maxSum = -1;
    for (int i = 0; i < numbers.length; i++) {
      for (int j = 0; j < numbers.length; j++) {
        if (i == j) {
          continue;
        }

        Node sum = add(parseNumber(numbers[i]), parseNumber(numbers[j]));
        reduce(sum);
        maxSum = Math.max(maxSum, computeMagnitude(sum));
      }
    }

    return maxSum;
  }

  private static Node add(Node left, Node right) {
    Node sum = new Node();
    sum.setLeft(left);
    sum.setRight(right);
    Node rightLeaf = getRightLeaf(left);
    Node leftLeaf = getLeftLeaf(right);
    rightLeaf.setNextVal(leftLeaf);
    leftLeaf.setPrevVal(rightLeaf);
    return sum;
  }

  private static Node getRightLeaf(Node node) {
    while (!node.isValueNode()) {
      node = node.getRight();
    }

    return node;
  }

  private static Node getLeftLeaf(Node node) {
    while (!node.isValueNode()) {
      node = node.getLeft();
    }

    return node;
  }

  private static void reduce(Node node) {
    do {
      explodeAll(node, 0);
    } while (splitFirst(node));
  }

  private static void explodeAll(Node node, int depth) {
    if (node.isValueNode()) {
      return;
    }

    if (depth == 4) {
      Node left = node.getLeft();
      Node right = node.getRight();
      if (left.getPrevVal() != null) {
        int newVal = left.getPrevVal().getVal() + left.getVal();
        left.getPrevVal().setVal(newVal);
        left.getPrevVal().setNextVal(node);
        node.setPrevVal(left.getPrevVal());
      }

      if (right.getNextVal() != null) {
        int newVal = right.getNextVal().getVal() + right.getVal();
        right.getNextVal().setVal(newVal);
        right.getNextVal().setPrevVal(node);
        node.setNextVal(right.getNextVal());
      }

      node.setLeft(null);
      node.setRight(null);
      node.setVal(0);
      return;
    }

    explodeAll(node.getLeft(), depth + 1);
    explodeAll(node.getRight(), depth + 1);
  }

  private static boolean splitFirst(Node node) {
    if (node.isValueNode()) {
      if (node.getVal() >= 10) {
        Node left = new Node(node.getVal() / 2);
        Node right = new Node((int) Math.ceil(node.getVal() / 2.0));
        left.setNextVal(right);
        right.setPrevVal(left);

        if (node.getPrevVal() != null) {
          left.setPrevVal(node.getPrevVal());
          node.getPrevVal().setNextVal(left);
        }

        if (node.getNextVal() != null) {
          right.setNextVal(node.getNextVal());
          node.getNextVal().setPrevVal(right);
        }

        node.setLeft(left);
        node.setRight(right);
        node.setVal(null);
        node.setPrevVal(null);
        node.setNextVal(null);
        return true;
      } else {
        return false;
      }
    }

    return splitFirst(node.getLeft()) || splitFirst(node.getRight());
  }

  private static int computeMagnitude(Node node) {
    if (node.isValueNode()) {
      return node.getVal();
    }

    return 3 * computeMagnitude(node.getLeft()) + 2 * computeMagnitude(node.getRight());
  }

  private static Node parseNumber(String number) {
    Stack<Node> pairStack = new Stack<>();
    Node prevValueNode = null;
    Node root = null;
    for (char c : number.toCharArray()) {
      if (c == '[') {
        pairStack.add(new Node());
      } else if (c == ']') {
        Node pair = pairStack.pop();
        if (pairStack.size() == 0) {
          root = pair;
          break;
        }

        Node parent = pairStack.peek();
        if (parent.getLeft() == null) {
          parent.setLeft(pair);
        } else {
          parent.setRight(pair);
        }
      } else if (c == ',') {
        continue;
      } else {
        Node pair = pairStack.peek();
        Node valueNode = new Node(Integer.parseInt("" + c));
        if (prevValueNode != null) {
          valueNode.setPrevVal(prevValueNode);
          prevValueNode.setNextVal(valueNode);
        }

        prevValueNode = valueNode;
        if (pair.getLeft() == null) {
          pair.setLeft(valueNode);
        } else {
          pair.setRight(valueNode);
        }
      }
    }

    return root;
  }


}
