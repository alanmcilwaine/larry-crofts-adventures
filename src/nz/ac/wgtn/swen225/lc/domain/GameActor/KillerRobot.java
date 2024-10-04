package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class KillerRobot extends Robot {
    public KillerRobot(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "KillerRobot";
    }
}