package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.DomainLogger;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.logging.Level;

public record LockedExit() implements Item, GameStateObserver {
    @Override
    public boolean blockActor(Actor actor) {
        return true;
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    }

    /**
     * LockedExit item will observe game state and receive
     * an update and change accordingly.
     * @param gameState game state.
     */
    @Override
    public void update(GameState gameState) {
        var playerTreasure = gameState.player().getTreasure()
                .stream().filter(e -> e instanceof Treasure).toList().size();
        var self = this;
        try {
            Tile<Item> tile = gameState.board().stream()
                    .flatMap(Collection::stream)
                    .filter(x->x.item instanceof LockedExit)
                    .toList()
                    .getFirst();
            if (playerTreasure == GameBoard.totalTreasure) {
                tile.item = new Exit();
            }
        } catch (NoSuchElementException e) {
            DomainLogger.LOGGER.getLogger().log(Level.INFO, "Exit is already unlocked.");
            GameBoard.unsubscribeGameState(self);
        }
    }
}
