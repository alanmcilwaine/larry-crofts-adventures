import javax.swing.*;
import java.awt.*;

public class ImageImplement{
    // App's jpanel called game in App
    JPanel jpanel;
    ImageImplement(JPanel jpanel) {
        this.jpanel = jpanel;

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
        return Img.getImgs().stream()
                .filter(img -> img.getImageName().equals(path))
                .findFirst()
                .map(Img::getImage)
                .orElseThrow(() -> new Error("no such image"));
    }

    /**
     * draw the image in each game board to the jpanel.
     */
    public void drawImages(Graphics g){
        IEntity[][] gameBoard = new GameState().getBoard();
        for(int row = 0; row < gameBoard.length; row++){
            for(int col = 0; col < gameBoard[row].length; col++){
                g.drawImage(getImage(gameBoard[row][col].getImage()), 0, 0, jpanel);
            }
        }
    }
}
