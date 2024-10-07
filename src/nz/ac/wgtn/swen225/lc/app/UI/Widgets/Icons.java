package nz.ac.wgtn.swen225.lc.app.UI.Widgets;

import javax.swing.*;

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
