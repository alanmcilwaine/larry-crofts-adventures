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
                        throw new RuntimeException(e);
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

    /**
     * Creates a fake controller that generates random inputs
     */
    static class FuzzController extends Controller {
        void randomizeInputs(){
            Action keyPressed = randomKey();
            keyLogger.add(keyPressed);
            actionsPressed.getOrDefault(keyPressed, ()->{}).run();


            Action keyReleased = randomKey();
            keyLogger.add(keyReleased);
            actionsReleased.getOrDefault(keyReleased, ()->{}).run();
        }

        Action randomKey() {
            return List.of(Action.Up, Action.Down, Action.Left, Action.Right).get((int) (Math.random() * 4));
        }
        List<Action> keyLogger = new ArrayList<>();
    }


}
