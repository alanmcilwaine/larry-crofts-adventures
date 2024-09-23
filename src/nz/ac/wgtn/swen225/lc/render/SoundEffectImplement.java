package nz.ac.wgtn.swen225.lc.render;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffectImplement {
    public void playMusic() {
        try {
            File wavFile = new File("SoundEffect/soundEffect.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);

            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);

            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.setFramePosition(0);
                    clip.start();
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music");
        }
    }
}
