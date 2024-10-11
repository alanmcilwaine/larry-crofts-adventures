package nz.ac.wgtn.swen225.lc.app.UI.Containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Action;

public class RemapFrame extends JFrame {
    public RemapFrame(){
        assert SwingUtilities.isEventDispatchThread();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(App.WIDTH, App.HEIGHT);
        setLayout(new GridLayout(3, 4));
        displayMappings();
        setVisible(true);
    }

    private void displayMappings() {
        getContentPane().removeAll();
        for (Action action : Action.values()) {
            add(new JLabel(action.description));
            JTextField mapping = new JTextField((action.control ? "CTRL-" : "") + KeyEvent.getKeyText(action.key));
            // Handle duplicate keys
            if (Arrays.stream(Action.values())
                    .filter(v -> v.key == action.key && action.control == v.control && action.key != 0)
                    .count() > 1) {
                mapping.setBorder(BorderFactory.createLineBorder(Color.red));
            }
            mapping.addActionListener((unused) -> {
                processMapping(action, KeyEvent.getExtendedKeyCodeForChar(mapping.getText().replace("CTRL-", "").charAt(0)));
            });
            add(mapping);
        }
        revalidate();
        repaint();
    }

    private void processMapping(Action action, int key) {
        if (action == null) {
            return;
        }
        action.key = key;
        displayMappings();
    }
}
