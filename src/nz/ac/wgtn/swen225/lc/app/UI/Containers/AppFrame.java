package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import javax.swing.*;

import nz.ac.wgtn.swen225.lc.app.App;

/**
 * AppFrame --- The frame encompasing the entire game.
 *
 * @author Alan McIlwaine 300653905
 */
public class AppFrame extends JFrame{
    // Window Dimensions
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    public AppFrame(){
        super("Larry Crofts Adventures");
        setSize(App.WIDTH, App.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }
}
