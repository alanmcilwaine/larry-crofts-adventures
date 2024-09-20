package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

/**
 * A game character that moves around.
 */
public interface Actor {

    Location getLocation();

    Direction getActorFacing();

    void doMove(Direction direction, GameBoard gameBoard);

    void updateActorLocation(Location location);
}
