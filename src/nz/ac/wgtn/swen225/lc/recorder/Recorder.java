package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;

/**
 * The Recorder interface defines the basic actions for saving, loading,
 * and controlling the playback of recordings. Implementations of this
 * interface provides mechanisms for undoing and redoing actions that
 * were recorded in previous games as well as playing and pausing recordings.
 * Saving to and loading from file is delegated to Persistence package.
 */
public interface Recorder {

    /**
     * Save the current recording to a file.
     * The name of the saved file will be chosen internally.
     *
     * @param commands A list of all commands recorded in that game.
     */
    void setCommands(List<Command> commands);

    /**
     * Saves all info needed to replay this tick. Should be called every time the player "ticks" and moves.
     *
     * @param commandToSave Will be stored, then saved to a file, so the game can be replayed later.
     */
    void tick(Command commandToSave);

    /**
     * Supplies an action that can be given to a Button.
     * The internal undo logic will be executed when this action is called.
     *
     * @return an Action which calls the undo method
     */
    Action undo();

    /**
     * Supplies an action that can be given to a Button.
     * The internal redo logic will be executed when this action is called.
     *
     * @return an Action which calls the redo method
     */
    Action redo();

    /**
     * Supplies an action that can be given to a Button.
     * The internal play logic will be executed when this action is called.
     *
     * @return an Action which calls the play method
     */
    Action play();

    /**
     * Supplies an action that can be given to a Button.
     * The internal pause logic will be executed when this action is called.
     *
     * @return an Action which calls the pause method
     */
    Action pause();

    /**
     * Supplies a Recorder (GameRecorder)
     *
     * @return GameRecorder, which in turn creates a playback
     */
    static Recorder create(App app){
        return new GameRecorder(app);
    }
}
