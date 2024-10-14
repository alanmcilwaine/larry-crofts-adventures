package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import nz.ac.wgtn.swen225.lc.render.ImageImplement;

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
    private final JMenu help = new JMenu("Help");

    /**
     * Constructor to build the Menu elements for the screen.
     * @param app App to pull the relevant methods to call in the menu.
     */
    public Menu(App app){
        file(app);
        input(app);
        help(app);
        add(file);
        add(input);
        add(help);
    }

    /**
     * Inputs the Menu Items under the File field in Menu
     * @param app App to pull the relevant methods to call in the menu.
     */
    private void file(App app){
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");
        load.addActionListener((unused) -> {
            String path = app.openFile();
            if (!path.isEmpty()){
                app.loadLevel(Persistency.loadwithFilePath(path));
            }
        });
        save.addActionListener((unused) -> {
            Persistency.saveProgress(app.domain);
        });
        exit.addActionListener((unused) -> System.exit(1));
        List.of(load, save, exit).forEach(file::add);
    }

    /**
     * Inputs the Menu Items under the Input field in Menu
     * @param app App to pull the relevant methods to call in the menu.
     */
    private void input(App app){
        JMenuItem saveInputs = new JMenuItem("Save Inputs");
        JMenuItem loadInputs = new JMenuItem("Load Inputs");
        saveInputs.addActionListener((unused) -> Persistency.saveCommands(app.recorder.getCommands(), app.domain.getGameState().level()));
        loadInputs.addActionListener((unused) -> {
            String filename = app.openFile();
            if (!filename.isEmpty()){
                app.loadRecording(Persistency.loadRecording(app.recorder, filename));
                app.pauseTimer(true);
            }
        });
        List.of(saveInputs, loadInputs).forEach(input::add);
    }

    /**
     * Inputs the Help Items to give the player help information
     * @param app App to pull the relevant methods to call in the menu.
     */
    private void help(App app) {
        JMenuItem controls = new JMenuItem("Controls");
        controls.addActionListener((unused) -> {
            app.pauseTimer(true);
            new RemapFrame();
        });
        help.add(controls);
    }
}
