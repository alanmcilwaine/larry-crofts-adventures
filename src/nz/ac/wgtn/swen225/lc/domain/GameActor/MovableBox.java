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

  public MovableBox(int x , int y) { this.location = new Location(x, y); }

  @Override
  public Location getLocation() { return this.location; }

  @Override
  public Direction getActorFacing() { return this.wallFacing; }

  @Override
  public void setActorFacing(Direction d) { this.wallFacing = d; }

  @Override
  public boolean attemptMove(Direction direction, GameBoard gameBoard) {

    MovableBox box = gameBoard.getGameState().boxes()
            .stream().filter(b -> b.location.equals(direction.act(location)))
            .findFirst().orElse(null);

    // if there is already a box then don't do anything
    if (box != null) { return false; }
    return Actor.super.attemptMove(direction, gameBoard);
  }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {


    // if there is another box then dont push
    actOnTile(direction, gameBoard, current, next);
  }

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  @Override
  public String toString() { return "MovableBox"; }
}
