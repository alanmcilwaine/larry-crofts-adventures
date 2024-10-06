package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.Action;
import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a fake controller that generates random inputs
 */
class FuzzController extends Controller {
    /**
     * Presses and releases random keys in every possible way. This will test corner cases that would be almost impossible to recreate.
     * For example releasing a key twice before ever pressing it.
     */
    private FuzzKeyChooser keyChooser;
    List<Action> keyLogger = new ArrayList<>();
    int level = -1;
    App app;

    /**
     * Give the key chooser an app so that it can make smart decisions
     * @param app
     */
    void setUpKeyChooser(App app){
        this.app = app;
        this.keyChooser = new FuzzKeyChooser(app);
        changedLevel();
    }

    /**
     * Creates pseudo random inputs, based on keyChoosers desired keys.
     */
    void randomizeInputs(){
        Action keyPressed = nextKey();
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
    Action nextKey() {
        return keyChooser.nextKey();
    }

    /**
     * Purely random key. Will generate a random number between 0-4 end exclusive, then truncated to get a key between index 0-3
     * @return The random key
     */
    Action randomKey(){
        return List.of(Action.Up, Action.Down, Action.Left, Action.Right).get((int)(Math.random()*4));
    }

    /**
     * If the level is over we need a new keyChooser
     */
    void changedLevel(){
        if(app.domain.getGameState().level() != level){
            level = app.domain.getGameState().level();
            this.keyChooser = new FuzzKeyChooser(app);
        }
    }
}