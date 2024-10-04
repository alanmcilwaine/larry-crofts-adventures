package nz.ac.wgtn.swen225.lc.domain;


import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Exit;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedExit;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Util;

public class Tile<T extends Item> {

    public Item item;
    public final Location location;

    public Tile(T item, Location location) {
        this.item = item;
        this.location = location;
    }

    /**
     * Checks if Tile can be stepped on
     * @param actor Actor to check
     * @return true if can be stepped, false if not
     */
    public boolean canStepOn(Actor actor) {
        Util.checkNull(actor, "Actor");
        return !item.blockActor(actor);
    }

    /**
     * Occurs when an Actor has stepped into this Tile
     * @param actor Actor interacting with Tile
     */
    public void onEntry(Actor actor) {
        Util.checkNull(actor, "Actor");
        if (!canStepOn(actor)) {
            throw new IllegalArgumentException("Can't move into tile.");
        }
        //TODO seems not reasonable
        if (actor instanceof Player) {
            item.onTouch(actor, this);
        }
    }

    /**
     * Occurs when an Actor has exited this Tile
     * @param actor
     */
    public void onExit(Actor actor) {
        Util.checkNull(actor, "Actor");
        item.onExit(actor, this);
    }

    /**
     * Get item name on tile.
     *
     * @return run time Item class name plus color.
     */
    public String getItemOnTile() { return item.toString(); }
}
