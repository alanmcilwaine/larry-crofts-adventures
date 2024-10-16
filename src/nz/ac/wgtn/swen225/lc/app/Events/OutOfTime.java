package nz.ac.wgtn.swen225.lc.app.Events;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.GameTimer;

/**
 * OutOfTime --- When the player has run out of time this event is called.
 * This event will reset the game to the current level after 2.5 seconds.
 *
 * @author Alan McIlwaine 300653905
 */
public class OutOfTime implements GameEvent{
    @Override
    public void check(App app) {
        if (GameTimer.stageCountdown > 0) {
            return;
        }
        app.timer().runEvent(() -> app.gameLoader().loadLevel(app.domain().getGameState().level()), 2500);
    }
}
