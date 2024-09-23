package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;

import javax.swing.*;
import java.util.List;

/**
 * Menu --- Generates a menu at the top of the Frame to handle saving.
 *
 * @author Alan McIlwaine 300653905
 */
public class Menu extends JMenuBar {
    private final JMenu file = new JMenu("File");
    private final JMenu input = new JMenu("Input");
    public Menu(App a){
        file(a);
        input(a);
        add(file);
        add(input);
    }

    /**
     * Inputs the Menu Items under the File field in Menu
     * @param a App
     */
    private void file(App a){
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");
        load.addActionListener((unused) -> {
            String filename = a.openFile();
            if (!filename.isEmpty()){
                a.domain = Persistency.loadGameState(a.openFile());
            }
        });
        save.addActionListener((unused) -> {
           String filename = a.saveFile();
           if (!filename.isEmpty()){
               Persistency.saveGameState(a.saveFile(), a.domain.getGameState());
           }
        });
        exit.addActionListener((unused) -> System.exit(1));
        List.of(load, save, exit).forEach(file::add);
    }

    /**
     * Inputs the Menu Items under the Input field in Menu
     * @param a App
     */
    private void input(App a){
        JMenuItem saveInputs = new JMenuItem("Save Inputs");
        JMenuItem loadInputs = new JMenuItem("Load Inputs");
        saveInputs.addActionListener((unused) -> Persistency.saveCommands(a.recorder.getCommands(), a.domain.getGameState().level()));
        loadInputs.addActionListener((unused) -> {
            String filename = a.saveFile();
            if (!filename.isEmpty()){
                Persistency.loadRecording(a.recorder, a.openFile());
            }
        });
        List.of(saveInputs, loadInputs).forEach(input::add);
    }
}
