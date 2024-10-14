package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Exit on tile
 *
 * @author Yee Li
 */
public record Exit() implements Item {

    @Override
    public boolean blockActor(Actor actor) {
        return !(actor instanceof Player); // other entities cannot enter tile
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if (actor instanceof Player p) {
            p.setNextLevel(true);
        }
    }

    @Override
    public String toString() {
        return "Exit";
    }

    @Override
    public Item makeNew() {
        return new Exit();
    }
}
