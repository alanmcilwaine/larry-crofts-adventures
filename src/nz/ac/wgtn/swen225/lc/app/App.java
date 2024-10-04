package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.UI.AppFrame;
import nz.ac.wgtn.swen225.lc.app.UI.Menu;
import nz.ac.wgtn.swen225.lc.app.UI.UIPanel;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.render.ImageImplement;

/**
 * App --- Program to build the Application elements, and start the ticking process.
 *
 * @author Alan McIlwaine 300653905
 */
public class App extends AppFrame implements AppInterface{
    // Window is made up of two main panels
    private final GamePanel game; //Don't generate here as controller could be generated in constructor.
    private final UIPanel ui;
    private final Menu menu = new Menu(this);

    // Colours for the UI
    public static final Color FONT = new Color(31, 30, 25);

    // Window Dimensions
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    // Tick rate
    public static final int TICK_RATE = 50;

    // Game Information
    public static Controller controller;
    public Recorder recorder = Recorder.create(this); // Created earlier so UI can hook up buttons to recorder.
    public GameBoard domain;
    public GameBoard initialDomain;
    public ImageImplement render;

    public Timer tick = new Timer(TICK_RATE, (unused) -> tick());

    /**
     * App()
     * Loads the default UI and starts the game loop.
     */
    public App(){
        assert SwingUtilities.isEventDispatchThread();
        controller = new Controller(this);
        game = new GamePanel(this);
        ui = new UIPanel(this);
        setupUI();
        startTick();
    }

    public App(Controller c) {
        assert SwingUtilities.isEventDispatchThread();
        controller = c;
        game = new GamePanel(this);
        ui = new UIPanel(this);
        setupUI();
        startTick();
    }

    /**
     * setupUI()
     * Sets up the base UI for the game inside a frame and displays.
     * Includes configs for UI and Game panel.
     */
    private void setupUI(){
        add(game, BorderLayout.CENTER);
        add(ui, BorderLayout.EAST);
        setJMenuBar(menu);
        setVisible(true);
        game.setVisible(true);
    }


    /**
     * startTick()
     * Starts the main update loop for the program. Packages Domain, Renderer and Recorder should be used here.
     */
    private void startTick(){
        domain = Persistency.loadGameBoard(1);
        initialDomain = Persistency.loadGameBoard(1);
        render = ImageImplement.getImageImplement(game);

        tick.start();
    }

    /**
     * tick()
     * Code inside tick() is called every 50ms. This is for updating player movement
     * at a separate tick rate so movement isn't sluggish.
     */
    public void tick(){
        // Allow an input every Keys.INPUT_WAIT time
        Command input = Command.None;
        if (controller.movementWaitTime <= 0 && controller.currentCommand() != Command.None) {
            input = controller.currentCommand();
            controller.movementWaitTime = Keys.INPUT_WAIT;
        }

        recorder.tick(input);
        giveInput(input);
        updateGraphics();

        controller.movementWaitTime -= TICK_RATE;
    }

    /**
     * Saves a list of inputs to a JSON file.
     * @param commands List of inputs.
     * @param level What level the inputs were made on.
     */
    public static void saveInputs(List<Command> commands, int level) {
        Persistency.saveCommands(commands, level);
    }


    @Override
    public void updateGraphics(){
        ui.repaint();
        game.repaint();
    }

    @Override
    public void giveInput(Command input){
        domain.action(input.direction());
    }

    @Override
    public void initialStateRevert(){
        domain = initialDomain;
        initialDomain = Persistency.loadGameBoard(domain.getGameState().level());
    }

    @Override
    public void pauseTimer(boolean state) {
        if (state) {
            tick.stop();
        } else {
            tick.start();
            game.requestFocusInWindow();
        }
        String status = tick.isRunning() ? "Pause" : "Resume";
        ui.pause.setText(status);
    }

    @Override
    public String openFile(){
        JFileChooser loader = new JFileChooser(new File(System.getProperty("user.dir")));
        if (loader.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return loader.getSelectedFile().getPath();
        }
        return "";
    }

    @Override
    public String saveFile() {
        JFileChooser saver = new JFileChooser(new File(System.getProperty("user.dir")));
        if (saver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return saver.getSelectedFile().getPath();
        }
        return "";
    }

}
