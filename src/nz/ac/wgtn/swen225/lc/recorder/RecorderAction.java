package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * A nice way to cleanly make Actions
 */
public interface RecorderAction{
    static Action of(Runnable r) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        };
    }
}

