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

  @Override
  public void setActorFacing(Direction dir) { this.robotFacing = dir; }

  public void update(GameBoard gameBoard) {
    attemptMove(this.robotFacing, gameBoard);
  }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {
    // TODO make it have a deterministic pattern
    GameBoard.domainLogger.log(Level.INFO, "Robot is facing: " + robotFacing);

    if (!next.canStepOn(this) && moveCount < 20) { return; }
    actOnTile(direction, gameBoard, current, next);

    // robot should move only once and then wait for counts
    moveCount = 0;
    GameBoard.domainLogger.log(Level.INFO, "Robot is now at:" + location);

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
