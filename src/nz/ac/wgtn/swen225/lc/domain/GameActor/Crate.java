package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;

import java.util.List;

/**
 * A crate can be destroyed by a laser, extends movable box
 *
 * @author Carla Parinas 300653631
 */
public class Crate extends MovableBox {

    public Crate(int x, int y) { super(x, y); }

    /**
     * Removes the current box from the existence. Does this by taking in
     * the boxes in the game board and removing itself from it.
     *
     * @param boxes list of boxes from the gameboard
     */
    public void explode(List<MovableBox> boxes) {
        boxes.remove(this);
    }

    @Override
    public String toString() {
        return "Crate";
    }
}
