package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.AppInterface;

import javax.swing.*;

/**
 * A timer that ticks based on AppInterfaces TICK_RATE, running the supplied runnable every frame.
 */
public class PlaybackTimer extends Timer {

    PlaybackTimer(Runnable r){
        super(AppInterface.TICK_RATE, (unused) -> r.run());
    }

}
