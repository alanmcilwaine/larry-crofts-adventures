package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;

public class GameTimer extends Timer {
    public GameTimer(Runnable tick) {
        super(App.TICK_RATE, (unused) -> tick.run());
    }

    /*
    public static void delay(int delay, Runnable r){
        delay -= App.TICK_RATE;
        if (delay <= 0) {
            r.run();
            delay = initialDelay;
        }
    }
    */


}