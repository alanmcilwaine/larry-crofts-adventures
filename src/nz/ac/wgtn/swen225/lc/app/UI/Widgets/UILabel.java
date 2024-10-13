package nz.ac.wgtn.swen225.lc.app.UI.Widgets;

import javax.swing.*;
import java.awt.*;

/**
 * UILabel --- The text styling for information section.
 *
 * @author Alan McIlwaine 300653905
 */
public class UILabel extends JLabel {
    public UILabel(String text, int x, int y) {
        super(text, SwingConstants.CENTER);
        setFont(new Font("Monospaced", Font.PLAIN, 18));
        setBounds(x, y, 100, 50);
        setForeground(new Color(225, 211, 174));
    }
}
