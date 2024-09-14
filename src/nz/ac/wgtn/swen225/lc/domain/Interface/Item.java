package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Item on top of tile.
 */
public interface Item {
    default boolean blockActor(Actor actor) {
        return false;
    }

    default <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    }

    default <T extends Item> void onExit(Actor actor, Tile<T> tile) {
    }
}
