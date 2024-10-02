package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.app.Controller;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import nz.ac.wgtn.swen225.lc.recorder.RecorderTests;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Fuzz {

    public static void main(String[] arg){


        RecorderTests.main(arg);
        FuzzController fuzzController = new FuzzController();
        List<Command> commands = new ArrayList<>();
        int level = 1;
        /**
         * Create a Runnable that creates the App
         *
         * This runnable will override tick with our own implementation to test
         */
        Runnable appCreator = () -> new App(fuzzController){
                {
                    //movementWait = 0;
                }
                public void tick(){
                    try {
                        fuzzController.randomizeInputs();
                        commands.add(controller.currentCommand());
                        recorder.tick(controller.currentCommand());
                        giveInput(controller.currentCommand());
                    }catch (Throwable t){
                        System.out.println("ERROR CAUGHT BY FUZZ: " + fuzzController.keyLogger + "\n\n");
                        saveInputs(commands, level);
                        //throw new Error(t);
                    }
                }
            };


        SwingUtilities.invokeLater(appCreator);



    }

    static void saveInputs(List<Command> commands, int level){

        Persistency.saveCommands(commands, level);

    }

    /**
     * Creates a fake controller that generates random inputs
     */
    static class FuzzController extends Controller {
        void randomizeInputs(){
            int keyPressed = randomKey();
            keyLogger.add(keyPressed);
            actionsPressed.getOrDefault(keyPressed, ()->{}).run();


            int keyReleased = randomKey();
            keyLogger.add(keyReleased);
            actionsReleased.getOrDefault(keyReleased, ()->{}).run();
        }

        int randomKey(){
            return keys.get((int) (Math.random()*3.99));
        }
        List<Integer> keys = List.of(KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D);
        List<Integer> keyLogger = new ArrayList<>();
    }


}
