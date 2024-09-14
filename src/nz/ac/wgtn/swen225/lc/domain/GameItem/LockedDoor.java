package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;

public record LockedDoor(ItemColor itemColor) implements Item {
    @Override
    public boolean blockActor(Actor actor) {
        // TODO
        // should check if actor has matched color key.
        return Item.super.blockActor(actor);
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        tile.item = new UnLockedDoor(this.itemColor());
    }
}
