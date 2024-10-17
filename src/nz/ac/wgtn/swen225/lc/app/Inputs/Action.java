package nz.ac.wgtn.swen225.lc.app.Inputs;

import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Action ---- Each Key is mapped to an event mapped. Each enum corresponds to a key press.
 *
 * @author Alan McIlwaine 300653905
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
    Level0(KeyEvent.VK_0, true, "Load Level 0"),
    Level1(KeyEvent.VK_1, true, "Load Level 1"),
    Level2(KeyEvent.VK_2, true, "Load Level 2"),
    ExitNoSave(KeyEvent.VK_X, true, "Force Exit"),
    ExitSave(KeyEvent.VK_S, true, "Exit with Save"),
    LoadSave(KeyEvent.VK_R, true, "Load Game");

    private int key;
    public final boolean control;
    public final String description;
    Action(int key, boolean control, String description){
        this.key = key;
        this.control = control;
        this.description = description;
    }

    /**
     * Setter for key
     * @param key KeyEvent to set this action to.
     */
    public void key(int key) {
        this.key = key;
    }

    /**
     * Getter for key
     */
    public int key() {
        return key;
    }

    /**
     * Returns the action associated with the given keyCode. Will produce a different action depending on if
     * control is pressed.
     * @param keyCode KeyCode of the key pressed.
     * @param controlPressed If control on the keyboard is held alongside the key.
     * @return The action associated with the keyCode. Can return an empty optional if no association.
     */
    public static Optional<Action> getAction(int keyCode, boolean controlPressed){
        return Stream.of(values())
                .filter(a -> a.key == keyCode && a.control == controlPressed)
                .findFirst();
    }

}
