package year2019.day6;

import util.InputLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

  public static void main (String[] args) {
    String[] inputs = InputLoader.load("/year2019/day6/input.txt")
        .split("\n");

    Map<String, TreeNode> orbitMap = buildOrbitMap(inputs);
    TreeNode centerOfMass = orbitMap.get("COM");
    System.out.println(calculateOrbitCount(centerOfMass));

    TreeNode you = orbitMap.get("YOU");
    TreeNode santa = orbitMap.get("SAN");
    System.out.println(findShortestPath(you, santa));
  }

  private static Map<String, TreeNode> buildOrbitMap(String[] orbits) {
    Map<String, TreeNode> orbitMap = new HashMap<>();
    for (String orbit : orbits) {
      int splitIndex = orbit.indexOf(')');
      String id = orbit.substring(0, splitIndex);
      String orbiterId = orbit.substring(splitIndex + 1);
      TreeNode orbiter = orbitMap.computeIfAbsent(orbiterId, TreeNode::new);
      TreeNode planet = orbitMap.computeIfAbsent(id, TreeNode::new);
      planet.addOrbiter(orbiter);
      orbiter.setOrbits(planet);
    }

    return orbitMap;
  }

  private static int calculateOrbitCount(TreeNode centerOfMass) {
    return calculateOrbitCount(centerOfMass, 0);
  }

  private static int calculateOrbitCount(TreeNode node, int depth) {
    int numOrbits = depth;
    for (TreeNode orbiter : node.getOrbitedBy()) {
      numOrbits += calculateOrbitCount(orbiter, depth + 1);
    }

    return numOrbits;
  }

  private static int findShortestPath(TreeNode one, TreeNode two) {
    List<TreeNode> pathFromOne = buildPathToRoot(one);
    Map<String, Integer> pathFromOneHeightMap = new HashMap<>();
    int height = 0;
    for (TreeNode node : pathFromOne) {
      pathFromOneHeightMap.put(node.getId(), height++);
    }

    List<TreeNode> pathFromTwo = buildPathToRoot(two);
    String leastCommonAncestor = null;
    int pathTwoHeight = 0;
    for (TreeNode node : pathFromTwo) {
      if (pathFromOneHeightMap.containsKey(node.getId())) {
        leastCommonAncestor = node.getId();
        break;
      }

      pathTwoHeight++;
    }

    // -2 to exclude first edges out of one and two
    return pathFromOneHeightMap.get(leastCommonAncestor) + pathTwoHeight - 2;
  }

  private static List<TreeNode> buildPathToRoot(TreeNode node) {
    List<TreeNode> pathToRoot = new ArrayList<>();
    while (node != null) {
      pathToRoot.add(node);
      node = node.getOrbits();
    }

    return pathToRoot;
  }

  private static class TreeNode {

    private final String id;

    private TreeNode orbits;

    private final List<TreeNode> orbitedBy;

    public TreeNode(String id) {
      this.id = id;
      this.orbitedBy = new ArrayList<>();
    }

    public String getId() {
      return id;
    }

    public List<TreeNode> getOrbitedBy() {
      return orbitedBy;
    }

    public void addOrbiter(TreeNode orbiter) {
      orbitedBy.add(orbiter);
    }

    public TreeNode getOrbits() {
      return orbits;
    }

    public void setOrbits(TreeNode orbits) {
      this.orbits = orbits;
    }
  }

}
