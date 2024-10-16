package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameState;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * responsible for handling the display of win/lose messages and
 * playing corresponding sound effects when the player either wins or loses the game.
 * It tracks whether the win/lose music has already been played to avoid repetition.
 *
 * @author libaix 300641237
 * @version 2.5
 */
public class WinLoseImplement {
    static Clip clip;
    private static boolean winMusicPlayed = false;
    private static boolean loseMusicPlayed = false;

    /**
     * Draws the win or lose message on the game screen
     *
     * @param gameState the current state of the game.
     * @param g the Graphics object used to draw the message.
     * @param jpanel the JPanel where the message will be drawn.
     */
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

    /**
     * Plays the music based on the provided sound file name.
     *
     * @param name the name of the sound file to play, without the file extension.
     * @throws RuntimeException if the music file cannot be found or played.
     */
    public static void playMusic(String name) {
        if(BackgroundSoundImplement.getMuted()) return;
        try {
            File wavFile = new File("SoundEffect/" + name + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music for status: " + name);
        }
    }

    /**
     * Plays the appropriate music based on the player's state (win or lose).
     * It ensures that the win or lose music is only played once during the relevant event.
     * Also, resets the win/lose flags when the game continues without a win or loss.
     *
     * @param gameState the current state of the game.
     */
    public static void allMusicPlayed(GameState gameState) {
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

    /** mute and unmute the win/lose music
     * @param mute to decide whether mute the win/lose music
     */
    public static void mute(boolean mute){
        if(clip != null && mute){
            clip.stop();
        }

    }
}
