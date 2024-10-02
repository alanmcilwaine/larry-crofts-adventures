package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.NoItem;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

public class MovableBox implements Actor, Item {
  private Location location;
  private Direction wallFacing = Direction.NONE; // set to none on initialization

  public MovableBox(Location location) { this.location = location; }

  @Override
  public Location getLocation() { return this.location; }

  @Override
  public Direction getActorFacing() { return this.wallFacing; }

  public boolean attemptPush(Direction direction, GameBoard gameBoard) {
    GameBoard.domainLogger.log(Level.INFO, "Player is attempting to push box towards direction: " + direction);
    Location newLoc = direction.act(this.location);
    if(locationIsValid(newLoc, gameBoard)) {
      doMove(direction, gameBoard);
      return true;
    }
    return false;
  }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard) {

    Location newLoc = direction.act(this.location);
    Tile currentTile = findTileInSpecificLocation(gameBoard, location);
    Tile nextTile = findTileInSpecificLocation(gameBoard, newLoc);


    if (nextTile.canStepOn(this)) {
      actOnTile(direction, gameBoard, currentTile, nextTile);

      // free up the current one and occupy the next space.
      currentTile.item = new NoItem();
      nextTile.item = this;
    }
  }

  @Override
  public boolean blockActor(Actor actor) {
    // if it's player, technically doesn't block since we can move it,
    return !(actor instanceof Player);
  }

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {}

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  @Override
  public String toString() { return "MovableBox"; }
}
