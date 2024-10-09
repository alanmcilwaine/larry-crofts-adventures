package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;

public enum Orientation {
  TOPRIGHTFACING(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT),
  BOTTOMLEFTFACING(Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT);
  Direction one;
  Direction faceOne;
  Direction two;
  Direction faceTwo;

  /**
   * Changes the direction of the source's laser
   */
  public void reflectLaser(LaserSource source) {
    if (source.getDirection().equals(faceOne)) {
      source.currentDirection = faceOne;
      source.target = faceOne.act(source.target);
    } else {
      source.currentDirection = faceTwo;
      source.target = faceTwo.act(source.target);
    }
  }

  Orientation(Direction one, Direction faceOne, Direction two, Direction faceTwo) {
    this.one = one;
    this.faceOne = faceOne;
    this.two = two;
    this.faceTwo = faceTwo;
  }

}
