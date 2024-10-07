package nz.ac.wgtn.swen225.lc.render;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundSoundImplement {
    public void playMusic() {
        try {
            File wavFile = new File("SoundEffect/soundEffect.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);

            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music");
        }
    }
}
