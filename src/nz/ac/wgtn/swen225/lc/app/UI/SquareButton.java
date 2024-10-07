package nz.ac.wgtn.swen225.lc.app.UI;


import javax.swing.*;
import java.awt.*;

public class SquareButton extends JButton {
    public SquareButton(String text){
        super(text);
        build();
    }

    public SquareButton(Icon icon){
        super(icon);
        build();
    }

    private void build(){
        setPreferredSize(new Dimension(100, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.GRAY);
    }
}
