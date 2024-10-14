package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;

/**
 * GameTimer --- Holds information about the timer that causes the game to tick.
 * Has methods for specific events to cause a delay before going forward with the event.
 *
 * @author Alan McIlwaine 300653905
 */
public class GameTimer extends Timer {
    public static int TICK_RATE = 50; // Call the tick every 50ms.
    public static final int INPUT_WAIT = GameTimer.TICK_RATE * 3; // Do a movement every 3 ticks.
    public static double stageCountdown = 0;

    public GameTimer(Runnable tick) {
        super(App.TICK_RATE, (unused) -> {
            GameTimer.stageCountdown -= ((double) TICK_RATE / 1000);
            tick.run();
        });
    }

    /**
     * Code to run when the player goes on an exit tile.
     * @param nextLevel Runnable to go to the next level.
     */
    public void onExitTile(Runnable nextLevel){
        this.stop();
        Timer delay = new Timer(1000, e -> {
            nextLevel.run();
        });
        delay.setRepeats(false);
        delay.start();
    }

    /**
     * Code to run when the player dies
     * @param death Runnable to restart the level.
     */
    public void onDeath(Runnable death) {
        this.stop();
        Timer delay = new Timer(5000, e -> {
            death.run();
        });
        delay.setRepeats(false);
        delay.start();
    }


}