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

    /**
     * Constructor to build the default timer for the game that calls tick every TICK_RATE ms.
     * @param tick A pointer to the tick() method in App.
     */
    public GameTimer(Runnable tick) {
        super(App.TICK_RATE, (unused) -> {
            GameTimer.stageCountdown -= ((double) TICK_RATE / 1000);
            tick.run();
        });
    }

    /**
     *
     * Code to run when the player meets an event.
     * @param event A runnable that will trigger the effects of the event.
     * @param delay Delay before the event is triggered in ms.
     */
    public void runEvent(Runnable event, int delay) {
        this.stop();
        Timer wait = new Timer(delay, e -> {
            event.run();
        });
        wait.setRepeats(false);
        wait.start();
    }


}