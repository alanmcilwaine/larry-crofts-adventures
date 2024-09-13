package nz.ac.wgtn.swen225.lc.domain;

/**
 * Indicates and represents which direction an entity might be facing.
 * Options are NONE, UP, DOWN, LEFT, RIGHT.
 */
public enum Direction {
  NONE(0, 0),
  UP(0, 1),
  DOWN(0, -1),
  LEFT(-1, 0),
  RIGHT(1, 0)
  ;

  public Point point; // point representation of the direction

  /**
   * Update the location of the entity according to direction with the given speed.
   * @param speed speed at which the entity should move
   * @return returns the new updated point
   */
  public Point act(double speed) { return point.multiply(speed);}

  /**
   * Constructor for direction
   * @param x horizontal position
   * @param y vertical position
   */
  Direction(double x, double y) { point = new Point(x ,y); }
}
