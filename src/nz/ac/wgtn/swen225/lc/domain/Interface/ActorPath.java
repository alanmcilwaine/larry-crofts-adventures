package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

import java.util.logging.Level;

/**
 * To implement paths for the NPC actors, kinda like state pattern
 */
public enum ActorPath {
  // TODO: Gotta decide if we want more than one paths
  UPDOWN(Direction.UP, Direction.DOWN),
  LEFTRIGHT(Direction.LEFT, Direction.RIGHT);

  int DELAY = 20;
  int stepCount;
  Direction dir1;
  Direction dir2;
  Direction d;

  ActorPath(Direction dir1, Direction dir2 ) {
    this.dir1 = dir1;
    this.dir2 = dir2;
    d = dir1;
  }

  public void doMove(Actor a, GameBoard b, Tile<Item> current, Tile<Item> next) {
    a.setActorFacing(d);
    GameBoard.domainLogger.log(Level.INFO, "Robot is facing: " + a.getActorFacing());
    if (!next.canStepOn(a) && stepCount > DELAY) {
      d = d.equals(dir1) ? dir2 : dir1;
      return;
    }

    if (stepCount > DELAY) {
      a.actOnTile(this.d, b, current, next);
      stepCount = 0; // reset
    }
    stepCount++; // delay

    GameBoard.domainLogger.log(Level.INFO, "Robot is now at:" + a.getLocation());
  }
}
