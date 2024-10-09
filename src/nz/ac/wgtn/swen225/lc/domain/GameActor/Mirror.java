package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Laser;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Orientation;

import java.util.Map;

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

//  public String getLaserReflect() {
//    orientation.reflectLaser();
//  }

  @Override
  public String toString() { return "Mirror"; }

}
