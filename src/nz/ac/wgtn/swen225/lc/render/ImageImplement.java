import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Item;
import nz.ac.wgtn.swen225.lc.domain.Location;

public class ImageImplement{
    // App's jpanel called game in App
    JPanel jpanel;
    GameState gameState;
    ImageImplement(JPanel jpanel, GameState gameState) {
        this.jpanel = jpanel;
        this.gameState = gameState;

        // ensure do not create a new jpanel object
        this.jpanel.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImages(g);
            }
        });

    }

    /**
     * draw the image in each game board to the jpanel.
     */
    public void drawImages(Graphics g) {
        List<List<Tile<Item>>> gameBoard = gameState.board();
        gameBoard.forEach(listTile -> listTile.forEach(tile -> {
            try {
                g.drawImage(Img.INSTANCE.getImgs(tile.getItemOnTile()), tile.location.x(), tile.location.y(), jpanel);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage()); // Prints "No such image: <name>"
            }
        }));
    }
}
