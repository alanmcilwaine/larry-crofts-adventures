package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

public class MovableBox implements Actor {
  private Location location;
  private Direction wallFacing = Direction.NONE; // set to none on initialization


  @Override
  public Location getLocation() { return this.location; }

  @Override
  public Direction getActorFacing() { return this.wallFacing; }




  @Override
  public void doMove(Direction direction, GameBoard gameBoard) {
    GameBoard.domainLogger.log(Level.INFO, "Player is attempting to push box towards direction: " + wallFacing);

    Location newLoc = direction.act(this.location);
    Tile currentTile = findTileInSpecificLocation(gameBoard, location);
    Tile nextTile = findTileInSpecificLocation(gameBoard, newLoc);


  }

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  @Override
  public String toString() { return "MovableBox"; }
}
