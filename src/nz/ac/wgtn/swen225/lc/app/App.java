package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.UI.Containers.AppFrame;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.GamePanel;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.Menu;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.UIPanel;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
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
    public UIPanel ui;
    private Menu menu = new Menu(this);

    // Window Dimensions
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    // Tick rate
    public static final int TICK_RATE = 50;
    public static double time;

    // Game Information
    public static Controller controller;
    public Recorder recorder = Recorder.create(this); // Created earlier so UI can hook up buttons to recorder.
    public GameBoard domain;
    public GameBoard initialDomain;
    public ImageImplement render;

    public GameTimer tick = new GameTimer(this::tick);

    /**
     * App()
     * Loads the default UI and starts the game loop.
     */
    public App(){
        assert SwingUtilities.isEventDispatchThread();
        controller = new Controller(this);
        game = makePanel();
        ui = new UIPanel(this);
        setupUI();
        startTick();
    }

    /**
     * Lets the FUZZ make their own controller.
     * @param c Controller for user inputs
     */
    public App(Controller c) {
        assert SwingUtilities.isEventDispatchThread();
        controller = c;
        game = makePanel();
        ui = new UIPanel(this);
        setupUI();
        startTick();
    }

    /**
     * makeGame()
     * Builds the game panel, this is here so users can override the paintComponent
     */
    public GamePanel makePanel() {
        return new GamePanel(this);
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
        time = domain.getGameState().timeLeft();
        tick.start();
    }

    /**
     * Checks if the player is on the next level tile. If it is, we will pause the game
     * and update domain to the next level.
     */
    private void checkNextLevel() {
        Player player = domain.getGameState().player();
        if (player.isNextLevel()) {
            tick.onExitTile(() -> loadLevel(domain.getGameState().level() + 1));
        }
    }

    /**
     * Checks if the player has died. If it has, we respawn at the new level.
     */
    private void checkDeath() {
        Player player = domain.getGameState().player();
        if (player.isDead() || time <= 0) {
            tick.onDeath(() -> loadLevel(domain.getGameState().level()));
        }
    }

    /**
     * Chooses the input to send to the game. We do this because an input is chosen
     * every few ticks instead of every tick. Without this, holding down inputs will
     * blast the player across the screen.
     *
     * @return Command as an input
     */
    private Command chooseInput() {
        Command input = Command.None;
        if (controller.movementWaitTime <= 0 && controller.currentCommand() != Command.None) {
            input = controller.currentCommand();
            controller.movementWaitTime = Keys.INPUT_WAIT;
        }
        controller.movementWaitTime -= TICK_RATE;
        return input;
    }

    /**
     * tick()
     * Code inside tick() is called every 50ms. This is what ticks the rest of the game.
     */
    public void tick(){
        time -= ((double) TICK_RATE / 1000);

        Command input = chooseInput();
        recorder.tick(input);
        giveInput(input);
        updateGraphics();

        checkNextLevel();
        checkDeath();
    }

    /**
     * Goes to the next level in the game.
     * @param level The level we go to.
     */
    public void loadLevel(int level) {
        domain = Persistency.loadGameBoard(level);
        initialDomain = domain.copyOf();
        recorder.setCommands(List.of());
        time = domain.getGameState().timeLeft();
        game.requestFocusInWindow();
        tick.start();
    }

    /**
     * Saves a list of inputs to a JSON file.
     * @param commands List of inputs.
     * @param level What level the inputs were made on.
     */
    public static void saveInputs(List<Command> commands, int level) {
        Persistency.saveCommands(commands, level);
    }

    // OVERRIDES-------------------------

    @Override
    public void updateGraphics(){
        ui.repaint();
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
    }

    @Override
    public String openFile(){
        JFileChooser loader = new JFileChooser(new File(System.getProperty("user.dir") + "/src/nz/ac/wgtn/swen225/lc/persistency/levels"));
        if (loader.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return loader.getSelectedFile().getPath();
        }
        return "";
    }

    @Override
    public String saveFile() {
        JFileChooser saver = new JFileChooser(new File(System.getProperty("user.dir") + "/src/nz/ac/wgtn/swen225/lc/persistency/levels"));
        if (saver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return saver.getSelectedFile().getPath();
        }
        return "";
    }

}
