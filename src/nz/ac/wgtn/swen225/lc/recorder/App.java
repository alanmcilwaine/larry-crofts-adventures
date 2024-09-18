package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.Command;

public interface App {

    int TICK_RATE = 50;

    void updateGraphics();
    void giveInput(Command input);
    void initialStateRevert();
}
