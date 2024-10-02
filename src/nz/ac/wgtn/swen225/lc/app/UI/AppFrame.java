package nz.ac.wgtn.swen225.lc.app.UI;

import javax.swing.*;
import java.awt.*;
import nz.ac.wgtn.swen225.lc.app.App;

public class AppFrame extends JFrame{
    public AppFrame(){
        super("Larry Crofts Adventures");
        setSize(App.WIDTH, App.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }
}
