package nz.ac.wgtn.swen225.lc.app.UI.Widgets;


import javax.swing.*;
import java.awt.*;

public class SquareButton extends JButton {
    private int x;
    private int y;
    public SquareButton(String text, int x, int y){
        super(text);
        this.x = x;
        this.y = y;
        build();
    }

    public SquareButton(Icon icon, int x, int y){
        super(icon);
        this.x = x;
        this.y = y;
        build();
    }

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
