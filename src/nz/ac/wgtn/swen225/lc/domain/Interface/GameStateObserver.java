package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;

public interface GameStateObserver {
    void update(int treasureNumber);
}
