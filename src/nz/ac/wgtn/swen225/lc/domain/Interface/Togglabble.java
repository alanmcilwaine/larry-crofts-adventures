package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Represents an item that can be toggled
 *
 * @author Carla Parinas, 300653631
 */
public interface Togglabble extends Item {

    /**
     * Switches the state of the item
     *
     * @param tile the tile the item is in
     */
    void toggle(Tile<Item> tile);
}
