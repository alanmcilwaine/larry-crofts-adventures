package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameItem.NoItem;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

import java.util.List;

public class Crate extends MovableBox {

    public Crate(int x, int y) {
        super(x, y);
    }

    public void explode(List<MovableBox> boxes) {
        boxes.remove(this);
    }

    @Override
    public String toString() {
        return "Crate";
    }
}
