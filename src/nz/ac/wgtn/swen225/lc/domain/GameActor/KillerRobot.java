package nz.ac.wgtn.swen225.lc.domain.GameActor;

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
}