package nz.ac.wgtn.swen225.lc.app.UI.Widgets;

import nz.ac.wgtn.swen225.lc.app.UI.Containers.AppFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * UILabel --- The text styling for information section.
 *
 * @author Alan McIlwaine 300653905
 */
public class UILabel extends JLabel {
    private Timer timer;
    public UILabel(String text, int x, int y) {
        super(text, SwingConstants.CENTER);
        setFont(new Font("Monospaced", Font.PLAIN, 18));
        setBounds(x, y, 100, 50);
        setForeground(AppFrame.FOREGROUND);
    }

    @Override
    public void setText(String text) {
        if (timer != null) {
            timer.stop();
        }
        super.setText(text);
        setVisible(true);
        timer = new Timer(3000, (unused) -> {
            super.setText("");
            setVisible(false);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
