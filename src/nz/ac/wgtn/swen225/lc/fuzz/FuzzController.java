
package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.Inputs.Action;
import nz.ac.wgtn.swen225.lc.app.Inputs.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces the human controller with one that automatically generates smart inputs for testing
 *
 * @author John Rais raisjohn@ecs.vuw.ac.nz
 * @version 2.0
 */
class FuzzController extends Controller {
    /**
     * Presses and releases random keys in every possible way. This will test corner cases that would be almost impossible to recreate.
     * For example releasing a key twice before ever pressing it.
     */
    List<Action> keyLogger = new ArrayList<>();

    /**
     * Creates pseudo random inputs, based on keyChoosers desired keys.
     */
    void randomizeInputs(FuzzKeyChooser strategy){
        Action keyPressed = nextKey(strategy);
        keyLogger.add(keyPressed);
        actionsPressed.getOrDefault(keyPressed, ()->{}).run();

        Action keyReleased = randomKey();
        keyLogger.add(keyReleased);
        actionsReleased.getOrDefault(keyReleased, ()->{}).run();
    }

    /**
     * The next key from our smart keyChooser
     *
     * @return Will be slightly random, but more likely to be a move that guides us somewhere new.
     */
    Action nextKey(FuzzKeyChooser strategy) {
        return strategy.nextKey();
    }

    /**
     * Purely random key. Will generate a random number between 0-4 end exclusive, then truncated to get a key between index 0-3
     * @return The random key
     */
    Action randomKey(){
        return List.of(Action.Up, Action.Down, Action.Left, Action.Right).get((int)(Math.random()*4));
    }

}