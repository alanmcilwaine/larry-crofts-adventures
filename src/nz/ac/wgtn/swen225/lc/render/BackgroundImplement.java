package nz.ac.wgtn.swen225.lc.render;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundImplement {
    private ImageIcon gifImage;

    public BackgroundImplement() {

        gifImage = new ImageIcon(getClass().getResource("/BackgroundImage/bc.gif"));
    }


    public void drawBackGround(JPanel jPanel, Graphics g) {

        g.drawImage(gifImage.getImage(), 0, 0, jPanel.getWidth(), jPanel.getHeight(), jPanel);

    }
}
