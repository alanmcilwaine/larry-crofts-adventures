package nz.ac.wgtn.swen225.lc.render;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class BackgroundImplement {
    private Image gifImage;

    public BackgroundImplement() {

        File backgroundFolder = new File("BackgroundImage");
        File[] imageFiles = backgroundFolder.listFiles();
        assert imageFiles != null;
        for(File file: imageFiles){
            try{
                if(file.getName().endsWith(".gif")) {
                    ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
                    gifImage = imageIcon.getImage();
                }
            }
            catch (RuntimeException e){
                throw new Error("Failed to load image: " + file.getName());
            }

        }
    }


    public void drawBackGround(JPanel jPanel, Graphics g) {

        g.drawImage(gifImage, 0, 0, jPanel.getWidth(), jPanel.getHeight(), jPanel);

    }
}
