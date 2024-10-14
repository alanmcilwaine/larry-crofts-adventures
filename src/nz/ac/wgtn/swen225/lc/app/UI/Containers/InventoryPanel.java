package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.render.Img;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;
import java.util.List;

/**
 * InventoryPanel --- This is the section of the UI dedicated to the inventory.
 *
 * @author Alan McIlwaine 300653905
 */
public class InventoryPanel extends JPanel {
    private App app;

    /**
     * Constructor for creating the Inventory panel. Will set to display.
     * @param app Application to obtain vars for information.
     */
    public InventoryPanel(App app) {
        this.app = app;
        setPreferredSize(new Dimension(App.WIDTH/3, App.HEIGHT/3)); // 300x200
        setLayout(null);
        setOpaque(false);
        setVisible(true);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<Item> treasure = app.domain().getGameState().player().getTreasure();
        IntStream.range(0, treasure.size())
                .boxed()
                .forEach(i -> {
                    g.drawImage(
                            Img.resizeImage(Img.INSTANCE.getImgs(treasure.get(i).toString() + ".png"), 30, 30),
                            45 + ((i % 4) * 60),
                            60 + ((i / 4) * 67),
                            this
                    );
                });
    }
}
