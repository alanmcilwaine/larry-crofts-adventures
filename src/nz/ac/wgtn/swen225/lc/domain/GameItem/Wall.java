package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

/**
 * Wall on tile
 * @author Yee Li
 */
public record Wall() implements Item {
    @Override
    public boolean blockActor(Actor actor) {
        return true;
    }

    @Override
    public String toString() { return "Wall"; }

}
