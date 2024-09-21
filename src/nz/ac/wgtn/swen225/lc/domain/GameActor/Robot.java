package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

public abstract class Robot implements Actor {
  private Location location;
  private Direction robotFacing = Direction.values()[(int) (Math.random() * 5)];
  private int switchDirCount;

  @Override
  public Location getLocation() { return location; }
  @Override
  public Direction getActorFacing() { return robotFacing; }

  public void setRobotFacing(Direction dir) { this.robotFacing = dir; }

  public void update(GameBoard gameBoard) {
    doMove(this.robotFacing, gameBoard);
  }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard) {
    // TODO check if we can overlap robots
    Location newLoc = direction.act(this.location); // location to move to

    GameBoard.domainLogger.log(Level.INFO, "Robot is facing: " + robotFacing);

    Tile tile = gameBoard.getBoard().get(newLoc.x()).get(newLoc.y()); // tile on this location
    Tile prevTile = gameBoard.getBoard().get(this.location.x()).get(this.location.y());

    if (tile.canStepOn(this) && switchDirCount <= 20) {
      updateActorLocation(newLoc);


      tile.onEntry(this);
      prevTile.onExit(this);
      switchDirCount++; // don't know how often should switch direction
    } else {
        // if the tile to move onto is blocked then re-roll robot's direction
        this.robotFacing = Direction.values()[(int) (Math.random() * 4)] ;
        switchDirCount = 0;
    }

  }

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  Robot(int x, int y) { this.location = new Location(x, y); }
}
