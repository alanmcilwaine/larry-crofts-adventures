package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.app.GamePanel;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Fuzz {

    /**
     * Test level 1 for 60 seconds
     */
    @Test
    @Timeout(62) // Only run test for 1 minute, if longer throw an error
    void testLevel1(){
        fuzzTest(1);
    }

    /**
     * Test level 2 for 60 seconds
     */
    @Test
    @Timeout(62) // only run test for 1 minute, if longer throw an error
    void testLevel2(){fuzzTest(2);}

    /**
     * Creates an implementation of App that allows us to auto test our code with randomly generated inputs
     */
    static void fuzzTest(int level){
        FuzzController fuzzController = new FuzzController();
        List<Command> commands = new ArrayList<>();
        //Make game field accessable so that we can set it to something that does not draw.

        /**
         * Create a Runnable that creates the App
         *
         * This runnable will override tick with our own implementation to test
         */
        Runnable appCreator = () -> new App(fuzzController){
            /**
             * Make a fake game panel, so that we don't waist time on drawing
             * @return a game panel that does nothing
             */
            @Override
            public GamePanel makePanel(){
                return new GamePanel(this) {
                    @Override
                    protected void paintComponent(Graphics g) {/*DO NOTHING*/ }
                };
            }

            /**
             * Set up and load the correct level for this test.
             */
            {
                fuzzController.setUpKeyChooser(this);
                //Load level 2, 3 etc.
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
                    fuzzController.randomizeInputs();
                    commands.add(controller.currentCommand());
                    recorder.tick(controller.currentCommand());
                    giveInput(controller.currentCommand());
                }catch (Throwable t){
                    saveInputs(commands, domain.getGameState().level());
                    System.out.println("ERROR CAUGHT BY FUZZ: Was saved as level" + domain.getGameState().level() +"-recording");
                    t.printStackTrace();
                    throw new Error(t);
                }
            }
        };

        //Open the app window
        SwingUtilities.invokeLater(appCreator);

        // Sleep to keep the test running for a set duration
        try {
            Thread.sleep(60000); // Keep the test running for 60 seconds
        } catch (InterruptedException e) {
            System.out.println("Interruption: " + e);
        }
    }

}
