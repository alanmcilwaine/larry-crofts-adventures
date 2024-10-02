package nz.ac.wgtn.swen225.lc.app;

/**
 * AppInterface --- Holds the default commands that should be the only things read by Recorder and Persistency for testing.
 *
 * @author Alan McIlwaine 300653905
 */
public interface AppInterface {
    int TICK_RATE = 50;

    /**
     * updateGraphics()
     * Sends an update request to graphics to update the graphics. Used after updating state in domain.
     */
    void updateGraphics();

    /**
     * giveInput()
     * Takes in an input, and sends to the domain to update state.
     * @param input An input in the game, e.g WASD as a command.
     */
    void giveInput(Command input);

    /**
     * initialStateRevert()
     * Tells domain to revert to the starting state of the game. Like a reset.
     * This is used by recorder to go from the start, so it can undo moves.
     */
    void initialStateRevert();

    /**
     * toggleTimer()
     * Stops the timer that runs the ticks if the timer is running. Otherwise, resume the timer.
     */

    /**
     * pauseTimer
     * Pauses or resumes the game based on the state given. True is pause, False is resume.
     * @param state True for pause, false for resume
     */
    void pauseTimer(boolean state);

    /**
     * openFile()
     * Opens a file at the given location. Note that it is expected to handle the "" case when no file is inputted.
     * @return File name.
     */
    String openFile();


    /**
     * saveFile()
     * Saves a file at the given location. Note that it is expected to handle the "" case when no file is inputted.
     * @return File name.
     */
    String saveFile();
}
