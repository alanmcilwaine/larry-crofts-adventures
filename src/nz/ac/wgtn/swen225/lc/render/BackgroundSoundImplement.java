package nz.ac.wgtn.swen225.lc.render;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class BackgroundSoundImplement {
    private static Clip clip;
    public static String status = "soundEffect";
    private static Supplier<String> statusSupplier = () -> status;
    public static void playMusic() {
        try {
            System.out.println(status);
            File wavFile = new File("SoundEffect/" + statusSupplier.get() + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);

            clip = AudioSystem.getClip();

            clip.open(audioStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music");
        }
    }
}
