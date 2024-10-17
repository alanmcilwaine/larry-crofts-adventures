package nz.ac.wgtn.swen225.lc.render;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

/**
 * Handles loading and rendering a GIF background image from the "BackgroundImage" folder.
 *
 * @author libaix 300641237
 * @version 2.5
 */
public class BackgroundImplement {
    private Image gifImage = null;

    /**
     * Safely loads the GIF image found in the "BackgroundImage" folder.
     *
     * @throws Error if loading the image fails.
     */
    public void backgroundImplement() {
        File backgroundFolder = new File("BackgroundImage");
        File[] imageFiles = backgroundFolder.listFiles();
        if(imageFiles == null) {throw new NullPointerException("The background image folder does not exist");}

        // filter the gif and convert it to ImageIcon
        gifImage = new ImageIcon(Arrays.stream(imageFiles)
                .filter(f -> f.getName().endsWith(".gif"))
                .findFirst().orElseThrow(() -> new Error("Failed to load the background image"))
                .getAbsolutePath()).getImage();
    }

    /**
     * Draws the loaded GIF image onto the given JPanel.
     *
     * @param jPanel the target JPanel.
     * @param g the Graphics object for drawing.
     */
    public void drawBackGround(JPanel jPanel, Graphics g) {
        g.drawImage(gifImage, 0, 0, jPanel.getWidth(), jPanel.getHeight(), jPanel);
    }
}
