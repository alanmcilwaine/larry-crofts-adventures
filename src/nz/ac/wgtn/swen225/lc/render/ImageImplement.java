import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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
     * get the Image type for each Img
     */
    public Image getImage(String path){
        return Img.INSTANCE.getImgs(path);
    }

    /**
     * draw the image in each game board to the jpanel.
     */
    public void drawImages(Graphics g){
        List<List<Tile<Item>>> gameBoard = gameState.board();
        gameBoard.forEach(listTile ->
                listTile.forEach(tile ->
                        g.drawImage(getImage(tile.getItemOnTile()), tile.location.x(), tile.location.y(), jpanel)));
    }
}
