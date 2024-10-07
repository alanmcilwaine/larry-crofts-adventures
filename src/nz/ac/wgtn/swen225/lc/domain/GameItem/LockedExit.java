package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.DomainLogger;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

import java.util.Collection;
import java.util.logging.Level;

/**
 * Locked Exit on tile
 * @author Yee Li
 */
public record LockedExit() implements Item, GameStateObserver {
    @Override
    public boolean blockActor(Actor actor) {
        return true;
    }

    /**
     * LockedExit item will observe game state and receive
     * an update and change accordingly.
     *
     * @param gameState game state.
     */
    @Override
    public void update(GameState gameState) {
        long playerTreasure = gameState.player().getTreasure()
                .stream().filter(e -> e instanceof Treasure).count();

        gameState.board().stream()
                .flatMap(Collection::stream)
                .filter(x -> x.item instanceof LockedExit)
                .findFirst()
                .ifPresentOrElse(tile -> {
                    if (playerTreasure >= gameState.totalTreasure()) {
                        tile.item = new Exit();
                    }
                }, () -> {
                    DomainLogger.LOGGER.getLogger().log(Level.INFO, "Exit is already unlocked.");
                    GameBoard.unsubscribeGameState(this);
                });
    }

    @Override
    public String toString() { return "LockedExit"; }

  @Override
  public Item makeNew() { return new LockedExit(); }
}
