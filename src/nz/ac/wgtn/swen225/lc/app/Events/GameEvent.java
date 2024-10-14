package nz.ac.wgtn.swen225.lc.app.Events;

import nz.ac.wgtn.swen225.lc.app.App;

public interface GameEvent {
    /**
     * Checks an event is possible. If so, will activate the event.
     */
    void check(App app);
}
