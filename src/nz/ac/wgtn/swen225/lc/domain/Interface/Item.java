package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Item on top of tile.
 *
 * @author Yee Li, Maria Louisa Carla Parinas
 */
public interface Item {

    /**
     * Checks if the item will block the player from moving
     *
     * @param actor actor that will move
     * @return true if blocks but false if not
     */
    default boolean blockActor(Actor actor) {
        return false;
    }

    /**
     * Occurs when an Actor interacts with the item on the tile
     *
     * @param actor Actor that touches item
     * @param tile  Tile to update
     * @param <T>   extends Item
     */
    default <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        throw new IllegalStateException("Can't step on: " + tile.getItemOnTile());
    }


    /**
     * Occurs when an actor leaves the tile with the item.
     *
     * @param actor Actor that interacts with item
     * @param tile  Tile to update
     * @param <T>   extends Item
     */
    default <T extends Item> void onExit(Actor actor, Tile<T> tile) {
    }
}
