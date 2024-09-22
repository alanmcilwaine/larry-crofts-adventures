package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;

import java.util.Optional;

public record LockedDoor(ItemColor itemColor) implements Item {
    @Override
    public boolean blockActor(Actor actor) {
        return !(actor instanceof Player p) || getKeyForDoor(p).isEmpty();
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        tile.item = new UnLockedDoor(this.itemColor());
        if (actor instanceof Player p) {
            p.removeTreasure(getKeyForDoor(p).orElse(new Key(ItemColor.InvalidColor)));
        }
    }

    /**
     * Get player's key for this door if any.
     *
     * @param p Player
     * @return an optional of item.
     */
    private Optional<Item> getKeyForDoor(Player p) {
        return p.getTreasure()
                .stream()
                .filter(e -> e instanceof Key k && k.itemColor().equals(this.itemColor())).findFirst();
    }

    @Override
    public String toString() { return "LockedDoor" + itemColor.toString(); }
}
