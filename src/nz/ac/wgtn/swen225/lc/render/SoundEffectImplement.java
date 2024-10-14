package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Button;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Key;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedDoor;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Treasure;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * responsible for managing and playing sound effects
 * when certain actions occur in the game.
 * It stores sound effects actions mapped to specific game locations and triggers them based
 * on the player's movements.
 *
 * @author libaix 300641237
 * @version 2.5
 */
public class SoundEffectImplement {
    private final Map<Location, Runnable> SoundActionMap = new HashMap<>();
    private final Set<Location> soundPlayedLocations = new HashSet<>();
    private Location previousLocation = null;
    private static Clip clip;

    /**
     * Plays the sound effect when a key or other specific item is collected.
     *
     * @param type the type of item being collected, used to determine the corresponding sound effect.
     * @throws UnsupportedAudioFileException if the audio format is unsupported.
     * @throws IOException if there is an error reading the sound file.
     * @throws LineUnavailableException if the audio line cannot be opened.
     */
    public void sound(String type) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if(BackgroundSoundImplement.isMuted) return;
        File wavFile = new File("SoundEffect/get" + type + ".wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    /**
     * mute the sound effect music
     *
     * @param mute to decide should mute the music or not
     */
    public static void mute(boolean mute){
        if(mute && clip!= null){
            clip.stop();
        }

    }

    /**
     * Fills the SoundActionMap with actions that should trigger sound effects when the player
     * reaches certain tiles containing specific types of game items
     *
     * @param gameState the current state of the game.
     * @param type the class type of the game item to match.
     */
    public void eachFillAction(GameState gameState, Class<?> type) {
        gameState.board().forEach(listTile ->
                listTile.stream()
                        .filter(tile -> type.isInstance(tile.item))  // Use type.isInstance to filter
                        .forEach(i -> {
                            // Adding action to SoundActionMap
                            SoundActionMap.put(i.location, () -> {
                                try {
                                    // Play sound for item
                                    sound(type.getSimpleName());
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                    throw new RuntimeException("No such sound effect for item: " + i.getItemOnTile());
                                }
                            });
                        })
        );
    }

    /**
     * use the method eachFillAction() to implement different sound effects
     *
     * @param gameState the current state of the game.
     */
    public void fillAction(GameState gameState){
        eachFillAction(gameState, Key.class);
        eachFillAction(gameState, LockedDoor.class);
        eachFillAction(gameState, Treasure.class);
        eachFillAction(gameState, Button.class);

    }

    /**
     * Matches the player's current location to a sound action in the SoundActionMap, and if a match is found,
     * executes the associated action (plays the sound) and then removes it from the map.
     *
     * @param gameState the current state of the game.
     */
    public void locationMatch(GameState gameState){
        Location playerLocation = gameState.player().getLocation();
        Runnable action = SoundActionMap.get(playerLocation);
        // Check if player moved off a previously triggered button
        if (previousLocation != null && !playerLocation.equals(previousLocation)) {
            soundPlayedLocations.remove(previousLocation);
        }

        // Play sound if the player is on a button, and it hasn't played yet
        if (action != null && !soundPlayedLocations.contains(playerLocation)) {
            action.run();
            SoundActionMap.remove(playerLocation);
            soundPlayedLocations.add(playerLocation); // Mark location as sound played
        }

        previousLocation = playerLocation; // Update previous location
    }


}