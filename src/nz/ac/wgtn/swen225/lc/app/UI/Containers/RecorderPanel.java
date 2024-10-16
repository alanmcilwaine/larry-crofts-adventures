package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.Icons;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.SquareButton;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.UILabel;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * RecorderPanel --- This is the section of the UI dedicated to the recorder. It handles the pressing of recorder buttons.
 *
 * @author Alan McIlwaine 300653905
 */
public class RecorderPanel extends JPanel{
    private final App app;
    private final JSlider playbackSpeed = new JSlider(JSlider.HORIZONTAL, 1, 45, 1);
    private final JButton undo = new SquareButton(Icons.Undo.icon(), 75, 8);
    private final JButton redo = new SquareButton(Icons.Redo.icon(), 175, 8);
    private final JButton pause = new SquareButton(Icons.Pause.icon(), 125,8);
    private final JButton play = new SquareButton("Play", 0, 60);
    private final JButton restart = new SquareButton("Restart", 0, 8);
    private final JButton mute = new SquareButton("Unmute", 230, 8);
    private final List<JButton> buttons = List.of(undo, redo, pause, play, restart,mute);
    public static JLabel label = new UILabel("Saved Game", 100, 105);

    // State of music playing
    private boolean isMuted = true;

    /**
     * Constructor for creating the Recorder panel. Will set to display.
     * @param app Application to obtain vars for information.
     */
    public RecorderPanel(App app) {
        this.app = app;
        setPreferredSize(new Dimension(App.WIDTH/3, App.HEIGHT / 4));
        setLayout(null);
        build();
        setOpaque(false);
        setVisible(true);
    }

    /**
     * Builds the relevant UI for the recorder panel.
     */
    public void build() {
        pause.addActionListener((unused) -> {
            app.recorder().pause();
            app.pauseTimer(app.timer().isRunning());
            RecorderPanel.label.setText("Pausing Game");
        });
        undo.addActionListener(app.recorder().undo());
        redo.addActionListener(app.recorder().redo());
        play.addActionListener(app.recorder().play());
        play.addActionListener((unused) -> RecorderPanel.label.setText("Playing..."));
        restart.addActionListener((unused) -> {
            app.timer().stop();
            app.gameLoader().loadLevel(app.domain().getGameState().level());
            RecorderPanel.label.setText("Restarting Game");
        });
        mute.setBounds(mute.getX(), mute.getY(), mute.getWidth() + 20, mute.getHeight());
        mute.addActionListener((unused) -> {
            isMuted = !isMuted;
            App.muteGame(isMuted);
            mute.setText(isMuted ? "Unmute" : "Mute");
        });
        buildSlider();
        buttons.forEach(this::add);
        add(playbackSpeed);
        label.setBounds(label.getX() - 50, label.getY(), label.getWidth() + 100, label.getHeight());
        label.setVisible(false);
        add(label);
        revalidate();
        repaint();
    }

    /**
     * Constructs the slider and all its settings. Could be moved to a class if used in
     * multiple locations.
     */
    private void buildSlider() {
        playbackSpeed.addChangeListener((e) -> {
            JSlider slider = (JSlider)e.getSource();
            if (!slider.getValueIsAdjusting()) {
                app.recorder().setPlaybackSpeed(App.TICK_RATE + (-1 * slider.getValue()));
            }
        });
        playbackSpeed.setBounds(play.getX() + 50, play.getY(), App.WIDTH/3 - play.getWidth(), play.getHeight());
        playbackSpeed.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playbackSpeed.setBackground(Color.GRAY);
        playbackSpeed.setOpaque(true);
        playbackSpeed.setFocusable(false);
        playbackSpeed.setBackground(new Color(105, 88, 92));
    }

    /**
     * Updates the visibility of the buttons in the UI. Will show if able to press.
     * @param isRunning True if the game timer is running. Otherwise, false.
     */
    public void updateButtons(boolean isRunning) {
        Recorder recorder = app.recorder();
        undo.setVisible(!isRunning && recorder.canUndo());
        redo.setVisible(!isRunning && recorder.canRedo());
        play.setVisible(!isRunning && recorder.canRedo());
        playbackSpeed.setVisible(!isRunning && recorder.canRedo());
        pause.setIcon(app.timer().isRunning() ? Icons.Pause.icon() : Icons.Resume.icon());
    }

    @Override
    protected void paintComponent(Graphics g) {
        updateButtons(app.timer().isRunning());
        super.paintComponent(g);
    }
}
