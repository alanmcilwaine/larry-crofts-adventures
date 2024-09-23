package nz.ac.wgtn.swen225.lc.render;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundImplement {
    private BufferedImage backgroundImage;

    BackgroundImplement(){
        try{
            backgroundImage = ImageIO.read(new File("BackgroundImage/backgroundImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawBackGround(JPanel jPanel){
        Graphics g = jPanel.getGraphics();
        g.drawImage(backgroundImage, 0, 0, jPanel.getWidth(), jPanel.getHeight(), jPanel);
    }
}
