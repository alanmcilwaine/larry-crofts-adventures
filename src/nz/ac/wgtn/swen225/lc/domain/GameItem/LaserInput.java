package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Toggles all the nearby togglable items much like a button,
 * toggles when a laser comes into contact with the item rather than a press.
 *
 * @author Carla Parinas 300653631
 */
public class LaserInput extends Button {

    @Override
    public boolean blockActor(Actor actor) {
        return true;
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        throw new IllegalStateException("Can't step on: " + tile.getItemOnTile());
    }

    @Override
    public String toString() {
        return "LaserInput";
    }

    @Override
    public Item makeNew() {
        return new LaserInput();
    }
}
