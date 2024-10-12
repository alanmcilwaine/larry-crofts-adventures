package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Orientation {
  ONE(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT) {

    @Override
    public void setReflectionMap() {
      reflectionMap = Map.of(Direction.DOWN, Direction.LEFT,
                             Direction.RIGHT, Direction.UP,
                             Direction.UP, Direction.LEFT,
                             Direction.LEFT, Direction.DOWN );
    }
  },
  TWO(Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT);
  Direction one;
  Direction faceOne;
  Direction two;
  Direction faceTwo;
  Map<Direction, Direction> reflectionMap = new HashMap<>();
  String lastUsedDir;

  /**
   * Changes the direction of the source's laser
   */
  public void reflectLaser(LaserSource source) {
    lastUsedDir = source.getDirection().toString();
    if (lastUsedDir.equals(faceOne)) {
      lastUsedDir += faceTwo.toString();
      source.getLasers().put(source.target, lastUsedDir);
      source.currentDirection = faceTwo;
      source.target = faceTwo.act(source.target);
    } else {
      lastUsedDir += faceOne.toString();
      source.getLasers().put(source.target, lastUsedDir);
      source.currentDirection = faceOne;
      source.target = faceOne.act(source.target);
    }
    source.orientation = lastUsedDir;
    System.out.println("break");
  }

  Orientation(Direction one, Direction faceOne, Direction two, Direction faceTwo) {
    this.one = one;
    this.faceOne = faceOne;
    this.two = two;
    this.faceTwo = faceTwo;
    setReflectionMap();
  }

  public void setReflectionMap() {}
  public Map<Direction, Direction> getReflectionMap() {
    return Collections.unmodifiableMap(reflectionMap);
  }

  @Override
  public String toString() {
    return lastUsedDir;
  }
}
