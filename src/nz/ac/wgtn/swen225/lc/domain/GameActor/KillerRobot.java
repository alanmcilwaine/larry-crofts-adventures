package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class KillerRobot extends Robot{
    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Direction getActorFacing() {
        return null;
    }

    @Override
    public void prepareMove(Direction direction, GameBoard gameBoard) {

    }

    @Override
    public void doMove(Location location) {

    }

    public KillerRobot(Location location) {
    }
}
