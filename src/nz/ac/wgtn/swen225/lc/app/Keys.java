package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Stack;

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
    protected final Map<Integer, Runnable> actionsPressed= new HashMap<>();
    protected final Map<Integer, Runnable> actionsReleased= new HashMap<>();

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
        actionsPressed.put(action.key, onPressed);
        actionsReleased.put(action.key, () -> inputBuffer.remove(Command.generate(action.name())));
    }

    @Override
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        // We make sure the queue doesn't already contain this command.
        Optional<Action> a = Action.getAction(e.getKeyCode());
        if (a.isEmpty()){
            return;
        }
        Command command = Command.generate(a.get().name());
        if (inputBuffer.stream().anyMatch(c -> c.equals(command))){ // Buffer already contains the movement.
            return;
        }

        actionsPressed.getOrDefault(e.getKeyCode(), ()->{}).run();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        actionsReleased.getOrDefault(e.getKeyCode(), ()->{}).run();
    }
    @Override
    public void keyTyped(KeyEvent e){}
}