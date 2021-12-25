package year2021.day18;

public class Node {

  private Node left;

  private Node right;

  private Integer val;

  private Node prevVal;

  private Node nextVal;

  public Node() {
  }

  public Node(int val) {
    this.val = val;
  }

  public Node getLeft() {
    return left;
  }

  public void setLeft(Node left) {
    this.left = left;
  }

  public Node getRight() {
    return right;
  }

  public void setRight(Node right) {
    this.right = right;
  }

  public Integer getVal() {
    return val;
  }

  public void setVal(Integer val) {
    this.val = val;
  }

  public boolean isValueNode() {
    return val != null;
  }

  public Node getPrevVal() {
    return prevVal;
  }

  public void setPrevVal(Node prevVal) {
    this.prevVal = prevVal;
  }

  public Node getNextVal() {
    return nextVal;
  }

  public void setNextVal(Node nextVal) {
    this.nextVal = nextVal;
  }
}
