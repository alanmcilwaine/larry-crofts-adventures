package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Orientation;

/**
 * Represents a mirror that can reflect lasers. Can take two orientations.
 * Functions like a movable box as the player can move it.
 *
 * @author Carla Parinas 300653631
 */
public class Mirror extends MovableBox {
  private final Orientation orientation;

  public Mirror(Orientation orientation, int x, int y) {
    super(x, y);
    this.orientation = orientation;
  }

  /**
   * Returns the orientation of the mirror
   * @return Orientation object
   */
  public Orientation getOrientation() { return orientation; }

  /**
   * Reflects the oncoming laser
   * @param source Laser Source object
   */
  public void reflectLaser(LaserSource source) {
    orientation.reflectLaser(source);
  }

  @Override
  public String toString() {
    return orientation.equals(Orientation.ONE) ? "MirrorReversed" : "Mirror";
  }

}
