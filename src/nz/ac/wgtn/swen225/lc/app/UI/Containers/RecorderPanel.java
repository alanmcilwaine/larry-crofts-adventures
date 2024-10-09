package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.Icons;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.SquareButton;
import nz.ac.wgtn.swen225.lc.app.UI.UIElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecorderPanel extends JPanel implements UIElement {
    private final App app;
    private final JSlider playbackSpeed = new JSlider(JSlider.HORIZONTAL, 1, 45, 1);
    private final JButton undo = new SquareButton(Icons.Undo.icon(), 75, 8);
    private final JButton redo = new SquareButton(Icons.Redo.icon(), 175, 8);
    private final JButton pause = new SquareButton(Icons.Pause.icon(), 125,8);
    private final JButton play = new SquareButton("Play", 0, 60);
    private final JButton restart = new SquareButton("Restart", 0, 8);
    private final List<JButton> buttons = List.of(undo, redo, pause, play, restart);

    public RecorderPanel(App app) {
        this.app = app;
        setPreferredSize(new Dimension(App.WIDTH/3, App.HEIGHT / 4));
        setLayout(null);
        build();
        setOpaque(false);
        setVisible(true);
    }

    @Override
    public void build() {
        pause.addActionListener((unused) -> app.pauseTimer(app.tick.isRunning()));
        undo.addActionListener(app.recorder.undo());
        redo.addActionListener(app.recorder.redo());
        play.addActionListener(app.recorder.play());
        restart.addActionListener((unused) -> {
            app.tick.stop();
            app.loadLevel(app.domain.getGameState().level());
        });
        buildSlider();
        buttons.forEach(this::add);
        add(playbackSpeed);
        revalidate();
        repaint();
    }

    private void buildSlider() {
        playbackSpeed.addChangeListener((e) -> {
            JSlider slider = (JSlider)e.getSource();
            if (!slider.getValueIsAdjusting()) {
                app.recorder.setPlaybackSpeed(App.TICK_RATE + (-1 * slider.getValue()));
            }
        });
        playbackSpeed.setBounds(play.getX() + 50, play.getY(), App.WIDTH/3 - play.getWidth(), play.getHeight());
        playbackSpeed.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playbackSpeed.setBackground(Color.GRAY);
        playbackSpeed.setOpaque(true);
        playbackSpeed.setFocusable(false);
        playbackSpeed.setBackground(new Color(105, 88, 92));
    }

    public void updateButtons(boolean isRunning) {
        undo.setVisible(!isRunning && app.recorder.canUndo());
        redo.setVisible(!isRunning && app.recorder.canRedo());
        play.setVisible(!isRunning && app.recorder.canRedo());
        playbackSpeed.setVisible(!isRunning && app.recorder.canRedo());
        pause.setIcon(app.tick.isRunning() ? Icons.Pause.icon() : Icons.Resume.icon());
    }

    @Override
    protected void paintComponent(Graphics g) {
        updateButtons(app.tick.isRunning());
        super.paintComponent(g);
    }
}
