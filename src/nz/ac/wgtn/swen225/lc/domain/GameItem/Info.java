package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

/**
 * Info on tile
 *
 * @author Yee Li
 */
public record Info(String info) implements Item {

    @Override
    public boolean blockActor(Actor actor) { return !(actor instanceof Player); }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if (actor instanceof Player p) {
            p.setShowPlayerInfo(true);
        }
    }

    @Override
    public <T extends Item> void onExit(Actor actor, Tile<T> tile) {
        if (actor instanceof Player p) {
            p.setShowPlayerInfo(false);
        }
    }

    @Override
    public String toString() {
        return "Info";
    }
}
