package year2020.day7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BagRule {

  private final String id;

  private final Set<String> childBagIds;

  private final List<BagQuantity> childBags;

  public BagRule(String id) {
    this.id = id;
    this.childBagIds = new HashSet<>();
    this.childBags = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public Set<String> getChildBagIds() {
    return childBagIds;
  }

  public List<BagQuantity> getChildBags() {
    return childBags;
  }

  public void addChildBag(BagQuantity bagQuantity) {
    if (childBagIds.contains(bagQuantity.getBagId())) {
      return;
    }

    childBagIds.add(bagQuantity.getBagId());
    childBags.add(bagQuantity);
  }



}
