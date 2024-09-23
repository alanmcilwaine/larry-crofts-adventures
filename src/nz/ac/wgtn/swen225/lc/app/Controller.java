package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import nz.ac.wgtn.swen225.lc.app.Keys;

/**
 * Controller --- Sets up the initial keys for the game.
 *
 * @author Alan McIlwaine && John Rais
 */
public class Controller extends Keys implements InputManager{
    /**
     * Action that is mapped to a KeyEvent. Each enum corresponds to a key press.
     */
    enum Action{
        UP(KeyEvent.VK_S),
        DOWN(KeyEvent.VK_W),
        LEFT(KeyEvent.VK_A),
        RIGHT(KeyEvent.VK_D);

        int key;
        Action opposite;
        Runnable stop = () -> {};

        static{
            UP.opposite = DOWN;
            DOWN.opposite = UP;
            LEFT.opposite = RIGHT;
            RIGHT.opposite = LEFT;
        }
        Action(int key){this.key = key;}
    }

    /**
     * Maps the default key presses to their relevant action in the game.
     */
    public Controller(){
        setAction(Action.UP, () -> Keys.currentCommand = Command.Up);
        setAction(Action.DOWN, () -> Keys.currentCommand = Command.Down);
        setAction(Action.LEFT, () -> Keys.currentCommand = Command.Left);
        setAction(Action.RIGHT, () -> Keys.currentCommand = Command.Right);

        //set all the stop functions.
        Arrays.stream(Action.values()).forEach(action -> action.stop = noCommand);
    }
}