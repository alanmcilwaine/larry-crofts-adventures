package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * This will add a EntrySet to the actionsPressed and actionsReleased maps that will be executed when keys are pressed
 * actionsPressed -> runs the onPressed Runnable passed in, then sets the opposite key (opposite of UP is DOWN) to do
 * this keys onPressed Runnable.
 * This way, if you hold down UP, then without letting go of UP, hold down DOWN, when you let go of DOWN, UP's runnable
 * will run, resulting in the player moving upwards again.
 *
 * actionsReleased -> runs the action.stop, which either sets currentCommand to NONE, or runs the opposite keys runnable
 * (see above)
 *
 * @author John Rais and Alan McIlwaine
 */
class Keys implements KeyListener {
    protected static Command currentCommand = Command.None;
    protected final Runnable noCommand = () ->{currentCommand = Command.None;};
    private final Map<Integer,Runnable> actionsPressed= new HashMap<>();
    private final Map<Integer,Runnable> actionsReleased= new HashMap<>();

    /**
     * Links actions from a keyboard in the form of enum Action,
     * which holds the key pressed and opposite key to a Runnable action in the game.
     *
     * @param action Action holds the KeyEvent from the keyboard and the opposite key.
     * @param onPressed The action to do when the key is pressed.
     */
    public void setAction(Controller.Action action, Runnable onPressed){
        actionsPressed.put(action.key, () -> {onPressed.run(); action.opposite.stop = onPressed;});
        //On released depends on what other keys you are holding down, so it can change
        actionsReleased.put(action.key, () -> {action.stop.run(); action.opposite.stop = noCommand;});
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        actionsPressed.getOrDefault(e.getKeyCode(), ()->{}).run();
    }

    @Override
    public void keyReleased(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        actionsReleased.getOrDefault(e.getKeyCode(), ()->{}).run();
    }
}