package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.DomainLogger;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.TeleportItem;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Animation;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

/**
 * Teleport player to a valid destination.
 *
 * @author Yee Li
 */
public class OneWayTeleport implements TeleportItem {

    final Location destination;
    final Animation anim = new Animation("portal", 6, 3);

    /**
     *
     * @param destination, the location player will be teleported to.
     **/
    public OneWayTeleport(Location destination) {
        this.destination = destination;
    }

    public Location destination() {
        return destination;
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        if (actor instanceof Player p) {
            var destTile = p.findTileInSpecificLocation(p.getGameBoard(), destination);
            checkLoopTeleport(tile, destTile);
            if (destTile.canStepOn(actor)) {
                tile.onExit(actor);
                p.updateActorLocation(destination);
                destTile.onEntry(actor);
            } else {
                p.updateActorLocation(tile.location);
                DomainLogger.LOGGER.getLogger().log(Level.INFO, "Can't not teleport to: " + destination);
            }
        }
    }

    private <T extends Item> void checkLoopTeleport(Tile<T> tile, Tile<Item> destTile) {
        if (destTile.item instanceof TeleportItem t && t.destination().equals(tile.location)) {
            DomainLogger.LOGGER.getLogger().log(Level.WARNING, "Teleport destination creates loop.");
            throw new IllegalArgumentException("Infinite teleport.");
        }
    }

    @Override
    public void tick() {
        anim.tick();
    }

    @Override
    public String toString() { return anim.toString(); }

    @Override
    public Item makeNew() { return new OneWayTeleport(destination); }
}
