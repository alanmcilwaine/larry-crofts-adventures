package nz.ac.wgtn.swen225.lc.render;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Info;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class InfoImplement {
    JTextArea textArea = new JTextArea();
    JPanel jPanel;

    private Map<Location, Runnable> locationActionMap = new HashMap<>();

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


        // Add more levels and hints as needed
    }

    public void showInfo(Graphics g, String text) throws IOException {
        Image image = ImageIO.read(new File("BackgroundImage/warning.png"));
        g.drawImage(Img.resizeImage(image, 130, 150), 185, 260, null);
        textArea.setText(text);
        textArea.setVisible(true);
        jPanel.add(textArea);

    }


    public void fillAction(Graphics g, GameState gameState){
        textArea.setVisible(false);
        gameState.board().forEach(listTile ->
                listTile.stream()
                        .filter(tile -> tile.item instanceof Info)
                        .forEach(i -> {
                            Info safeI = (Info) i.item;
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

    public void locationMatch(GameState gameState){
        Location playerLocation = gameState.player().getLocation();
        Runnable action = locationActionMap.get(playerLocation);
        if(action != null){
            action.run();
        }
        locationActionMap.clear();

    }

}
