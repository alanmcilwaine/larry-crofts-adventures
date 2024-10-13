/**
 * Creates smart random inputs to test the game automatically.
 * Tests run for a set period, and save commands that were used to a file if the game crashes
 *
 * @author John Rais raisjohn@ecs.vuw.ac.nz
 * @version 2.0
 */
package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Inputs.Command;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.GamePanel;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Fuzz {
    //Should we draw the game every time it ticks (will significantly slow down the tests)
    private static final boolean draw = true;
    //Max time the tests will run for
    private static final int TEST_TIME = 60;
    /**
     * Test level 1 for 60 seconds
     */
    @Test
    @Timeout(TEST_TIME + 1) // Only run test for 1 minute, if longer throw an error
    void testLevel1(){
        fuzzTest(1);
    }

    /**
     * Test level 2 for 60 seconds
     */
    @Test
    @Timeout(TEST_TIME + 1) // only run test for 1 minute, if longer throw an error
    void testLevel2(){fuzzTest(2);}

    /**
     * Creates an implementation of App that allows us to auto test our code with randomly generated inputs
     */
    static void fuzzTest(int level){

        //Create an app when we run this runnable, set it up to auto generate keyboard inputs
        Runnable appCreator = appCreator(level);

        //Open the app window
        SwingUtilities.invokeLater(appCreator);

        // Sleep to keep the test running for a set duration
        try {
            Thread.sleep(TEST_TIME * 1000); // Keep the test running for 60 seconds
        } catch (InterruptedException e) {
            System.out.println("Interruption: " + e);
        }
    }

    /**
     * Makes a runnable that creates an app when it is run.
     * @param level The level the app should immediately load
     *
     * @return The runnable with an app () -> new app
     */
    static Runnable appCreator(int level){
        //The controller that deals with the fake keyboard inputs
        FuzzController fuzzController = new FuzzController();
        //List of all commands that were triggered. Save them when the game crashes
        List<Command> commands = new ArrayList<>();

        //The modified app is wrapped in a runnable so it can be created on the correct thread
        return () -> new App(fuzzController){
            final FuzzKeyChooser keyStrategy = new FuzzKeyChooser(this);
            /**
             * Make a fake game panel, so that we don't waist time on drawing
             * @return a game panel that does nothing
             */
            @Override
            public GamePanel makePanel(){return fakePanel(this);}

            /**
             * Set up and load the correct level for this test.
             */
            {
                if(level != 1) {
                    domain = Persistency.loadGameBoard(level);
                    initialDomain = domain.copyOf();
                }
            }

            /**
             * Override tick so that we can test quicker.
             */
            @Override
            public void tick(){
                //Reload level on finish
                if (domain.getGameState().player().isNextLevel() || domain.getGameState().player().isDead()) {
                    domain = initialDomain.copyOf();
                }
                try {
                    fuzzController.randomizeInputs(keyStrategy);

                    var command = controller.currentCommand();
                    commands.add(command);
                    recorder.tick(command);
                    giveInput(command);
                }catch (Throwable t){
                    saveInputs(commands, domain.getGameState().level());
                    System.out.println("ERROR CAUGHT BY FUZZ: Was saved as level" + domain.getGameState().level() +"-recording");
                    t.printStackTrace();
                    throw new Error(t);
                }
            }
        };
    }
    /**
     * Create a game panel that can draws nothing, to make our tests faster
     */
    private static GamePanel fakePanel(App app){
        return new GamePanel(app) {
            @Override
            protected void paintComponent(Graphics g) {
                if(draw) super.paintComponent(g);
            }
        };
    }
}
