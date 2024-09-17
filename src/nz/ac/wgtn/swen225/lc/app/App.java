package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import nz.ac.wgtn.swen225.lc.persistency.*;

public class App extends JFrame{
    // Window is made up of two main panels
    private JPanel game = new JPanel();
    private JPanel ui = new JPanel(new GridLayout(3, 1, 0, 15));

    // Colours for the UI
    private final Color BACKGROUND = new Color(47, 74, 58);
    private final Color FOREGROUND = new Color(179, 178, 137);
    private final Color FONT = new Color(31, 30, 25);

    // Window Dimensions
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    // Tick rate
    public static final int GAME_TICK_RATE = 500;
    public static final int LOGIC_TICK_RATE = 50;

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
     * startTick()
     * Starts the main update loop for the program. Packages Domain, Renderer and Recorder should be used here.
     */
    private void startTick(){
        Timer gameTick = new Timer(GAME_TICK_RATE, (unused) -> gameTick());
        Timer logicTick = new Timer(LOGIC_TICK_RATE, (unused) -> logicTick());
        gameTick.start();
        logicTick.start();
    }

    /**
     * gameTick()
     * Code inside gameTick() is called every 500ms. This is the main update loop
     * for moving enemies in the game and handling things from domain.
     */
    public void gameTick(){
    }

    /**
     * logicTick()
     * Code inside logicTick() is called every 50ms. This is for updating player movement
     * at a separate tick rate so movement isn't sluggish.
     */
    public void logicTick(){
    }

    /**
     * updateGraphics()
     * Sends an update request to graphics to update the graphics. Used after updating state in domain.
     */
    public void updateGraphics(){
        // renderer.update();
    }

    /**
     * giveInput()
     * Takes in an input, and sends to the domain to update state.
     * @param input An input in the game, e.g WASD as a command.
     */
    public void giveInput(Command input){

    }

    /**
     * initialStateRevert()
     * Tells domain to revert to the starting state of the game. Like a reset.
     */
    public void initialStateRevert(){

    }

    /**
     * persistency()
     * Gives the object for keeping track of persistency.
     * @return
     */
    public String persistency(){ // TODO change String to Persistency, unable to import Persistency right now.
        return "";
    }


    /**
     * setupUI()
     * Sets up the base UI for the game inside a frame and displays.
     * Includes configs for UI and Game panel.
     */
    private void setupUI(){
        // Frame config
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel config
        game.setPreferredSize(new Dimension(WIDTH/3*2, HEIGHT)); // 600x600
        ui.setPreferredSize(new Dimension(WIDTH/3, HEIGHT));     // 300x600
        game.setBackground(Color.BLACK);
        ui.setBackground(BACKGROUND);
        game.setFocusable(true);                // Without this keyListener won't work
        // game.addKeyListener(keyInvoker);     // TODO: Key inputs
        add(game, BorderLayout.CENTER);
        add(ui, BorderLayout.EAST);

        setupButtons();
        setupDisplay();
        setVisible(true);
    }

    /**
     * setupButtons()
     * Sets up the buttons inside the UI panel.
     */
    private void setupButtons(){
        JPanel buttons = new JPanel(new GridLayout(2, 3, 5, 10));
        JButton pause = new JButton("Pause");   // On press, it should pause and change to "Resume".
        JButton exit = new JButton("Exit");     // On press, it should exit the game, without saving?.
        JButton save = new JButton("Save");     // On press, it should save the game state.
        JButton load = new JButton("Load");     // On press, it should open a window to load a saved file.
        JButton help = new JButton("Help");     // On press, it should display a help screen.

        /* Uncomment for testing
        pause.addActionListener(() -> );
        exit.addActionListener(() -> );
        save.addActionListener(() -> );
        load.addActionListener(() -> );
        help.addActionListener(() -> );
         */

        List.of(pause, exit, save, load, help).forEach(i -> {
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
    }
}
