package nz.ac.wgtn.swen225.lc.Domain;

import nz.ac.wgtn.swen225.lc.Domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.Location;

public class Tile<T extends Item> {

    public Item item;
    public final Location location;

    public Tile(T item, Location location) {
        this.item = item;
        this.location = location;
    }

    public boolean canStepOn(Actor actor) {
        return item.isBlock(actor);
    }

    public void onEntry(Actor actor) {
        item.onTouch(actor, this);
    }

    public void onExit(Actor actor) {
        //probably nothing for most tiles.
    }
}
