package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Laser;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class Mirror implements Item {

  String angle;

//  public Mirror() {
//    super(1, 1);
//  }

  public String getAngle() { return angle; }

//  public Location reflectLaser(Laser laser) {
//    Direction dir = laser.getDirection();
//  }

  @Override
  public String toString() { return "Mirror"; }
}
