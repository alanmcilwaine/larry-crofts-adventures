package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.Events.Death;
import nz.ac.wgtn.swen225.lc.app.Events.GameEvent;
import nz.ac.wgtn.swen225.lc.app.Events.NextLevel;
import nz.ac.wgtn.swen225.lc.app.Events.OutOfTime;
import nz.ac.wgtn.swen225.lc.app.Inputs.Command;
import nz.ac.wgtn.swen225.lc.app.Inputs.Controller;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.AppFrame;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.GamePanel;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.Menu;
import nz.ac.wgtn.swen225.lc.app.UI.Containers.UIPanel;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;
import nz.ac.wgtn.swen225.lc.render.BackgroundSoundImplement;
import nz.ac.wgtn.swen225.lc.render.ImageImplement;
import nz.ac.wgtn.swen225.lc.render.InfoImplement;

/**
 * App --- Program to build the Application elements, and start the ticking process.
 *
 * @author Alan McIlwaine 300653905
 */
public class App extends AppFrame implements AppInterface{
    // Window is made up of two main panels and a menu.
    private final GamePanel game;
    private final UIPanel ui;
    private final Menu menu = new Menu(this);

    // Game Information
    private final Controller controller;
    private final Recorder recorder = Recorder.create(this); // Created earlier so UI can hook up buttons to recorder.
    private final GameTimer timer = new GameTimer(this::tick);
    private final List<GameEvent> eventsOnTick = List.of(new NextLevel(), new Death(), new OutOfTime());
    private final GameLoader gameLoader = new GameLoader(this);
    private GameBoard domain;
    private GameBoard initialDomain;
    private ImageImplement render;

    /**
     * App()
     * Loads the game with the default key controller.
     * Note that we can't call this(Controller) because Controller depends on App.
     */
    public App(){
        assert SwingUtilities.isEventDispatchThread();
        controller = new Controller(this);  // Now controller is initialized
        game = makePanel();
        ui = new UIPanel(this);
        initialise();
        startTick(gameLoader.loadSave());
    }

    /**
     * Controls the start of the game. Creates the UI and starts the game.
     * @param c Controller for user inputs
     */
    public App(Controller c) {
        assert SwingUtilities.isEventDispatchThread();
        controller = c; // Must be before game as game depends on the controller.
        game = makePanel();
        ui = new UIPanel(this);
        initialise();
        startTick(gameLoader.loadSave());
    }

    /**
     * Sets up the base UI for the game inside a frame and displays.
     * Includes configs for UI and Game panel.
     */
    private void initialise(){
        add(game, BorderLayout.CENTER);
        add(ui, BorderLayout.EAST);
        setJMenuBar(menu);
        setVisible(true);
        game.setVisible(true);
    }

    /**
     * Builds the game panel, this is here so users can override the paintComponent
     * to not draw the game.
     */
    public GamePanel makePanel() {
        return new GamePanel(this);
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
            controller.movementWaitTime = GameTimer.INPUT_WAIT;
        }
        controller.movementWaitTime -= GameTimer.TICK_RATE;
        return input;
    }

    /**
     * Starts the main update loop for the program.
     * Packages Domain, Renderer and Recorder should be used here.
     */
    public void startTick(GameBoard b){
        if (render != null) {
            InfoImplement.unvisiableTextArea(); // Set info areas to invisible. Or else still visible in memory.
        }
        render = ImageImplement.getImageImplement(game);
        domain = b;
        initialDomain = Persistency.loadGameBoard(domain().getGameState().level());
        GameTimer.stageCountdown = domain.getGameState().timeLeft();
        game.requestFocusInWindow();
        timer.start();
    }

    /**
     * tick()
     * Code inside tick() is called every TICK_RATE. This is what ticks the rest of the game.
     */
    public void tick(){
        Command input = chooseInput();
        if (!input.equals(Command.None)) {
            recorder.takeControl(); // Remove the redo if we are moving.
        }
        recorder.tick(input);
        giveInput(input);
        updateGraphics();
        eventsOnTick.forEach(e -> e.check(this));
    }

    /**
     * Saves a list of inputs to a JSON file.
     * @param commands List of inputs.
     * @param level What level the inputs were made on.
     */
    public static void saveInputs(List<Command> commands, int level) {
        Persistency.saveCommands(commands, level);
    }

    /**
     * Mutes the game given the state.
     * @param state True to mute, false to unmute.
     */
    public static void muteGame(boolean state) {
        BackgroundSoundImplement.muteMusic(state);
    }

    /*
    OVERRIDES
     */
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
            timer.stop();
        } else {
            timer.start();
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

    /**
     * GETTERS
     */
    public UIPanel ui() {
        return ui;
    }
    public GamePanel game() {
        return game;
    }
    public GameBoard domain(){
        return domain;
    }
    public GameBoard initialDomain(){
        return initialDomain;
    }
    public Controller controller() {
        return controller;
    }
    public Recorder recorder() {
        return recorder;
    }
    public GameTimer timer() {
        return timer;
    }
    public ImageImplement render() {
        return render;
    }
    public GameLoader gameLoader() {
        return gameLoader;
    }

    /**
     * SETTERS
     */
    public void domain(GameBoard domain) {
        assert domain != null;
        this.domain = domain;
    }
    public void initialDomain(GameBoard domain) {
        assert domain != null;
        this.initialDomain = domain;
    }
}