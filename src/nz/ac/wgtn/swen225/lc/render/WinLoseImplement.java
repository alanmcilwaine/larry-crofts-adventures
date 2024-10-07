package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameState;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WinLoseImplement {
    static Clip clip;
    private static boolean winMusicPlayed = false;
    private static boolean loseMusicPlayed = false;

    public void drawWinLose(GameState gameState, Graphics g, JPanel jpanel) {


        g.setFont(new Font("Brush Script MT", Font.BOLD, 60));
        if (gameState.player().isNextLevel()) {

            g.setColor(Color.YELLOW);
            g.drawString("Victory!! You win!!!", 30, 72);

            g.setColor(Color.RED);
            g.drawString("Victory!! You win!!!", 28, 70);
        }
        if (gameState.player().isDead()) {

            g.setColor(Color.BLACK);
            g.drawString("Ooooops, try again!!!", 16, 52);
            g.setColor(Color.RED);
            g.drawString("Ooooops, try again!!!", 8, 50);

        }
        jpanel.repaint();

    }

    public static void playMusic(String name) {

        try {
            File wavFile = new File("SoundEffect/" + name + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music");
        }
    }

    public void allMusicPlayed(GameState gameState) {
        // Play win music only once when the player wins
        if (gameState.player().isNextLevel() && !winMusicPlayed) {
            WinLoseImplement.playMusic("win");
            winMusicPlayed = true;
            loseMusicPlayed = false;
        }

        // Play lose music only once when the player dies
        if (gameState.player().isDead() && !loseMusicPlayed) {
            WinLoseImplement.playMusic("lose");
            loseMusicPlayed = true;
            winMusicPlayed = false; // Reset the win flag for future wins
        }

        // Reset flags if the game continues without win/loss
        if (!gameState.player().isDead() && !gameState.player().isNextLevel()) {
            winMusicPlayed = false;
            loseMusicPlayed = false;
        }
    }


}
