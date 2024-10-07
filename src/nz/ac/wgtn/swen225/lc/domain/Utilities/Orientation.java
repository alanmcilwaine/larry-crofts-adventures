package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Laser;

public enum Orientation {
  TOPRIGHTFACING(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT),
  BOTTOMLEFTFACING(Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT);
  Direction one;
  Direction faceOne;
  Direction two;
  Direction faceTwo;

  /**
   * Returns a location that the reflected laser would go to
   * @param direction Direction of the laser
   * @param location Location of the current laser
   * @return new reflected location
   */
  public Location reflectLaser(Direction direction, Location location) {
    return direction.equals(one) ? faceOne.act(location) : faceTwo.act((location));
  }

  Orientation(Direction one, Direction faceOne, Direction two, Direction faceTwo) {
    this.one = one;
    this.faceOne = faceOne;
    this.two = two;
    this.faceTwo = faceTwo;
  }
}
