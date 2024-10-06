package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class LaserSource implements Item {

  private Location location; // needs to set the laser somewhere
  private Direction direction; // has a direction but it should not move as well as blocks all actors
  private boolean laserToggle = false; // auto set to false

  LaserSource(Direction direction, int x, int y) {
    this.direction = direction;
    this.location = new Location(x, y);
  }

  private void passLaser() {
    //Tile<Item> next = this.direction.act(location);
  }

  public Direction getDirection() { return direction; }

  @Override
  public String toString() { return "LaserSource"; }

}
