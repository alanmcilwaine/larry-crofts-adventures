package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

import java.util.logging.Level;

/**
 * To implement paths for the NPC actors, kinda like state pattern
 */
public enum ActorPath {
  // TODO: might just turn this into a class but not sure yet
  UPDOWN(Direction.UP, Direction.DOWN),
  LEFTRIGHT(Direction.LEFT, Direction.RIGHT);

  int DELAY = 5;
  int stepCount;
  Direction dir1;
  Direction dir2;
  Direction d;

  ActorPath(Direction dir1, Direction dir2) {
    this.dir1 = dir1;
    this.dir2 = dir2;
    d = dir1;
  }

  public Direction getDir1() { return dir1; }

  public Direction getDir2() { return dir2; }

  public void switchDirection(Actor a) {
    d = d.equals(dir1) ? dir2 : dir1;
    a.setActorFacing(d);
  }

  // For testing lol
  public void resetStepCount() { stepCount = 0; }

  public void doMove(Actor a, GameBoard b, Tile<Item> current, Tile<Item> next) {
    GameBoard.domainLogger.log(Level.INFO, "Robot is facing: " + a.getActorFacing());

    MovableBox box = b.getGameState().boxes()
            .stream().filter(bx-> bx.getLocation().equals(next.location))
            .findFirst().orElse(null);

    if (stepCount > DELAY) {
      if (next.canStepOn(a) && a.locationIsValid(next.location, b)) {
        a.actOnTile(this.d, b, current, next);
      } else {
        switchDirection(a);
      }
      stepCount = 0; // reset
    }

    stepCount++; // delay

    GameBoard.domainLogger.log(Level.INFO, "Robot is now at:" + a.getLocation());
  }
}
