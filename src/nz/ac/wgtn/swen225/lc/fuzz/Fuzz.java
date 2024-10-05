package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.app.Controller;
import nz.ac.wgtn.swen225.lc.app.Action;
import nz.ac.wgtn.swen225.lc.domain.DomainTest.GameItemTest;
import nz.ac.wgtn.swen225.lc.domain.DomainTest.PlayerMoveTest;
import nz.ac.wgtn.swen225.lc.domain.DomainTest.RobotMovementTest;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import nz.ac.wgtn.swen225.lc.persistency.PersistencyTest;
import nz.ac.wgtn.swen225.lc.recorder.RecorderTests;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fuzz {

    public static void main(String[] arg){

        fuzzTest();
        testAllJUnits();
    }

    /**
     * Calls all tests
     */
    @Test void runTests(){
        testAllJUnits();
    }
    /**
     * Uses reflection to find all @Test methods and run them in every test class
     */
    static void testAllJUnits() {
        List.of(
                new RecorderTests(),
                new PersistencyTest(),
                new GameItemTest(),
                new RobotMovementTest(),
                new PlayerMoveTest()
        ).forEach(pt -> Arrays.stream(pt.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Test.class))
                .forEach(m -> {
                    m.setAccessible(true);
                    try {
                        m.invoke(pt);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        // Unwrap the exception thrown by the test method
                        Throwable cause = e.getCause();
                        if (cause instanceof IOException) {
                            System.err.println("Test failed due to IOException: " + cause.getMessage());
                            cause.printStackTrace();
                        } else {
                            throw new RuntimeException(cause); // Rethrow the actual cause
                        }
                    }
                })
        );
    }

    /**
     * Creates an implementation of App that allows us to auto test our code
     */
    static void fuzzTest(){
        FuzzController fuzzController = new FuzzController();
        List<Command> commands = new ArrayList<>();
        /**
         * Create a Runnable that creates the App
         *
         * This runnable will override tick with our own implementation to test
         */
        Runnable appCreator = () -> new App(fuzzController){
            {
                fuzzController.setUpKeyChooser(this);
            }
            public void tick(){
                try {
                    fuzzController.randomizeInputs();
                    commands.add(controller.currentCommand());
                    recorder.tick(controller.currentCommand());
                    giveInput(controller.currentCommand());
                    updateGraphics();
                }catch (Throwable t){
                    saveInputs(commands, domain.getGameState().level());
                    System.out.println("ERROR CAUGHT BY FUZZ: Was saved as level" + domain.getGameState().level() +"-recording");
                    throw new Error(t);
                }
            }
        };


        SwingUtilities.invokeLater(appCreator);
    }

}
