package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public record Exit() implements Item {
    @Override
    public boolean blockActor(Actor actor) {
        if(actor instanceof Player p) {
            return p.getTreasure().stream().filter(e->e instanceof Treasure).toList().size() < GameBoard.totalTreasure;
        }
        return false;
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        // should finish the level by app?
    }
}
