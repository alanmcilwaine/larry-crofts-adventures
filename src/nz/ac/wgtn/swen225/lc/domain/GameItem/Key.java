package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;

/**
 * Key on tile
 * @author Yee Li
 */
public record Key(ItemColor itemColor) implements Item {
    /**
     * Key will be pickup by player.
     *
     * @param actor Actor
     */
    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if (actor instanceof Player p) {
            p.addTreasure(this);
            tile.item = new NoItem();
        }
    }

    @Override
    public String toString() { return "Key" + itemColor.toString(); }

    @Override
    public Item makeNew() { return new Key(itemColor); }
}
