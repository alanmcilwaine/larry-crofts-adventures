package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WinLoseImplement {

    public static void drawWinLose(GameState gameState, Graphics g, JPanel jpanel) {


        g.setFont(new Font("Brush Script MT", Font.BOLD, 60));
        if(gameState.player().isNextLevel()){
            g.setColor(Color.YELLOW);
            g.drawString("Victory!! You win!!!", 30, 72);

            g.setColor(Color.RED);
            g.drawString("Victory!! You win!!!", 28, 70);
        }
        if(gameState.player().isDead()) {
            g.setColor(Color.BLACK);
            g.drawString("Ooooops, try again!!!", 16, 52);
            g.setColor(Color.RED);
            g.drawString("Ooooops, try again!!!", 8, 50);
        }
        jpanel.repaint();

    }
}
