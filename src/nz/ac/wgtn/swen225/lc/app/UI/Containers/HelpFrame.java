package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.UI.Widgets.SquareButton;

import javax.swing.*;
import java.awt.*;

/**
 * HelpFrame --- Builds the Help screen to show instructions for the player.
 *
 * @author Alan McIlwaine 300653905
 */
public class HelpFrame extends JFrame{
    private final Image helpImage;
    public HelpFrame() {
        helpImage = new ImageIcon("BackgroundImage/help.png").getImage();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(App.WIDTH, App.HEIGHT));
        setLayout(null);
        create();
        setVisible(true);
    }

    private void create() {
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(helpImage, 0, 0, this);
            }
        };
        JButton exit = new SquareButton("Close Help", App.WIDTH/2 - 75, App.HEIGHT / 5 * 4);
        exit.setBounds(exit.getX(), exit.getY(), 150, exit.getHeight());
        exit.addActionListener((unused) -> {
            setVisible(false);
        });
        panel.setSize(new Dimension(App.WIDTH, App.HEIGHT));
        add(panel);
        add(exit);
        panel.setVisible(true);
        revalidate();
        repaint();
    }
}
