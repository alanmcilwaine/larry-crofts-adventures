package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import nz.ac.wgtn.swen225.lc.app.Keys;

class Controller extends Keys{
    //Default keys

    enum Action{
        UP(KeyEvent.VK_W),
        DOWN(KeyEvent.VK_S),
        LEFT(KeyEvent.VK_A),
        RIGHT(KeyEvent.VK_D);

        int key; Action opposite; Runnable stop = () -> {};

        static{
            UP.opposite = DOWN;
            DOWN. opposite = UP;
            LEFT.opposite = RIGHT;
            RIGHT.opposite = LEFT;
        }
        Action(int key){this.key = key;}
    }

    Controller(){
        setAction(Action.UP, () -> currentCommand = Command.Up);
        setAction(Action.DOWN, () -> currentCommand = Command.Down);
        setAction(Action.LEFT, () -> currentCommand = Command.Left);
        setAction(Action.RIGHT, () -> currentCommand = Command.Right);

        //set all the stop functions.
        Arrays.stream(Action.values()).forEach(action -> action.stop = noCommand);
    }
}