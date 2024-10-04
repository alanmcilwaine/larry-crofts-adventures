package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameState;

/**
 * Class needs to observe game state shall implement this interface
 * @author Yee Li
 */
public interface GameStateObserver {
    void update(GameState gameState);
}
