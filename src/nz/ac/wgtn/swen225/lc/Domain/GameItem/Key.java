package nz.ac.wgtn.swen225.lc.Domain.GameItem;

import nz.ac.wgtn.swen225.lc.Domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Tile;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.KeyColor;

public record Key(KeyColor keyColor) implements Item {
    /**
     * Key will be pickup by player.
     * @param actor Actor
     */
    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if(actor instanceof Player p){
            p.addTreasure(this);
            tile.item = new NoItem();
        }
    }
}
