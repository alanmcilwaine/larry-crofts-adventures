package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import java.awt.*;

/**
 * UIPanel --- JPanel extension which holds the UI elements to the right side of the screen.
 *
 * @author Alan McIlwaine 300653905
 */
public class UIPanel extends JPanel {
    /**
     *  Create a new JPanel with 300x600 size with the default layout manager.
     */
    public UIPanel(GridLayout layout) {
        super(layout);
        setPreferredSize(new Dimension(WIDTH/3, HEIGHT));     // 300x600
        setBackground(App.BACKGROUND);
    }
}
