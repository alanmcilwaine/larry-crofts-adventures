package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
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
import java.util.Map;

public class SoundEffectImplement {
    private Map<Location, Runnable> SoundActionMap = new HashMap<>();

    public void collectKeySound(String type) throws UnsupportedAudioFileException, IOException, LineUnavailableException {


        File wavFile = new File("SoundEffect/get" + type + ".wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        clip.start();
    }

    public void eachFillAction(GameState gameState, Class<?> type) {
        gameState.board().forEach(listTile ->
                listTile.stream()
                        .filter(tile -> type.isInstance(tile.item))  // Use type.isInstance to filter
                        .forEach(i -> {
                            // Adding action to SoundActionMap
                            SoundActionMap.put(i.location, () -> {
                                try {
                                    collectKeySound(type.getSimpleName());  // Play sound for Key
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                    throw new RuntimeException("No such sound effect for item: " + i.getItemOnTile());
                                }
                            });
                        })
        );
    }

    public void fillAction(GameState gameState){
        eachFillAction(gameState, Key.class);
        eachFillAction(gameState, LockedDoor.class);
        eachFillAction(gameState, Treasure.class);
        eachFillAction(gameState, Button.class);

    }

    public void locationMatch(GameState gameState){
        Location playerLocation = gameState.player().getLocation();
        Runnable action = SoundActionMap.get(playerLocation);
        if(action != null){
            action.run();
            SoundActionMap.remove(playerLocation);
        }


    }
}
