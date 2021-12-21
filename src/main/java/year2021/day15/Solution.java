package year2021.day15;

import util.InputLoader;

import java.util.*;

public class Solution {

  public static void main(String[] args) {
    System.out.println("Part 1: " + computeShortestPath(initGraphPartOne()));
    System.out.println("Part 2: " + computeShortestPath(initGraphPartTwo()));
  }

  private static int computeShortestPath(Node[][] graph) {
    graph[0][0].setDistanceTo(0);

    PriorityQueue<Node> toVisit = new PriorityQueue<>();
    Set<Node> visited = new HashSet<>();
    for (Node[] row : graph) {
      toVisit.addAll(Arrays.asList(row));
    }

    while (!toVisit.isEmpty()) {
      Node node = toVisit.poll();
      for (Node neighbor : getNeighbors(node, graph)) {
        if (visited.contains(neighbor)) {
          continue;
        }

        int distanceToNeighbor = node.getDistanceTo() + neighbor.getRisk();
        if (distanceToNeighbor < neighbor.getDistanceTo()) {
          neighbor.setDistanceTo(distanceToNeighbor);
          toVisit.remove(neighbor);
          toVisit.add(neighbor);
        }
      }

      visited.add(node);
    }

    Node target = graph[graph.length - 1][graph[0].length - 1];
    return target.getDistanceTo();
  }

  private static List<Node> getNeighbors(Node node, Node[][] graph) {
    List<Node> neighbors = new ArrayList<>();
    int x = node.getX();
    int y = node.getY();

    if (x > 0) {
      neighbors.add(graph[y][x - 1]);
    }

    if (x < graph[0].length - 1) {
      neighbors.add(graph[y][x + 1]);
    }

    if (y > 0) {
      neighbors.add(graph[y - 1][x]);
    }

    if (y < graph.length - 1) {
      neighbors.add(graph[y + 1][x]);
    }

    return neighbors;
  }

  private static Node[][] initGraphPartOne() {
    String[] lines = InputLoader.load("/year2021/day15/input.txt").split("\n");
    Node[][] graph = new Node[lines.length][];
    for (int y = 0; y < lines.length; y++) {
      graph[y] = new Node[lines[y].length()];
      for (int x = 0; x < lines[y].length(); x++) {
        int risk = Integer.parseInt("" + lines[y].charAt(x));
        graph[y][x] = new Node(x, y, risk);
      }
    }

    return graph;
  }

  private static Node[][] initGraphPartTwo() {
    String[] lines = InputLoader.load("/year2021/day15/input.txt").split("\n");
    Node[][] graph = new Node[lines.length * 5][];
    for (int y = 0; y < graph.length; y++) {
      int linesRow = y % lines.length;
      int linesColLength = lines[linesRow].length();
      graph[y] = new Node[linesColLength * 5];
      for (int x = 0; x < linesColLength * 5; x++) {
        int linesCol = x % linesColLength;
        int remainder = x / linesColLength + y / lines.length;
        int risk = Integer.parseInt("" + lines[linesRow].charAt(linesCol)) + remainder;
        if (risk > 9) {
          risk = risk - 9;
        }

        graph[y][x] = new Node(x, y, risk);
      }
    }

    return graph;
  }

}
