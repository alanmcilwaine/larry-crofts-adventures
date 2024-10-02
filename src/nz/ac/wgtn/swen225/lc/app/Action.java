package nz.ac.wgtn.swen225.lc.app;

import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Action that is mapped to a KeyEvent. Each enum corresponds to a key press.
 */
public enum Action{
    Up(KeyEvent.VK_S),
    Down(KeyEvent.VK_W),
    Left(KeyEvent.VK_A),
    Right(KeyEvent.VK_D);

    public int key;
    Action(int key){
        this.key = key;
    }
    public void key(int key) { this.key = key; }
    public static Optional<Action> getAction(int keyCode){
        return Stream.of(values())
                .filter(a -> a.key == keyCode)
                .findFirst();
    }
}
