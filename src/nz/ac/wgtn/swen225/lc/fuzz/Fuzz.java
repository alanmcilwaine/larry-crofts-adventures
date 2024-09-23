package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.recorder.RecorderTests;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.IntStream;

public class Fuzz {

    public static void main(String[] arg){


        RecorderTests.main(arg);

        SwingUtilities.invokeLater(App::new);

    }
    class FuzzController extends Controller{
        void randomizeInputs(){
            actionsPressed.getOrDefault(KeyEvent.VK_UP, ()->{}).run();
        }
    }

    /**
     * Create num commands. Each command is random. Commands can be NONE
     *
     * @param num the number of commands to generate
     * @return the commands generated
     */
    static List<Controller.Action> randomActions(int num){
        return IntStream.range(0,num)
                .mapToObj(i -> Controller.Action.values()[(int)Math.round(Math.random()*4)])
                .toList();
    }
}
