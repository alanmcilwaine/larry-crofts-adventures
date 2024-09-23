package nz.ac.wgtn.swen225.lc.render;


import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import java.util.List;
import javax.swing.*;
import java.awt.*;


public class ImageImplement{
    // App's jpanel called game in App
    private JPanel jpanel;
    private static final int IMAGE_SIZE = 30;
    private static final int BUFFER_SIZE = 5;


    ImageImplement(JPanel jpanel) {
        this.jpanel = jpanel;
        new BackgroundImplement().drawBackGround(jpanel);
        new SoundEffectImplement().playMusic();
    }

    /**
     * factory method getting the ImageImplement instance.
     */
    public static ImageImplement getImageImplement(JPanel jpanel){
        return new ImageImplement(jpanel);
    }


    /**
     * draw the image in each game board to the jpanel.
     */
    public void drawImages(GameState gameState){
        drawItemsTile(gameState);
        drawActors(gameState);
    }

    /**
     * draw all the items and the tiles
     */
    public void drawItemsTile(GameState gameState){
        List<List<Tile<Item>>> gameBoard = gameState.board();
        Player player = gameState.player();
        // run through all the tiles
        gameBoard.forEach(listTile -> listTile.forEach(tile -> {
            // tile
            drawOneImage("Tile",
                    tile.location.x() - player.getLocation().x(),
                    tile.location.y() - player.getLocation().y(),
                    jpanel);

            // items on the tile
            listTile.stream()
                    .filter(t -> !t.getItemOnTile().equals("NoItem"))
                    .forEach(t -> drawOneImage(t.getItemOnTile(),
                            t.location.x() - player.getLocation().x(),
                            t.location.y() - player.getLocation().y(),
                            jpanel));
        }));
        // robots
        List<Robot> robots = gameState.robots();
        robots.forEach(robot -> drawOneImage(robot.toString(),
                robot.getLocation().x() - player.getLocation().x(),
                robot.getLocation().y() - player.getLocation().y(), jpanel));

    }

    /**
     * draw all the Actors
     */
    public void drawActors(GameState gameState){
        // player
        Player player = gameState.player();
        drawOneImage(player.toString(), 0, 0, jpanel);

    }

    /**
     * draw one image
     */
    public void drawOneImage(String imageName, int x, int y, JPanel jpanel){
        Graphics g = jpanel.getGraphics();
        g.drawImage(Img.INSTANCE.getImgs(imageName + ".png"), (x + BUFFER_SIZE) * IMAGE_SIZE,
                (y + BUFFER_SIZE) * IMAGE_SIZE, jpanel);

    }
}
