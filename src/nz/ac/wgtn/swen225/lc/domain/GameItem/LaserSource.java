package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.List;

public class LaserSource implements Item {

  private Location location; // needs to set the laser somewhere
  private Direction direction; // has a direction but it should not move as well as blocks all actors
  private boolean laserToggle = false; // auto set to false
  private Laser childLaser;

  public LaserSource(GameBoard gameBoard, Direction direction, boolean toggle, int x,
                     int y) {
    this.direction = direction;
    this.location = new Location(x, y);
    this.laserToggle = toggle;
    Location target = this.direction.act(location);
    this.childLaser = new Laser(gameBoard, direction, target);
    passLaser(gameBoard.getBoard(), target);
  }

  private void passLaser(List<List<Tile<Item>>> board, Location next) {
    if (!laserToggle) { return; }
    board.get(next.x()).get(next.y()).item = childLaser;
  }

  public void setLaserToggle() {
    laserToggle = !laserToggle;
  }

  @Override
  public boolean blockActor(Actor actor) { return true; }

  @Override
  public String toString() { return "LaserSource"; }

}
