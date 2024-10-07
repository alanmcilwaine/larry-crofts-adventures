package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;

public class Crate extends MovableBox {

    public Crate(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "Crate";
    }
}
