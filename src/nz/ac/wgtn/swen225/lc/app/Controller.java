package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

class Controller extends Keys{
    //Default keys
    // TODO Store a runnable in here, as a reference to the last thing pressed.
    enum Action{
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
    static HashMap<Action, Integer> bindings = new HashMap<>(Map.of(
            Action.UP,KeyEvent.VK_W,
            Action.DOWN,KeyEvent.VK_S,
            Action.LEFT,KeyEvent.VK_A,
            Action.RIGHT,KeyEvent.VK_D
    ));
    public Controller(){
        setAction(bindings.get(Action.UP), () -> currentCommand = Command.Up, () -> currentCommand = Command.None);
        setAction(bindings.get(Action.DOWN), () -> currentCommand = Command.Down, () -> currentCommand = Command.None);
        setAction(bindings.get(Action.LEFT), () -> currentCommand = Command.Left, () -> currentCommand = Command.None);
        setAction(bindings.get(Action.RIGHT), () -> currentCommand = Command.Right, () -> currentCommand = Command.None);
    }
}