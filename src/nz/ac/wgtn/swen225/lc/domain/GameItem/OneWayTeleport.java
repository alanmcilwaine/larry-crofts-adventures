package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.DomainLogger;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.TeleportItem;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.logging.Level;

/**
 * Teleport player to a valid destination.
 *
 * @param destination, the location player will be teleported to.
 * @author Yee Li
 */
public record OneWayTeleport(Location destination) implements TeleportItem {
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
    public String toString() {
        return "OneWayTeleport";
    }
}
