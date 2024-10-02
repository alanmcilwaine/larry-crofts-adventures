package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Action that is mapped to a KeyEvent. Each enum corresponds to a key press.
 */
public enum Action{
    // Movement.
    Up(KeyEvent.VK_S, false, "Up"),
    Down(KeyEvent.VK_W, false, "Down"),
    Left(KeyEvent.VK_A, false, "Left"),
    Right(KeyEvent.VK_D, false, "Right"),

    // Hotkeys.
    Pause(KeyEvent.VK_SPACE, false, "Pause Game"),
    Resume(KeyEvent.VK_ESCAPE, false, "Resume Game"),
    Level1(KeyEvent.VK_1, true, "Load Level 1"),
    Level2(KeyEvent.VK_2, true, "Load Level 2"),
    ExitNoSave(KeyEvent.VK_X, true, "Force Exit"),
    ExitSave(KeyEvent.VK_S, true, "Exit with Save"),
    LoadSave(KeyEvent.VK_R, true, "Load Game");

    public int key;
    public boolean control;
    public String description;
    Action(int key, boolean control, String description){
        this.key = key;
        this.control = control;
        this.description = description;
    }
    public void key(int key) { this.key = key; }
    public static Optional<Action> getAction(int keyCode, boolean controlPressed){
        return Stream.of(values())
                .filter(a -> a.key == keyCode && a.control == controlPressed)
                .findFirst();
    }
}
