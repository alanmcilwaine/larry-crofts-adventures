package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;

public enum Orientation {
  TOPRIGHTFACING(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT),
  BOTTOMLEFTFACING(Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT);
  Direction one;
  Direction faceOne;
  Direction two;
  Direction faceTwo;
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
  }

  @Override
  public String toString() {
    return lastUsedDir;
  }
}
