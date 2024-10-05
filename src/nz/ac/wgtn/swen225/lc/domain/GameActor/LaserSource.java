package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class LaserSource implements Actor {
  Direction direction;

  public LaserSource(Direction direction) { this.direction = direction; }

  @Override
  public String toString() { return "LaserSource"; }

  @Override
  public Location getLocation() {
    return null;
  }

  @Override
  public Direction getActorFacing() {
    return null;
  }

  @Override
  public void setActorFacing(Direction d) {

  }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {

  }

  @Override
  public void updateActorLocation(Location location) {

  }
}
