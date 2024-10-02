package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import nz.ac.wgtn.swen225.lc.app.Keys;

/**
 * Controller --- Sets up the initial keys for the game.
 *
 * @author Alan McIlwaine
 */
public class Controller extends Keys{
    /**
     * Maps the default key presses to their relevant action in the game.
     */
    public Controller(){
        setAction(Action.Up, () -> inputBuffer.add(Command.Up));
        setAction(Action.Down, () -> inputBuffer.add(Command.Down));
        setAction(Action.Left, () -> inputBuffer.add(Command.Left));
        setAction(Action.Right, () -> inputBuffer.add(Command.Right));
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