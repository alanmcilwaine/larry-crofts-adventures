package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.NoItem;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

public class MovableBox implements Actor {
  private Location location;
  private Direction wallFacing = Direction.NONE; // set to none on initialization

  public MovableBox(Location location) { this.location = location; }

  @Override
  public Location getLocation() { return this.location; }

  @Override
  public Direction getActorFacing() { return this.wallFacing; }

  @Override
  public void setActorFacing(Direction d) { this.wallFacing = d; }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {
    // if can't be stepped on then leave function and not do anything
    if (!next.canStepOn(this)) { return; }

    actOnTile(direction, gameBoard, current, next);

    // free up the current one and occupy the next space.
//    current.item = new NoItem();
//    next.item = this;
  }

//  @Override
//  public boolean blockActor(Actor actor) {
//    // if it's player, technically doesn't block since we can move it,
//    return !(actor instanceof Player);
//  }
//
//  @Override
//  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {}

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  @Override
  public String toString() { return "MovableBox"; }
}
