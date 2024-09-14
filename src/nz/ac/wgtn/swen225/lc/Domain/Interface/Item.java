package nz.ac.wgtn.swen225.lc.Domain.Interface;

import nz.ac.wgtn.swen225.lc.Domain.Tile;

/**
 * Item on top of tile.
 */
public interface Item {
    default boolean isBlock(Actor actor){return false;}

    <T extends Item> void onTouch(Actor actor, Tile<T> tile);
}
