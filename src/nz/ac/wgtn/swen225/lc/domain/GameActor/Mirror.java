package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Orientation;

public class Mirror extends MovableBox {
  Orientation orientation;

  public Mirror(Orientation orientation, int x, int y) {
    super(x, y);
    this.orientation = orientation;
  }

  public Orientation getOrientation() { return orientation; }

  public void reflectLaser(LaserSource source) {
    orientation.reflectLaser(source);
  }

  @Override
  public String toString() { return "Mirror"; }

}
