package nz.ac.wgtn.swen225.lc.app.UI.Widgets;

import javax.swing.*;

/**
 * Icons --- Loads the dedicated icons for the recorder panel.
 *
 * @author Alan McIlwaine 300653905
 */
public enum Icons {
    Undo(new ImageIcon("AllItemsImages/undo.png")),
    Redo(new ImageIcon("AllItemsImages/redo.png")),
    Resume(new ImageIcon("AllItemsImages/resume.png")),
    Pause(new ImageIcon("AllItemsImages/pause.png")),;

    private final ImageIcon icon;
    Icons(ImageIcon icon) {
        this.icon = icon;
    }
    public ImageIcon icon() {
        return icon;
    }
}
