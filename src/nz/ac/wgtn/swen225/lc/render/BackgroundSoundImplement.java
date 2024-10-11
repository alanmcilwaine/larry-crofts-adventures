package nz.ac.wgtn.swen225.lc.render;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * handles playing background sound effects using .wav files.
 */
public class BackgroundSoundImplement {
    private static Clip clip;
    public static String status = "soundEffect";
    // load the different sounds based on different status
    private static Supplier<String> statusSupplier = () -> status;

    /**
     * Plays the background music or sound effect based on the current status.
     *
     * @throws RuntimeException if the audio file cannot be loaded or played.
     */
    public static void playMusic() {
        try {
            File wavFile = new File("SoundEffect/" + statusSupplier.get() + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);

            clip = AudioSystem.getClip();

            clip.open(audioStream);

            // non-stopping playing
            //clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music for the status: " + statusSupplier.get());
        }
    }
}
