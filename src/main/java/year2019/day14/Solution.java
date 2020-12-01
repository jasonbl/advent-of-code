package year2019.day14;

import util.InputLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution {

  public static void main(String[] args) {
    String[] equations = InputLoader.load("/year2019/day14/input.txt")
        .split("\n");

    Map<String, Resource> outputIdMap = new HashMap<>();
    Map<Resource, List<Resource>> resourceConversions = new HashMap<>();
    for (String equation : equations) {
      String[] equationHalves = equation.split(" => ");
      List<Resource> inputs = Arrays.stream(equationHalves[0].split(", "))
          .map(Resource::fromString)
          .collect(Collectors.toList());
      Resource output = Resource.fromString(equationHalves[1]);
      outputIdMap.put(output.getId(), output);
      resourceConversions.put(output, inputs);
    }

    System.out.println("Part one: " + countOrePerFuel(outputIdMap, resourceConversions));
    System.out.println("Part two: " + produceFuelFaster(outputIdMap, resourceConversions, 1_000_000_000_000L));
  }

  private static long countOrePerFuel(Map<String, Resource> outputIdMap,
                                      Map<Resource, List<Resource>> resourceConversions) {
    return getOreCount(outputIdMap.get("FUEL"), outputIdMap, resourceConversions, new HashMap<>());
  }

  private static long produceFuelFaster(Map<String, Resource> outputIdMap,
                                        Map<Resource, List<Resource>> resourceConversions,
                                        long oreAvailable) {
    // The ore required per fuel can only get lower (or stay the same) after the first fuel produced (we'll
    // have leftover resources on subsequent iterations)
    long fuelLowerBound = oreAvailable / countOrePerFuel(outputIdMap, resourceConversions);
    long fuelUpperBound = 2 * fuelLowerBound;
    while (getOreCount(new Resource("FUEL", fuelUpperBound), outputIdMap, resourceConversions, new HashMap<>())
        < oreAvailable) {
      fuelUpperBound *= 2;
    }

    while (fuelLowerBound < fuelUpperBound - 1) {
      long fuelToProduce = (fuelUpperBound + fuelLowerBound) / 2;
      long oreForFuel =
          getOreCount(new Resource("FUEL", fuelToProduce), outputIdMap, resourceConversions, new HashMap<>());
      if (oreForFuel < oreAvailable) {
        fuelLowerBound = fuelToProduce;
      } else if (oreForFuel > oreAvailable) {
        fuelUpperBound = fuelToProduce;
      } else {
        return fuelToProduce;
      }
    }

    return fuelLowerBound;
  }

  private static long produceFuel(Map<String, Resource> outputIdMap,
                                  Map<Resource, List<Resource>> resourceConversions,
                                  long oreAvailable) {
    long oreUsed = 0;
    long fuelCount = 0;
    // Continue to pass leftover resources back into the fuel production process
    Map<String, Resource> availableResources = new HashMap<>();
    while (true) {
      oreUsed += getOreCount(outputIdMap.get("FUEL"), outputIdMap, resourceConversions, availableResources);

      // If we've exceeded the amount of ore available, return the amount of fuel produced before this iteration
      if (oreUsed > oreAvailable) {
        return fuelCount;
      }

      // If availableResources has returned to its initial state (an empty map), the ore usage per iteration will
      // start to repeat, so we can skip to the beginning of the last repeat and continue iterating.
      // We check for fuelCount != 0 to ignore the initial iteration of the loop.
      if (availableResources.isEmpty() && fuelCount != 0) {
        long numberOfRepeats = oreAvailable / oreUsed;
        oreUsed = numberOfRepeats * oreUsed;
        fuelCount = numberOfRepeats * (fuelCount + 1);
        continue;
      }

      fuelCount++;
    }
  }

  private static long getOreCount(Resource resourceToMake,
                                  Map<String, Resource> outputIdMap,
                                  Map<Resource, List<Resource>> equations,
                                  Map<String, Resource> leftoverResources) {
    // The amount of ore required to create ore is just the quantity specified
    if (resourceToMake.getId().equals("ORE")) {
      return resourceToMake.getQuantity();
    }

    long amountToProduce = resourceToMake.getQuantity();

    // Drain from any leftover resources first
    if (leftoverResources.containsKey(resourceToMake.getId())) {
      Resource leftover = leftoverResources.get(resourceToMake.getId());
      if (amountToProduce < leftover.getQuantity()) {
        Resource newLeftover =
            new Resource(leftover.getId(), leftover.getQuantity() - amountToProduce);
        leftoverResources.put(leftover.getId(), newLeftover);
        return 0;
      } else if (amountToProduce == leftover.getQuantity()) {
        leftoverResources.remove(leftover.getId());
        return 0;
      } else {
        leftoverResources.remove(leftover.getId());
        amountToProduce -= leftover.getQuantity();
      }
    }

    // We didn't have enough leftover resources to use, so produce more
    Resource outputResource = outputIdMap.get(resourceToMake.getId());
    List<Resource> inputs = equations.get(outputResource);
    long oreNeededForInputs = 0;
    // Determine the multiplier required to create enough of the output
    long multiplier = (long) Math.ceil((double) amountToProduce / outputResource.getQuantity());
    for (Resource inputResource : inputs) {
      // Adjust the quantity of inputs needed to create the required output
      Resource correctedInput = new Resource(inputResource.getId(), multiplier * inputResource.getQuantity());
      oreNeededForInputs += getOreCount(correctedInput, outputIdMap, equations, leftoverResources);
    }

    // If the actual quantity of the output produced is larger than what was needed, add to leftovers
    long quantityOfOutput = multiplier * outputResource.getQuantity();
    if (quantityOfOutput > amountToProduce) {
      leftoverResources.put(outputResource.getId(),
          new Resource(outputResource.getId(), quantityOfOutput - amountToProduce));
    }

    return oreNeededForInputs;
  }

}
