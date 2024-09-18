package nz.ac.wgtn.swen225.lc.app;

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
     * openFile()
     * @return Filename of the save file that has been opened
     */
    String openFile();
}
