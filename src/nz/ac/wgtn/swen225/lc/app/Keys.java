package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

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
    protected Stack<Command> inputBuffer = new Stack<>();
    protected final Map<Action, Runnable> actionsPressed= new HashMap<>();
    protected final Map<Action, Runnable> actionsReleased= new HashMap<>();

    public static final int INPUT_WAIT = App.TICK_RATE * 3;
    public int movementWaitTime = 0;

    /**
     * Links actions from a keyboard in the form of enum Action,
     * which holds the key pressed and opposite key to a Runnable action in the game.
     *
     * @param action Action holds the KeyEvent from the keyboard and the opposite key.
     * @param onPressed The action to do when the key is pressed.
     */
    public void setAction(Action action, Runnable onPressed){
        actionsPressed.put(action, onPressed);
        // If it's a movement action we need to remove it from the input buffer.
        Runnable r = List.of("Up", "Down", "Left", "Right").contains(action.name()) ? () -> inputBuffer.remove(Command.generate(action.name())) : () -> {};
        actionsReleased.put(action, r);
    }

    @Override
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        Optional<Action> a = Action.getAction(e.getKeyCode(), e.isControlDown());
        if (a.isEmpty()){
            return;
        }
        // Checks specific to movement.
        if (List.of("Up", "Down", "Left", "Right").contains(a.get().name())){
            Command command = Command.generate(a.get().name());
            if (inputBuffer.stream().anyMatch(c -> c.equals(command))){ // Buffer already contains the movement.
                return;
            }
        }
        actionsPressed.getOrDefault(a.get(), ()->{}).run();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        Optional<Action> a = Action.getAction(e.getKeyCode(), e.isControlDown());
        if (a.isEmpty()){
            return;
        }
        actionsReleased.getOrDefault(a.get(), ()->{}).run();
    }
    @Override
    public void keyTyped(KeyEvent e){}
}