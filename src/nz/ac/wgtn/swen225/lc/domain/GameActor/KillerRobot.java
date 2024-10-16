package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;

/**
 * An NPC robot that can kill the player
 *
 * @author Carla Parinas 300653631
 */
public class KillerRobot extends Robot {
    public KillerRobot(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "KillerRobot";
    }

    @Override
    public Actor makeNew() {
        return new KillerRobot(getLocation().x(), getLocation().y());
    }
}