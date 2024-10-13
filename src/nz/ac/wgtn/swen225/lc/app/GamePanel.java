package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;

/**
 * GamePanel --- JPanel extension which holds the Game elements on the left side of the screen.
 *
 * @author Alan McIlwaine 300653905
 */
public class GamePanel extends JPanel{
    private final App app;

    /**
     *  Create a new JPanel with 600x600 size with the default layout manager.
     */
    public GamePanel(App a) {
        setPreferredSize(new Dimension(App.WIDTH/3*2, App.HEIGHT)); // 600x600
        setBackground(Color.BLACK);
        setFocusable(true);                // Without this keyListener won't work
        addKeyListener(App.controller);
        this.app = a;
    }

    /**
     * Calls to Renderer. Overriding to stop flickering when rendering.
     * https://stackoverflow.com/questions/54814269/strange-jpanel-background-glitching
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        app.ui.repaint();
        app.render.drawImages(app.domain.getGameState(), g);
    }
}
