package nz.ac.wgtn.swen225.lc.render;


import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * responsible for rendering various game elements
 * (tiles, items, actors, and boxes) onto a JPanel, as well as managing background
 * sound and additional effects during the game.
 *
 * @author libaix 300641237
 * @version 2.5
 */
public class ImageImplement{
    // App's panel
    private final JPanel jpanel;
    /**
     * the parameters help to draw the maze on the right place
     * also the background, information and sound effect
     */
    public static final int IMAGE_SIZE = 70;
    private static final int BUFFER_SIZE = 5;
    private static final int xBorder = 260;
    private static final int yBorder = 260;
    private final BackgroundImplement backgroundImplement;
    private final InfoImplement info;
    private final SoundEffectImplement soundImplement;

    /**
     * Constructs an ImageImplement object to draw elements on the provided JPanel.
     * Initializes background, sound, and info implementations.
     *
     * @param panel the JPanel where game images are drawn.
     */
    ImageImplement(JPanel panel) {
        this.jpanel = panel;
        this.jpanel.setDoubleBuffered(true);
        backgroundImplement = new BackgroundImplement();
        info = new InfoImplement(panel);
        soundImplement = new SoundEffectImplement();
        BackgroundSoundImplement.playMusic();
    }

    /**
     * Factory method for getting an ImageImplement instance.
     *
     * @param panel the JPanel where the images will be drawn.
     * @return an instance of ImageImplement.
     */
    public static ImageImplement getImageImplement(JPanel panel) {
        return new ImageImplement(panel);
    }

    /**
     * Draws the game elements, implements the info, sounds and background
     *
     * @param gameState the current state of the game.
     * @param g the Graphics object used for rendering.
     */
    public void drawImages(GameState gameState, Graphics g) {
        // give actions to info
        info.fillAction(g, gameState);

        // give action to sound effect
        soundImplement.fillAction(gameState);

        // background
        backgroundImplement.drawBackGround(jpanel, g);

        // game items
        drawItemsTile(gameState, g);
        drawActors(gameState, g);
        drawBoxes(gameState, g);
        drawLasers(gameState, g);

        // implement the action to info
        info.locationMatch(gameState);

        // implement the action to sound effect
        soundImplement.locationMatch(gameState);

        // win lose implementation
        new WinLoseImplement().drawWinLose(gameState, g, jpanel);
        new WinLoseImplement().allMusicPlayed(gameState);
    }

    /**
     * Draws all the items and tiles on the game board.
     *
     * @param gameState the current state of the game.
     * @param g the Graphics object used for rendering.
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
     * Draws all actors in the game, including the player and robots.
     *
     * @param gameState the current state of the game.
     * @param g the Graphics object used for rendering.
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
     * Draws a single image.
     *
     * @param imageName the name of the image file to be drawn.
     * @param x the x-coordinate relative to the player.
     * @param y the y-coordinate relative to the player.
     * @param g the Graphics object used for rendering.
     */
    public void drawOneImage(String imageName, int x, int y, Graphics g){
        if(Math.abs(x) < BUFFER_SIZE && Math.abs(y) < BUFFER_SIZE) {
            g.drawImage(Img.INSTANCE.getImgs(imageName + ".png"), x * IMAGE_SIZE + xBorder,
                    y * IMAGE_SIZE + yBorder, jpanel);
        }
    }

    /**
     * Draws all movable boxes in the game.
     *
     * @param gameState the current state of the game.
     * @param g the Graphics object used for rendering.
     */
    public void drawBoxes(GameState gameState, Graphics g){
        List<MovableBox> boxList = gameState.boxes();
        Player player = gameState.player();
        boxList.forEach(box -> drawOneImage(box.toString(),
                box.getLocation().x() - player.getLocation().x(),
                box.getLocation().y() - player.getLocation().y(), g));
    }

    /**
     * Draws all lasers emitted by laser sources in the game.
     *
     * @param gameState the current state of the game.
     * @param g the Graphics object used for rendering.
     */
    public void drawLasers(GameState gameState, Graphics g) {
        // Get player for reference of relative positioning
        Player player = gameState.player();

        // Get all laser sources from the game state
        List<LaserSource> lasers = gameState.laserSources();

        // Loop through each laser source and its lasers
        lasers.forEach(laserSource -> laserSource.getLasers().forEach((laserLocation, orientation) -> {
            int relativeX = laserLocation.x() - player.getLocation().x();
            int relativeY = laserLocation.y() - player.getLocation().y();
            drawOneImage("laser" + orientation, relativeX, relativeY, g);
        }));
    }


}
