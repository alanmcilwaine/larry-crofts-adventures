package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.AppInterface;
import javax.swing.*;

/**
 * A clean way to set up timers, not exposing the unused Action, and insuring it is running at the correct tick rate
 *
 * <p>
 * Usage Example:
 * <pre>
 *     timer = new PlaybackTimer(this::redoFrame);
 * </pre>
 *
 *
 * @author John Rais raisjohn@ecs.vuw.ac.nz
 * @version 1.0
 */
public class PlaybackTimer extends Timer {
    /**
     * A timer that ticks based on AppInterfaces TICK_RATE, running the supplied runnable every frame.
     *
     * @param r the runnable you want ot run every tick
     */
    PlaybackTimer(Runnable r){
        super(AppInterface.TICK_RATE, (unused) -> r.run());
    }

}
