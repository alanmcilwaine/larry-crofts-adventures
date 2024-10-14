package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ActorPath;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

/**
 * Represents another Actor in the game who's actions are not influenced by the
 * player pressing keys. Can follow two specific paths.
 *
 * @author Carla Parinas 300653631
 */
public abstract class Robot implements Actor {
  private Location location;
  private ActorPath actorPath = ActorPath.LEFTRIGHT;

  private Direction robotFacing = actorPath.getDir1();

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public Direction getActorFacing() { return robotFacing; }
  public ActorPath getActorPath() { return actorPath; }

  @Override
  public void setActorFacing(Direction dir) { this.robotFacing = dir; }

  public void setActorPath(ActorPath actorPath) { this.actorPath = actorPath; }

  public void update(GameBoard gameBoard) {
    if (!attemptMove(this.robotFacing, gameBoard)) {
      actorPath.switchDirection(this);
    }
  }

  @Override
  public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {
    actorPath.doMove(this, gameBoard, current, next);
  }

  @Override
  public void updateActorLocation(Location location) {
    this.location = new Location(location.x(), location.y());
  }

  @Override
  public String toString() {
    return "Robot";
  }

  Robot(int x, int y) {
    this.location = new Location(x, y);
  }
}