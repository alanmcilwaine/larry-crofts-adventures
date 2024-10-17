package nz.ac.wgtn.swen225.lc.app.Inputs;

import java.util.EmptyStackException;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.RecorderPanel;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;

/**
 * Controller --- Sets up the initial keys for the game.
 *
 * @author Alan McIlwaine 300653905
 */
public class Controller extends Keys{
    /**
     * Maps the default key presses to their relevant action in the game. This version is used in testing.
     */
    public Controller(){
        setAction(Action.Up, () -> inputBuffer.add(Command.Up));
        setAction(Action.Down, () -> inputBuffer.add(Command.Down));
        setAction(Action.Left, () -> inputBuffer.add(Command.Left));
        setAction(Action.Right, () -> inputBuffer.add(Command.Right));
    }

    /**
     * Maps the default key presses to their relevant action in the game. This version is used in a GUI app.
     * @param app App to call methods specific to the GUI.
     */
    public Controller(App app) {
        this();
        setAction(Action.Pause, () -> {
            app.pauseTimer(true);
            RecorderPanel.label.setText("Pausing Game");
        });
        setAction(Action.Resume, () -> {
            app.pauseTimer(false);
            RecorderPanel.label.setText("Resuming Game");
        });
        setAction(Action.Level0, () -> app.gameLoader().loadLevel(0));
        setAction(Action.Level1, () -> app.gameLoader().loadLevel(1));
        setAction(Action.Level2, () -> app.gameLoader().loadLevel(2));
        setAction(Action.LoadSave, () -> {
            String path = app.openFile();
            if (path.isEmpty()) {
                return;
            }
            if (path.contains("save_")) {
                app.gameLoader().loadRecording(Persistency.loadRecording(app.recorder(), path));
            } else {
                app.gameLoader().loadLevel(Persistency.loadwithFilePath(path));
            }
            RecorderPanel.label.setText("Loaded Game");
        });
        setAction(Action.ExitSave, () -> {
            Persistency.saveProgress(app.recorder().getCommands(), app.domain().getGameState().level());
            System.exit(0);
        });
        setAction(Action.ExitNoSave, () -> System.exit(0));
    }

    /**
     * The current lined up movement command to run.
     * @return Movement command
     */
    public Command currentCommand() {
        try{
            return inputBuffer.peek();
        } catch (EmptyStackException e) {
            return Command.None;
        }
    }
}