package year2021.day22;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class CuboidTest {

  @Test
  public void testSubtract_SameCuboid() {
    // Remove an identical cuboid
    Cuboid cuboid = new Cuboid(true, 10, 100, -50, 142, 100, 125);
    Cuboid toRemove = new Cuboid(false, 10, 100, -50, 142, 100, 125);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertTrue(remainingCuboids.isEmpty());
  }

  @Test
  public void testSubtract_EncompassingCuboid() {
    // Remove a cuboid that contains this cuboid
    Cuboid cuboid = new Cuboid(true, 10, 100, -50, 142, 100, 125);
    Cuboid toRemove = new Cuboid(false, 9, 101, -51, 143, 99, 126);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertTrue(remainingCuboids.isEmpty());
  }

  @Test
  public void testSubtract_NonOverlapping() {
    // Remove a cuboid that doesn't overlap this cuboid
    Cuboid cuboid = new Cuboid(true, 10, 100, -50, 142, 100, 125);
    Cuboid toRemove = new Cuboid(false, 1000, 1020, 1500, 1800, 3000, 3005);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertEquals(remainingCuboids.size(), 1);
    assertEquals(remainingCuboids.get(0), cuboid);
  }

  @Test
  public void testSubtract_EndSlice1D() {
    // Remove a cuboid at the end of the xRange
    Cuboid cuboid = new Cuboid(true, 10, 100, -50, 142, 100, 125);
    Cuboid toRemove = new Cuboid(false, 12, 100, -50, 142, 100, 125);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertEquals(remainingCuboids.size(), 1);
    assertEquals(remainingCuboids.get(0).getxRange(), new Range(10, 11));
    assertEquals(remainingCuboids.get(0).getyRange(), cuboid.getyRange());
    assertEquals(remainingCuboids.get(0).getzRange(), cuboid.getzRange());
    assertTrue(remainingCuboids.get(0).isOn());
  }

  @Test
  public void testSubtract_MiddleSlice1D() {
    // Remove a cuboid at the middle of the xRange
    Cuboid cuboid = new Cuboid(false, 10, 100, -50, 142, 100, 125);
    Cuboid toRemove = new Cuboid(false, 15, 80, -50, 142, 100, 125);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertEquals(remainingCuboids.size(), 2);
    assertEquals(remainingCuboids.get(0).getxRange(), new Range(10, 14));
    assertEquals(remainingCuboids.get(0).getyRange(), cuboid.getyRange());
    assertEquals(remainingCuboids.get(0).getzRange(), cuboid.getzRange());
    assertFalse(remainingCuboids.get(0).isOn());

    assertEquals(remainingCuboids.get(1).getxRange(), new Range(81, 100));
    assertEquals(remainingCuboids.get(1).getyRange(), cuboid.getyRange());
    assertEquals(remainingCuboids.get(1).getzRange(), cuboid.getzRange());
    assertFalse(remainingCuboids.get(1).isOn());
  }

  @Test
  public void testSubtract_MiddleSlice2D() {
    // Remove a cuboid at the middle of xRange and yRange
    Cuboid cuboid = new Cuboid(true, 10, 100, 30, 120, 100, 125);
    Cuboid toRemove = new Cuboid(false, 15, 80, 45, 130, 100, 125);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertEquals(remainingCuboids.size(), 3);
    assertEquals(remainingCuboids.get(0).getxRange(), new Range(10, 14));
    assertEquals(remainingCuboids.get(0).getyRange(), new Range(30, 120));
    assertEquals(remainingCuboids.get(0).getzRange(), cuboid.getzRange());

    assertEquals(remainingCuboids.get(1).getxRange(), new Range(81, 100));
    assertEquals(remainingCuboids.get(1).getyRange(), new Range(30, 120));
    assertEquals(remainingCuboids.get(1).getzRange(), cuboid.getzRange());

    assertEquals(remainingCuboids.get(2).getxRange(), new Range(15, 80));
    assertEquals(remainingCuboids.get(2).getyRange(), new Range(30, 44));
    assertEquals(remainingCuboids.get(2).getzRange(), cuboid.getzRange());
  }

  @Test
  public void testSubtract_EnclosedCuboid() {
    // Remove a cuboid at the middle of this cuboid
    Cuboid cuboid = new Cuboid(true, 10, 100, 30, 120, 100, 125);
    Cuboid toRemove = new Cuboid(false, 15, 80, 45, 110, 110, 120);

    List<Cuboid> remainingCuboids = cuboid.subtract(toRemove);
    assertEquals(remainingCuboids.size(), 6);
  }

}
