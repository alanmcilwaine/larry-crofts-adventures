package nz.ac.wgtn.swen225.lc.domain.Utilities;

/**
 * Represents a specific point/location on the board.
 * Can do specific mathematical operations with other Points.
 * @param x horizontal position
 * @param y vertical position
 */
public record Location(int x, int y) {

  /**
   * Gets a new point that is the sum of both values.
   * @param p other point to add
   * @return returns an updated point with the sum
   */
  public Location add(Location p) { return new Location(x + p.x(), y + y()); }

  /**
   * returns a new Point with the product of its values and the provided number to times by
   * @param d number to times it by
   * @return returns an updated point with the product
   */
  public Location multiply(int d) { return new Location(x * d, y * d); }
}
