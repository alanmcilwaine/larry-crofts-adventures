package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Laser;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Orientation;

public class Mirror implements Item {
  Orientation orientation;

  public Mirror(Orientation orientation, int x, int y) {
    //super(x, y);
    this.orientation = orientation;
  }

  public Mirror() {
  }

//  public Orientation getOrientation() { return orientation; }
//
//  public Location reflectLaser(Laser laser) {
//    Direction dir = laser.getDirection();
//    return orientation.reflectLaser(dir, ) ;
//  }
//
//  @Override
//  public String toString() { return "Mirror"; }
}
