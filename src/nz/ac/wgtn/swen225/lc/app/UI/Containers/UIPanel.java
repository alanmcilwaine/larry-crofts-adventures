package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;

import javax.swing.*;
import java.awt.*;

/**
 * UIPanel --- JPanel extension which holds the UI elements to the right side of the screen.
 *
 * @author Alan McIlwaine 300653905
 */
public class UIPanel extends JPanel {
    private final App app;
    private final Image backgroundImage;

    public InformationPanel infoPanel;
    public RecorderPanel recorderPanel;
    public InventoryPanel inventoryPanel;

    /**
     *  Create a new JPanel with 300x600 size with the default layout manager.
     */
    public UIPanel(App a) {
        this.app = a;
        backgroundImage = new ImageIcon("BackgroundImage/GameUI.png").getImage();
        setPreferredSize(new Dimension(App.WIDTH/3, App.HEIGHT));     // 300x600
        buildUI();
    }

    private void buildUI(){
        infoPanel = new InformationPanel(app);
        recorderPanel = new RecorderPanel(app);
        inventoryPanel = new InventoryPanel(app);

        add(recorderPanel);
        add(infoPanel);
        add(inventoryPanel);
        repaint();
        revalidate();

        /*
        undo.addActionListener(app.recorder.undo());
        redo.addActionListener(app.recorder.redo());
        undo.addActionListener((unused) -> {
            undo.setEnabled(app.recorder.canUndo());
            redo.setEnabled(app.recorder.canRedo());
        });
        redo.addActionListener((unused) -> {
            redo.setEnabled(app.recorder.canRedo());
            undo.setEnabled(app.recorder.canUndo());
        });
        play.setEnabled(false);
        undo.setEnabled(app.recorder.canUndo());
        redo.setEnabled(app.recorder.canRedo());
        play.addActionListener(app.recorder.play());
        pause.addActionListener((unused) -> {
            app.pauseTimer(app.tick.isRunning());
            undo.setEnabled(!app.tick.isRunning());
            redo.setEnabled(!app.tick.isRunning());
            play.setEnabled(!app.tick.isRunning());
            pause.setText(app.tick.isRunning() ? "Pause" : "Resume");
        });

        List.of(undo, redo, pause, play).forEach(this::add);

        level.setBounds(233, 35, App.WIDTH / 14, App.HEIGHT / 12);
        time.setBounds(58, 276, App.WIDTH / 14, App.HEIGHT / 12);
        chips.setBounds(218, 276, App.WIDTH / 14, App.HEIGHT / 12);

        List.of(level, time, chips).forEach(l -> {
            l.setFont(new Font("Monospaced", Font.PLAIN, 20));
            l.setForeground(FOREGROUND);
            add(l);
        });

        JButton restart = new JButton("Restart");
        restart.addActionListener((unused) -> {
            app.tick.stop();
            app.loadLevel(app.domain.getGameState().level());
        });
        restart.setBounds(0, 0, 100, 50);
        add(restart);

         */
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        /*
        GameState game = app.domain.getGameState();
        List<Item> treasure = game.player().getTreasure();
        level.setText(String.valueOf(game.level()));
        time.setText(String.valueOf((int)App.time));
        chips.setText(String.valueOf(game.totalTreasure() - treasure.stream().filter(t -> t instanceof Treasure).count()));
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);

         */
    }
}
