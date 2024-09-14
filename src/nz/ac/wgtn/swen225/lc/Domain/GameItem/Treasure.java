package nz.ac.wgtn.swen225.lc.Domain.GameItem;

import nz.ac.wgtn.swen225.lc.Domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Tile;

public record Treasure() implements Item {
    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if(actor instanceof Player p) {
            p.addTreasure(this);
            tile.item = new NoItem();
        }
    }
}
