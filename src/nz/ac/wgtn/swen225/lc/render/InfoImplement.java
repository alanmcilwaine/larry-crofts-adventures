package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Info;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * responsible for managing and displaying information messages
 * on the game's JPanel when the player reaches certain locations.
 */
public class InfoImplement {
    JTextArea textArea = new JTextArea();
    JPanel jPanel;

    private Map<Location, Runnable> locationActionMap = new HashMap<>();

    /**
     * sets up the JTextArea for displaying information
     * and configures its appearance and layout within the provided JPanel.
     *
     * @param jPanel the JPanel where the information will be displayed.
     */
    public InfoImplement(JPanel jPanel) {
        this.jPanel = jPanel;
        textArea.setFont(new Font("Monospaced", Font.BOLD, 35));
        textArea.setForeground(Color.RED);
        textArea.setBackground(Color.BLACK);
        textArea.setBounds(0, 400, 580, 130);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        jPanel.setLayout(null);
    }

    /**
     * Displays a warning icon along with the provided text on the game screen.
     *
     * @param g    the Graphics object used to draw the image.
     * @param text the text to display in the text area.
     * @throws IOException if there is an error loading the warning image.
     */
    public void showInfo(Graphics g, String text) throws IOException {
        Image image = ImageIO.read(new File("BackgroundImage/warning.png"));
        g.drawImage(Img.resizeImage(image, 140, 150), 0, 260, null);
        textArea.setText(text);
        textArea.setVisible(true);
        jPanel.add(textArea);
    }

    /**
     * Fills the action map with tasks that should be executed when the player reaches certain tiles.
     * It finds all tiles containing an Info item and maps their locations to actions that display
     * the related information.
     *
     * @param g          the Graphics object used to draw the info.
     * @param gameState  the current state of the game.
     */
    public void fillAction(Graphics g, GameState gameState){
        textArea.setVisible(false);
        gameState.board().forEach(listTile ->
                listTile.stream()
                        // filter the item Info
                        .filter(tile -> tile.item instanceof Info)
                        .forEach(i -> {
                            Info safeI = (Info) i.item;
                            // add the action to the location of Info
                            locationActionMap.put(i.location,
                                    () -> {
                                        try {
                                            showInfo(g, safeI.info());
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        }));
    }

    /**
     * Matches the player's current location to an action in the locationActionMap, and if a match is found,
     * executes the associated action to display information. Clears the action map after execution.
     *
     * @param gameState the current state of the game.
     */
    public void locationMatch(GameState gameState){
        Location playerLocation = gameState.player().getLocation();
        Runnable action = locationActionMap.get(playerLocation);
        if(action != null){
            action.run();
        }
        locationActionMap.clear();
    }

}
