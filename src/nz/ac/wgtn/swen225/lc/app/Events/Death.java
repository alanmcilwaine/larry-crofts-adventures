package nz.ac.wgtn.swen225.lc.app.Events;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.GameTimer;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.RecorderPanel;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;

/**
 * Death --- Event to see if the player has died. If they have it will run logic on the timer.
 * @author Alan McIlwaine 300653905
 */
public class Death implements GameEvent {
    @Override
    public void check(App app) {
        Player player = app.domain().getGameState().player();
        if (!player.isDead()) {
            return;
        }
        RecorderPanel.label.setText("You have died. Reset in 5.");
        app.timer().runEvent(() -> {
            if (app.domain().getGameState().player().isDead()) { // We re-check if the player is dead because they can undo.
                app.gameLoader().loadLevel(app.domain().getGameState().level());
            }
        }, 5000);
    }
}
