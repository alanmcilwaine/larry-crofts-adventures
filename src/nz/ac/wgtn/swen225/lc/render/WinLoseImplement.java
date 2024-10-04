package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WinLoseImplement {
    private Image image;

    public WinLoseImplement() throws IOException {
        image = ImageIO.read(new File("BackgroundImage/warning.png"));
    }

    public static void drawWinLose(GameState gameState, Graphics g) {

        g.setFont(new Font("Georgia", Font.BOLD, 40));
        if(gameState.player().isNextLevel()){
            g.setColor(Color.red);
            g.drawString("Victory!! You win!!!", 20, 70);
        }
        if(gameState.player().isDead()) {
            g.drawString("Ooooopsssss, try again!!!", 0, 50);
        }

    }
}
