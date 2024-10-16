package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.GameTimer;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.UILabel;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Key;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Treasure;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * InformationPanel --- This is the section of the UI holding information about the game.
 * e.g Level, keys left, treasure left, time.
 *
 * @author Alan McIlwaine 300653905
 */
public class InformationPanel extends JPanel{
    public JLabel level = new UILabel("1", 34, 24);
    public JLabel time = new UILabel("0", 166, 24);
    public JLabel chips = new UILabel("0", 34, 125);
    public JLabel keys = new UILabel("0", 166, 125);
    private final List<JLabel> items = List.of(level, time, chips, keys);

    private final App app;

    /**
     * Constructor for creating the information panel. Will set to display.
     * @param app Application to obtain vars for information.
     */
    public InformationPanel(App app) {
        this.app = app;
        setPreferredSize(new Dimension(App.WIDTH/3, App.HEIGHT/3)); // 300x200
        setLayout(null);
        setOpaque(false);
        build();
    }

    /**
     * Builds the relevant UI for the information panel.
     */
    public void build() {
        items.forEach(this::add);
    }

    @Override
    protected void paintComponent(Graphics g) {
        GameState game = app.domain().getGameState();
        List<Item> treasure = app.domain().getGameState().player().getTreasure();
        level.setText(String.valueOf(game.level()));
        time.setText(String.valueOf((int) GameTimer.stageCountdown));
        chips.setText(String.valueOf(game.totalTreasure() - treasure.stream().filter(t -> t instanceof Treasure).count()));
        keys.setText(String.valueOf(app.domain().keysLeft()));
        super.paintComponent(g);
    }
}
