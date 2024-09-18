package nz.ac.wgtn.swen225.lc.domain;


import nz.ac.wgtn.swen225.lc.domain.GameItem.Exit;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedExit;

import nz.ac.wgtn.swen225.lc.domain.GameActor.KillerRobot;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Util;

public class Tile<T extends Item> implements GameStateObserver {

    public Item item;
    public final Location location;

    public Tile(T item, Location location) {
        this.item = item;
        this.location = location;
    }

    public boolean canStepOn(Actor actor) {
        Util.checkNull(actor, "Actor");
        return !item.blockActor(actor);
    }
    
    public void onEntry(Actor actor) {
        Util.checkNull(actor, "Actor");
        if (!canStepOn(actor)) {
            throw new IllegalArgumentException("Can't move into tile.");
        }

        if(actor instanceof Player) { item.onTouch(actor, this); }
    }

    public void onExit(Actor actor) {
        Util.checkNull(actor, "Actor");
        item.onExit(actor, this);
    }

    /**
     * Get item name on tile.
     *
     * @return run time Item class name.
     */
    public String getItemOnTile() {
        String attribute = "";
        try {
            attribute += item.getClass().getMethod("itemColor").invoke(item);
        } catch (Exception ignored) {
        }
        return item.getClass().getSimpleName() + attribute;
    }

    @Override
    public void update(int treasureNumber) {
        if (treasureNumber == GameBoard.totalTreasure && item instanceof LockedExit) {
            item = new Exit();
        }
    }
}
