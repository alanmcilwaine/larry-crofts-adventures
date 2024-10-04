package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;

/**
 * An open door on tile
 * @author Yee Li
 */
public record UnLockedDoor(ItemColor itemColor) implements Item {
    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {}

    @Override
    public String toString() { return "UnlockedDoor"; }
}
