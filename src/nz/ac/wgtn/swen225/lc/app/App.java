package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

import nz.ac.wgtn.swen225.lc.domain.DomainTest.Mock;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.render.ImageImplement;

/**
 * App --- Program to build the Application elements, and start the ticking process.
 *
 * @author Alan McIlwaine 300653905
 */
public class App extends JFrame implements AppInterface{
    // Window is made up of two main panels
    private GamePanel game = new GamePanel();
    private UIPanel ui = new UIPanel(new GridLayout(3, 1, 0, 15));

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
    public static Controller controller = new Controller(); // Create controller before others for menu screen.
    public Recorder recorder = Recorder.create(this); // Created earlier so UI can hook up buttons to recorder.
    public GameBoard domain;
    public ImageImplement render;

    /**
     * App()
     * Loads the default UI and starts the game loop.
     */
    App(){
        super("Larry Croft's Adventures");
        assert SwingUtilities.isEventDispatchThread();
        setupUI();
        startTick();
    }

    /**
     * setupUI()
     * Sets up the base UI for the game inside a frame and displays.
     * Includes configs for UI and Game panel.
     */
    private void setupUI(){
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game, BorderLayout.CENTER);
        add(ui, BorderLayout.EAST);
        setupButtons();
        setupDisplay();
        setVisible(true);
    }

    /**
     * startTick()
     * Starts the main update loop for the program. Packages Domain, Renderer and Recorder should be used here.
     */
    private void startTick(){
        domain = Persistency.loadGameBoard(Persistency.path + "level1.json");
        Objects.requireNonNull(domain);
        render = ImageImplement.getImageImplement(game);
        Objects.requireNonNull(render);
        recorder = Recorder.create(this);
        Objects.requireNonNull(recorder);
        controller = new Controller(); // Clears the current action;
        Timer tick = new Timer(TICK_RATE, (unused) -> tick());
        tick.start();
    }

    /**
     * tick()
     * Code inside tick() is called every 50ms. This is for updating player movement
     * at a separate tick rate so movement isn't sluggish.
     */
    public void tick(){
        recorder.tick(controller.currentCommand);
        giveInput(controller.currentCommand);
        updateGraphics();
    }

    /**
     * setupButtons()
     * Sets up the buttons inside the UI panel.
     */
    private void setupButtons(){
        JPanel buttons = new JPanel(new GridLayout(2, 3, 5, 10));
        JButton undo = new JButton("Undo");     // On press, it should save the game state.
        JButton redo = new JButton("Redo");     // On press, it should open a window to load a saved file.
        JButton pause = new JButton("Pause");   // On press, it should pause and change to "Resume".
        JButton help = new JButton("Help");     // On press, it should display a help screen.
        undo.addActionListener(recorder.undo());
        redo.addActionListener(recorder.redo());
        pause.addActionListener(recorder.pause());
        //help.addActionListener((unused) -> render.help());
        List.of(undo, redo, pause, help).forEach(i -> {
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

        Menu menu = new Menu(this);
        setJMenuBar(menu);
    }

    @Override
    public void updateGraphics(){
        render.drawImages(domain.getGameState());
    }

    @Override
    public void giveInput(Command input){
        domain.action(input.direction());
    }

    @Override
    public void initialStateRevert(){
        domain = Persistency.loadGameBoard(Persistency.path + "level" + domain.getGameState().level() + ".json");
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
