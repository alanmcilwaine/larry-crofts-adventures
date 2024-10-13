/**
 * The Recorder interface defines the basic actions for saving, loading,
 * and controlling the playback of recordings. Implementations of this
 * interface provides mechanisms for undoing and redoing actions that
 * were recorded in previous games as well as playing and pausing recordings.
 * Saving to and loading from file is delegated to Persistence package.
 *
 * <p>
 * Usage Example:
 * <pre>
 *     undoButton.addActionListener(recorder.undo());
 *     undoButton.addActionListener(recorder.redo());
 *     undoButton.addActionListener(recorder.play());
 *
 *     //On tick
 *     recorder.tick(userCommand);
 * </pre>
 * </p>
 *
 * @author John Rais raisjohn@ecs.vuw.ac.nz
 * @version 1.2
 */
package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.AppInterface;
import nz.ac.wgtn.swen225.lc.app.Inputs.Command;

public interface Recorder {

    /**
     * Save the current recording to a file.
     * The name of the saved file will be chosen internally.
     *
     * @param commands A list of all commands recorded in that game.
     */
    void setCommands(List<Command> commands);

    /**
     * Sets the speed of the timer that replays the recording
     *
     * @param tickTime in millis
     */
    void setPlaybackSpeed(int tickTime);

    /**
     * Get the current commands, so that we can pass them to persistence for saving
     *
     * @return all current commands, unmodifiable list
     */
    List<Command> getCommands();

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
     * Supplies an action that can be given to a Button.
     * Deletes all the recorded actions after this point, allowing you to resume play.
     *
     * @return an Action which allows you to take control of the recording
     */
    Action takeControl();

    /**
     * Can you undo? Checks to see if the stack of commands are empty.
     *
     * @return if you are able to undo
     */
    boolean canUndo();
    /**
     * Can you redo? Checks to see if the stack of undos are empty.
     *
     * @return if you are able to redo
     */
    boolean canRedo();
    /**
     * Supplies a Recorder (GameRecorder)
     *
     * @return GameRecorder, which in turn creates a playback
     */
    static Recorder create(AppInterface app){return new GameRecorder(app);}
}
