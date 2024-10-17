package nz.ac.wgtn.swen225.lc.app.Events;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.GameTimer;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.RecorderPanel;

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
        RecorderPanel.label.setText("Out of time! Reset in 3.");
        app.timer().runEvent(() -> {
            app.gameLoader().loadLevel(app.domain().getGameState().level());
        }, 3000);
    }
}
