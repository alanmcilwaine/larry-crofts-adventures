package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Treasure on tile
 *
 * @author Yee Li
 */
public record Treasure() implements Item {
    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if (actor instanceof Player p) {
            p.addTreasure(this);
            tile.item = new NoItem();
        }
    }

    @Override
    public String toString() {
        return "Treasure";
    }

    @Override
    public Item makeNew() { return new Treasure(); }
}
