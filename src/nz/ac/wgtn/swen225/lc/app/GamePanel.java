package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * UIPanel --- JPanel extension which holds the Game elements on the left side of the screen.
 *
 * @author Alan McIlwaine 300653905
 */
public class GamePanel extends JPanel{
    /**
     *  Create a new JPanel with 600x600 size with the default layout manager.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(App.WIDTH/3*2, App.HEIGHT)); // 600x600
        setBackground(Color.BLACK);
        setFocusable(true);                // Without this keyListener won't work
        addKeyListener((KeyListener) App.controller);
    }
}
