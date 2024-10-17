package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import javax.swing.*;

import nz.ac.wgtn.swen225.lc.app.App;

import java.awt.*;

/**
 * AppFrame --- The frame encompasing the entire game.
 *
 * @author Alan McIlwaine 300653905
 */
public class AppFrame extends JFrame{
    // Window Dimensions
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    public static final Color BACKGROUND = new Color(16, 13, 25);
    public static final Color FOREGROUND = new Color(225, 211, 174);
    public static final Color BACKGROUND_2 = new Color(105, 88, 92);

    /**
     * Constructor to build the settings for the Container for the entire game.
     */
    public AppFrame(){
        super("Larry Crofts Adventures");
        setSize(App.WIDTH, App.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }
}
