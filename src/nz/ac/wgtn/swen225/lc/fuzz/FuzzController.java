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
    int level = -1;
    App app;

    /**
     * Give the key chooser an app so that it can make smart decisions
     * @param app
     */
    void setUpKeyChooser(App app){
        this.app = app;
        this.keyChooser = new FuzzKeyChooser(app);
    }
    void randomizeInputs(){
        Action keyPressed = nextKey();
        keyLogger.add(keyPressed);
        actionsPressed.getOrDefault(keyPressed, ()->{}).run();


        Action keyReleased = nextKey();
        keyLogger.add(keyReleased);
        actionsReleased.getOrDefault(keyReleased, ()->{}).run();
    }

    Action nextKey() {
        //Check to see if this last frame we have changed levels
        changedLevel();

        return keyChooser.nextKey();
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
    List<Action> keyLogger = new ArrayList<>();
}