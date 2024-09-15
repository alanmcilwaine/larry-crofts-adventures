package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class Tile<T extends Item> {

    public Item item;
    public final Location location;

    public Tile(T item, Location location) {
        this.item = item;
        this.location = location;
    }

    public Tile(Location location) {
        this.location = location;
    }

    public boolean canStepOn(Actor actor) {
        return !item.blockActor(actor);

    public void onEntry(Actor actor) {
        item.onTouch(actor, this);
    }

    public void onExit(Actor actor) {
        //probably nothing for most tiles.
    }
}
