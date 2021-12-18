package year2021.day12;

import util.InputLoader;

import java.util.*;

import static java.util.Collections.singletonList;

public class Solution {

  public static void main(String[] args) {
    Map<String, List<String>> graph = initGraph();
    System.out.println("Part 1: " + partOne(graph));
    System.out.println("Part 2: " + partTwo(graph));
  }

  private static int partOne(Map<String, List<String>> graph) {
    List<Deque<String>> paths = computePathsPartOne("start", "end", graph, new HashSet<>());
    return paths.size();
  }

  private static int partTwo(Map<String, List<String>> graph) {
    List<Deque<String>> paths = computePathsPartTwo("start", "end", graph, new HashSet<>(), false);
    return paths.size();
  }

  private static List<Deque<String>> computePathsPartOne(String from, String to, Map<String, List<String>> graph,
      Set<String> visited) {
    if (from.equals(to)) {
      return singletonList(new ArrayDeque<>(singletonList(from)));
    }

    Set<String> nextVisited = new HashSet<>(visited);
    if (isSmallCave(from)) {
      nextVisited.add(from);
    }

    List<Deque<String>> paths = new ArrayList<>();
    for (String neighbor : graph.get(from)) {
      if (visited.contains(neighbor)) {
        continue;
      }

      List<Deque<String>> subPaths = computePathsPartOne(neighbor, to, graph, nextVisited);
      for (Deque<String> subPath : subPaths) {
        subPath.addFirst(from);
        paths.add(subPath);
      }
    }

    return paths;
  }

  private static List<Deque<String>> computePathsPartTwo(String from, String to, Map<String, List<String>> graph,
      Set<String> visited, boolean hasVisitedSmallTwice) {
    if (from.equals(to)) {
      return singletonList(new ArrayDeque<>(singletonList(from)));
    }

    Set<String> nextVisited = new HashSet<>(visited);
    if (isSmallCave(from)) {
      nextVisited.add(from);
    }

    List<Deque<String>> paths = new ArrayList<>();
    for (String neighbor : graph.get(from)) {
      List<Deque<String>> subPaths;
      if (visited.contains(neighbor) && !hasVisitedSmallTwice && !neighbor.equals("start") && !neighbor.equals("end")) {
        subPaths = computePathsPartTwo(neighbor, to, graph, nextVisited, true);
      } else if (visited.contains(neighbor)) {
        continue;
      } else {
        subPaths = computePathsPartTwo(neighbor, to, graph, nextVisited, hasVisitedSmallTwice);
      }

      for (Deque<String> subPath : subPaths) {
        subPath.addFirst(from);
        paths.add(subPath);
      }
    }

    return paths;
  }

  private static boolean isSmallCave(String vertex) {
    return vertex.toLowerCase().equals(vertex);
  }

  private static Map<String, List<String>> initGraph() {
    String[] lines = InputLoader.load("/year2021/day12/input.txt").split("\n");
    Map<String, List<String>> graph = new HashMap<>();
    for (String line : lines) {
      String[] vertices = line.split("-");
      addEdge(graph, vertices[0], vertices[1]);
    }

    return graph;
  }

  private static void addEdge(Map<String, List<String>> graph, String vertex1, String vertex2) {
    graph.computeIfAbsent(vertex1, (v) -> new ArrayList<>())
        .add(vertex2);
    graph.computeIfAbsent(vertex2, (v) -> new ArrayList<>())
        .add(vertex1);
  }

}
