package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

public abstract class Robot implements Actor {
  private Location location;
  private Direction robotFacing = Direction.values()[(int) (Math.random() * 5)];
  private int switchDirCount;
  private int moveCount;

  // GETTERS

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
    // TODO observe robot movement if it is going too fast and also polish movement a bit more.
    Location newLoc = direction.act(this.location); // location to move to

    GameBoard.domainLogger.log(Level.INFO, "Robot is facing: " + robotFacing);

    Tile<Item> currentTile = findTileInSpecificLocation(gameBoard, location); // current tile
    Tile<Item> nextTile = findTileInSpecificLocation(gameBoard, newLoc); // tile to move to


    if (locationIsValid(newLoc, gameBoard)) {
      if (nextTile.canStepOn(this) && switchDirCount <= 20 && moveCount >= 20) {
        currentTile.onExit(this);
        nextTile.onEntry(this);
        updateActorLocation(newLoc);

        // robot should move only once and then wait for counts
        moveCount = 0;
        GameBoard.domainLogger.log(Level.INFO, "Robot is now at:" + location);
      } else {

        GameBoard.domainLogger.log(Level.INFO, "Robot did not move still at: " + location);
        this.robotFacing = Direction.values()[(int) (Math.random() * 5)];
        switchDirCount = 0;
      }

      switchDirCount++; // don't know how often should switch direction
    } else {
      GameBoard.domainLogger.log(Level.INFO, "Robot tried to move to invalid, still at: " + location);
    }

    moveCount++;

  }

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  @Override
  public String toString() { return "Robot"; }

  Robot(int x, int y) { this.location = new Location(x, y); }
}
