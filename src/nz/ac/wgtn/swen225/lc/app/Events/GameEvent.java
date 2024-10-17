package nz.ac.wgtn.swen225.lc.app.Events;

import nz.ac.wgtn.swen225.lc.app.App;

/**
 * GameEvent --- An event in the game checked every tick. The check code will execute the
 * event code.
 *
 * @author Alan McIlwaine 300653905
 */
public interface GameEvent {
    /**
     * Checks an event is possible. If so, will activate the event.
     */
    void check(App app);
}
