package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.SwingUtilities;

/**
 * Keys --- Handles what to do if a key is pressed. Holds associations between specific actions and the event
 * they will cause.
 *
 * @author Alan McIlwaine 300653905
 */
class Keys implements KeyListener {
    protected Stack<Command> inputBuffer = new Stack<>();
    protected final Map<Action, Runnable> actionsPressed= new HashMap<>();
    protected final Map<Action, Runnable> actionsReleased= new HashMap<>();

    public int movementWaitTime = 0;

    private final List<Action> inputs = List.of(Action.Up, Action.Down, Action.Left, Action.Right);

    /**
     * Creates an association between the action and what that action will cause. On key press we will find the action, and call
     * the runnable.
     * @param action Holds the KeyEvent in the form of Action.
     * @param onPressed The thing to do once the key is pressed.
     */
    public void setAction(Action action, Runnable onPressed){
        actionsPressed.put(action, onPressed);
        actionsReleased.put(action, inputs.contains(action) ? () -> inputBuffer.remove(Command.generate(action.name())) : ()->{});
    }

    @Override
    public void keyPressed(KeyEvent e){
        assert SwingUtilities.isEventDispatchThread();
        Optional<Action> a = Action.getAction(e.getKeyCode(), e.isControlDown());
        if (a.isEmpty()){
            return;
        }
        if (inputs.contains(a.get()) && inputBuffer.stream().anyMatch(c -> c.equals(Command.generate(a.get().name())))) {
           return;
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