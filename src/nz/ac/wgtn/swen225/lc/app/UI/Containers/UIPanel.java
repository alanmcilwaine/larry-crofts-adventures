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
        build();
    }

    /**
     * Builds the relevant UI for the UI Panel.
     */
    private void build(){
        infoPanel = new InformationPanel(app);
        recorderPanel = new RecorderPanel(app);
        inventoryPanel = new InventoryPanel(app);
        add(recorderPanel);
        add(infoPanel);
        add(inventoryPanel);
        repaint();
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
