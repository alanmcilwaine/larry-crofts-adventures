package nz.ac.wgtn.swen225.lc.app;

import java.util.EmptyStackException;

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
     * @param a App to call methods specific to the GUI.
     */
    public Controller(App a) {
        this();
        setAction(Action.Pause, () -> a.pauseTimer(true));
        setAction(Action.Resume, () -> a.pauseTimer(false));
        setAction(Action.Level1, () -> {
            a.loadLevel(1);
        });
        setAction(Action.Level2, () -> {
            a.loadLevel(2);
        });
        setAction(Action.LoadSave, () -> {
            String path = a.openFile();
            if (!path.isEmpty()){
                a.domain = Persistency.loadwithFilePath(path);
                a.initialDomain = a.domain.copyOf();
            }
        });
        setAction(Action.ExitSave, () -> {
           Persistency.saveProgress(a.domain);
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