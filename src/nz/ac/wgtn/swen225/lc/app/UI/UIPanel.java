package nz.ac.wgtn.swen225.lc.app.UI;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Treasure;
import nz.ac.wgtn.swen225.lc.render.Img;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * UIPanel --- JPanel extension which holds the UI elements to the right side of the screen.
 *
 * @author Alan McIlwaine 300653905
 */
public class UIPanel extends JPanel {
    public JLabel level = new JLabel("1");
    public JLabel time = new JLabel("0");
    public JLabel chips = new JLabel("0");
    public JButton pause = new JButton("Pause");
    private final Image backgroundImage;
    private final App app;

    public static final Color FOREGROUND = new Color(203, 219, 202);
    /**
     *  Create a new JPanel with 300x600 size with the default layout manager.
     */
    public UIPanel(App a) {
        this.app = a;
        backgroundImage = new ImageIcon("BackgroundImage/GameUI.png").getImage();
        setLayout(null); // Disable layout so we can position elements in absolute position
        setPreferredSize(new Dimension(App.WIDTH/3, App.HEIGHT));     // 300x600
        buildUI();
    }

    private void buildUI(){
        JButton undo = new JButton("Undo");
        JButton redo = new JButton("Redo");
        JButton play = new JButton("Play");

        undo.setBounds(30, 119, App.WIDTH / 14, App.HEIGHT / 12);
        redo.setBounds(205, 119, App.WIDTH / 14, App.HEIGHT / 12);
        pause.setBounds(105, 117, App.WIDTH / 10, App.HEIGHT / 15);
        play.setBounds(105, 160, App.WIDTH / 10, App.HEIGHT / 15);

        undo.addActionListener(app.recorder.undo());
        redo.addActionListener(app.recorder.redo());
        play.setEnabled(false);
        undo.setEnabled(false);
        redo.setEnabled(false);
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        GameState game = app.domain.getGameState();
        List<Item> treasure = game.player().getTreasure();
        level.setText(String.valueOf(game.level()));
        time.setText(String.valueOf((int)App.time));
        chips.setText(String.valueOf(game.totalTreasure() - treasure.stream().filter(t -> t instanceof Treasure).count()));

        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);

        IntStream.range(0, treasure.size())
                .boxed()
                .forEach(i -> {
                    g.drawImage(Img.resizeImage(Img.INSTANCE.getImgs(treasure.get(i).toString() + ".png"), 30, 30), 45 + (i * 60), 425, this);
                });
    }
}
