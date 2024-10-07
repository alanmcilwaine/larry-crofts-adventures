package nz.ac.wgtn.swen225.lc.render;


import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

import java.io.IOException;
import java.util.List;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;


public class ImageImplement{
    // App's jpanel called game in App
    private JPanel jpanel;
    public static final int IMAGE_SIZE = 70;
    private static final int BUFFER_SIZE = 5;
    private static final int xBorder = 260;
    private static final int yBorder = 260;
    private BackgroundImplement backgroundImplement;
    private InfoImplement info;
    private SoundEffectImplement soundImplement;

    // Flags to track if win or lose music has been played
    private boolean winMusicPlayed = false;
    private boolean loseMusicPlayed = false;

    ImageImplement(JPanel jpanel) {
        this.jpanel = jpanel;
        this.jpanel.setDoubleBuffered(true);
        backgroundImplement = new BackgroundImplement();
        info = new InfoImplement(jpanel);
        soundImplement = new SoundEffectImplement();
        new BackgroundSoundImplement().playMusic();
    }

    /**
     * Factory method getting the ImageImplement instance.
     */
    public static ImageImplement getImageImplement(JPanel jpanel) {
        return new ImageImplement(jpanel);
    }

    /**
     * Draw the image in each game board to the jpanel.
     */
    public void drawImages(GameState gameState, Graphics g) {
        info.fillAction(g, gameState);

        soundImplement.fillAction(gameState);
        backgroundImplement.drawBackGround(jpanel, g);
        drawItemsTile(gameState, g);
        drawActors(gameState, g);
        drawBoxes(gameState, g);
        info.locationMatch(gameState);
        soundImplement.locationMatch(gameState);
        new WinLoseImplement().drawWinLose(gameState, g, jpanel);

        new WinLoseImplement().allMusicPlayed(gameState);
    }

    /**
     * draw all the items and the tiles
     */
    public void drawItemsTile(GameState gameState, Graphics g){
        List<List<Tile<Item>>> gameBoard = gameState.board();
        Player player = gameState.player();
        // run through all the tiles
        gameBoard.forEach(listTile -> listTile.forEach(tile -> {
            // tile
            drawOneImage("Tile",
                    tile.location.x() - player.getLocation().x(),
                    tile.location.y() - player.getLocation().y(),
                    g);

            // items on the tile
            listTile.stream()
                    .filter(t -> !t.getItemOnTile().equals("NoItem"))
                    .forEach(t -> drawOneImage(t.getItemOnTile(),
                            t.location.x() - player.getLocation().x(),
                            t.location.y() - player.getLocation().y(),
                            g));
        }));

    }

    /**
     * draw all the Actors
     */
    public void drawActors(GameState gameState, Graphics g){
        // player
        Player player = gameState.player();
        drawOneImage(player.toString(), 0, 0, g);

        // robots
        List<Robot> robots = gameState.robots();
        robots.forEach(robot -> drawOneImage(robot.toString(),
                robot.getLocation().x() - player.getLocation().x(),
                robot.getLocation().y() - player.getLocation().y(), g));

    }

    /**
     * draw one image
     */
    public void drawOneImage(String imageName, int x, int y, Graphics g){

        if(Math.abs(x) < BUFFER_SIZE && Math.abs(y) < BUFFER_SIZE) {
            g.drawImage(Img.INSTANCE.getImgs(imageName + ".png"), x * IMAGE_SIZE + xBorder,
                    y * IMAGE_SIZE + yBorder, jpanel);
        }
    }

    public void drawBoxes(GameState gameState, Graphics g){
        List<MovableBox> boxList = gameState.boxes();
        Player player = gameState.player();
        boxList.forEach(box -> drawOneImage(box.toString(),
                box.getLocation().x() - player.getLocation().x(),
                box.getLocation().y() - player.getLocation().y(), g));
    }


}
