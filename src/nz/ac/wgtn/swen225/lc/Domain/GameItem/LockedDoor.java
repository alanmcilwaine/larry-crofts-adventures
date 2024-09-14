package nz.ac.wgtn.swen225.lc.Domain.GameItem;

import nz.ac.wgtn.swen225.lc.Domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Tile;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.ItemColor;

public record LockedDoor(ItemColor itemColor) implements Item {
    @Override
    public boolean isBlock(Actor actor) {
        // TODO
        // should check if actor has matched color key.
        return Item.super.isBlock(actor);
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        tile.item = new UnLockedDoor(this.itemColor());
    }
}
