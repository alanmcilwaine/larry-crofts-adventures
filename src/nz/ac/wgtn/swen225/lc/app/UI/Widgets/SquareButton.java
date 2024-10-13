package nz.ac.wgtn.swen225.lc.app.UI.Widgets;


import javax.swing.*;
import java.awt.*;

/**
 * SquareButton --- The button styling for the project.
 *
 * @author Alan McIlwaine 300653905
 */
public class SquareButton extends JButton {
    private int x;
    private int y;

    /**
     * Used to generate a button that has text at an x, y.
     * @param text The text in the button.
     * @param x X position.
     * @param y Y position.
     */
    public SquareButton(String text, int x, int y){
        super(text);
        this.x = x;
        this.y = y;
        build();
    }

    /**
     * Used generate a button that has an icon at x, y.
     * @param icon The icon in the button.
     * @param x X position.
     * @param y Y position.
     */
    public SquareButton(Icon icon, int x, int y){
        super(icon);
        this.x = x;
        this.y = y;
        build();
    }

    /**
     * Builds the relevant styling for the button.
     */
    private void build(){
        setBounds(x, y, 50, 50);
        setBorder(BorderFactory.createLineBorder(new Color(225, 211, 174), 2));
        setBackground(new Color(105, 88, 92));
        setForeground(new Color(225, 211, 174));
        setOpaque(true);
        setFocusable(false);
        setVisible(true);
    }
}
