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

    /**
     * Give the key chooser an app so that it can make smart decisions
     * @param app
     */
    void setUpKeyChooser(App app){
        keyChooser = new FuzzKeyChooser(app);
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
        return keyChooser.nextKey();
    }
    List<Action> keyLogger = new ArrayList<>();
}