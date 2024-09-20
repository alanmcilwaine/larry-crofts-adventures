package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public record Info(String info) implements Item {

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if(actor instanceof Player p) {
            p.setShowPlayerInfo(true);
        }
    }

    @Override
    public <T extends Item> void onExit(Actor actor, Tile<T> tile) {
        if(actor instanceof Player p) {
            p.setShowPlayerInfo(false);
        }
    }
}
