package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

/**
 * A wall with holes that a laser can pass through
 *
 * @author Carla Parinas 300653631
 */
public class Tube implements Item {

    @Override
    public boolean blockActor(Actor actor) {
        return true;
    }

    @Override
    public String toString() {
        return "Tube";
    }

    @Override
    public Item makeNew() {
        return new Tube();
    }
}
