package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;

public class GameTimer extends Timer {
    public GameTimer(Runnable tick) {
        super(App.TICK_RATE, (unused) -> tick.run());
    }

    public void onExitTile(Runnable nextLevel){
        this.stop();
        Timer exitDelay = new Timer(2000, e -> {
            nextLevel.run();
            this.start();
        });
        exitDelay.setRepeats(false);
        exitDelay.start();
    }
}