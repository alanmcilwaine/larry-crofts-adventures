package nz.ac.wgtn.swen225.lc.render;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * handles playing background sound effects using .wav files.
 *
 * @author libaix 300641237
 * @version 2.5
 */
public class BackgroundSoundImplement {
    private static Clip clip;
    /**
     * should mute the music or not
     */
    private static boolean isMuted = true;
    /**
     * the status decide what kine of music to be implemented
     */
    public static final String status = "soundEffect";
    // load the different sounds based on different status
    private static final Supplier<String> statusSupplier = () -> status;

    /**
     * Plays the background music or sound effect based on the current status.
     *
     * @throws RuntimeException if the audio file cannot be loaded or played.
     */
    public static void playMusic() {
        if(isMuted) return;
        try {
            File wavFile = new File("SoundEffect/" + statusSupplier.get() + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
            if (clip == null || !clip.isOpen()) {
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("No such music for the status: " + statusSupplier.get());
        }
    }

    /** mute and unmute all the music
     * @param mute to decide whether mute the background music
     */
    public static void muteMusic(boolean mute){
        WinLoseImplement.mute(mute);
        SoundEffectImplement.mute(mute);
        mute(mute);
    }

    /** mute and unmute the background music
     * @param mute to decide whether mute the background music
     */
    public static void mute(boolean mute){
        isMuted = mute;
        if(mute && clip!= null){
            clip.stop();
        }
        else{
            playMusic();
        }
    }

    /**
     * get the isMuted filed to check mute/unmute
     *
     * @return isMuted to check if the music needs to be muted
     */
    public static Boolean getMuted(){
        return isMuted;
    }
}
