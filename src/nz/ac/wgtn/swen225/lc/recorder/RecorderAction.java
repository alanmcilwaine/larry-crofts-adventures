/**
 * Has 1 static method to allow us to make Actions easily and cleanly
 *
 * <p>
 * Usage Example:
 * <pre>
 *     RecorderAction.of(() -> System.out.println("Convenient!"));
 * </pre>
 * </p>
 *
 * @author John Rais raisjohn@ecs.vuw.ac.nz
 * @version 2.0
 */
package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.awt.event.ActionEvent;

public interface RecorderAction{
    /**
     * A nice way to cleanly make Actions
     *
     * @param r The action you want to happen when this ActionListener is run
     */
    static Action of(Runnable r) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        };
    }
}

