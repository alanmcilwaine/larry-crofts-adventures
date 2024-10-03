package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

public class LaserSource implements Item {
  Direction direction;

  public LaserSource(Direction direction) { this.direction = direction; }

  @Override
  public String toString() { return "LaserSource"; }
}
