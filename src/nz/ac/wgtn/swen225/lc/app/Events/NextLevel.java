package nz.ac.wgtn.swen225.lc.app.Events;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;

/**
 * NextLevel --- Checks if the player is on the next level tile. If it is, we will pause the game
 * and update domain to the next level.
 *
 * @author Alan McIlwaine 300653905
 */
public class NextLevel implements GameEvent {
    @Override
    public void check(App app) {
        Player player = app.domain().getGameState().player();
        if (player.isNextLevel()) {
            app.timer().runEvent(() -> app.gameLoader().loadLevel(app.domain().getGameState().level() + 1), 1000);
        }
    }
}