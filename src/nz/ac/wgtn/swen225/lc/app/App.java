package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.UI.AppFrame;
import nz.ac.wgtn.swen225.lc.app.UI.GamePanel;
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
    private GamePanel game; //Don't generate here as controller could be generated in constructor.
    private UIPanel ui = new UIPanel(new GridLayout(3, 1, 0, 15));
    private Menu menu = new Menu(this);

    // Colours for the UI
    public static final Color BACKGROUND = new Color(47, 74, 58);
    public static final Color FOREGROUND = new Color(179, 178, 137);
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

    private Timer tick = new Timer(TICK_RATE, (unused) -> tick());

    /**
     * App()
     * Loads the default UI and starts the game loop.
     */
    public App(){
        assert SwingUtilities.isEventDispatchThread();
        game = new GamePanel(this);
        controller = new Controller();
        setupUI();
        startTick();
    }

    public App(Controller c) {
        assert SwingUtilities.isEventDispatchThread();
        game = new GamePanel(this);
        controller = c;
        setupUI();
        startTick();
    }

    /**
     * setupUI()
     * Sets up the base UI for the game inside a frame and displays.
     * Includes configs for UI and Game panel.
     */
    private void setupUI(){
        setupButtons();
        setupDisplay();
        add(game, BorderLayout.CENTER);
        add(ui, BorderLayout.EAST);
        setVisible(true);
        game.setVisible(true);
    }


    /**
     * startTick()
     * Starts the main update loop for the program. Packages Domain, Renderer and Recorder should be used here.
     */
    private void startTick(){
        domain = Persistency.loadGameBoard(2);
        initialDomain = Persistency.loadGameBoard(2);
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
     * setupButtons()
     * Sets up the buttons inside the UI panel.
     */
    private void setupButtons(){
        JPanel buttons = new JPanel(new GridLayout(2, 3, 5, 10));
        JButton undo = new JButton("Undo");     // On press, it should save the game state.
        JButton redo = new JButton("Redo");     // On press, it should open a window to load a saved file.
        JButton pause = new JButton("Pause/Resume");   // On press, it should pause and change to "Resume".
        JButton play = new JButton("Play");
        undo.addActionListener(recorder.undo());
        redo.addActionListener(recorder.redo());
        play.addActionListener(recorder.play());
        pause.addActionListener((unused) -> { pauseTimer(tick.isRunning()); });
        List.of(undo, redo, pause, play).forEach(i -> {
            i.setFont(new Font("Monospaced", Font.BOLD, 15));
            i.setForeground(FONT);
            buttons.add(i);
        });
        buttons.setBackground(FOREGROUND);
        ui.add(buttons);
    }

    /**
     * setupDisplay()
     * Sets up the time left to play, current level,
     * keys collected and number of treasures that need collecting.
     */
    private void setupDisplay(){
        JPanel elements = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel time = new JLabel("Time: ");
        JLabel level = new JLabel("Level: ");
        JLabel keys = new JLabel("Keys: ");
        JLabel keysToCollect = new JLabel("Keys to Collect: ");

        List.of(time, level, keys, keysToCollect).forEach(i -> {
            i.setFont(new Font("Monospaced", Font.BOLD, 18));
            i.setForeground(FONT);
            elements.add(i);
        });

        elements.setBackground(FOREGROUND);
        ui.add(elements);

        add(game, BorderLayout.CENTER);
        add(ui, BorderLayout.EAST);
        setJMenuBar(menu);
    }

    @Override
    public void updateGraphics(){
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
        String status = tick.isRunning() ? "PLAYING" : "PAUSED";
        setTitle("Larry Croft's Adventures " + status);
    }

    @Override
    public String openFile(){
        JFileChooser loader = new JFileChooser(new File(System.getProperty("user.dir")));
        if (loader.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return loader.getSelectedFile().getName();
        }
        return "";
    }

    @Override
    public String saveFile() {
        JFileChooser saver = new JFileChooser(new File(System.getProperty("user.dir")));
        if (saver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return saver.getSelectedFile().getName();
        }
        return "";
    }
}
