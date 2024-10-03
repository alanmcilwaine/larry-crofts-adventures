package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

/**
 * To implement paths for the NPC actors, kinda like state pattern
 */
public interface ActorPath {
  // TODO: Gotta decide if we want more than one paths

  void doMove(Direction d, GameBoard b);
}
