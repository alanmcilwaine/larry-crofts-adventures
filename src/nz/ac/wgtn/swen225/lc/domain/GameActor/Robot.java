package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public abstract class Robot implements Actor {
  private Location location;
  private Direction robotFacing;

  @Override
  public Location getLocation() { return location; }

  @Override
  public Direction getActorFacing() { return robotFacing; }

  @Override
  public void prepareMove(Direction direction, GameBoard gameBoard) {
    this.robotFacing = direction;

    Location newLoc = direction.act(this.location); // location to move to
    Tile tile = gameBoard.getBoard().get(newLoc.x()).get(newLoc.y()); // tile on this location

    if (tile.canStepOn(this)) {
      doMove(newLoc);
      tile.onEntry(this);
      tile.onExit(this);
    }
  }

  @Override
  public void doMove(Location location) {
    this.location = new Location(location.x(), location.y());
  }
}
