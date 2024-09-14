package nz.ac.wgtn.swen225.lc.Domain.GameItem;

import nz.ac.wgtn.swen225.lc.Domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Tile;

/**
 * A free tile.
 */
public record NoItem() implements Item {

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {}
}
