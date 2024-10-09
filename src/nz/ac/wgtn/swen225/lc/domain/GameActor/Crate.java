package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameItem.NoItem;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public class Crate extends MovableBox {

    public Crate(int x, int y) {
        super(x, y);
    }

    public void explode(Tile<Item> tile) {
        tile.item = new NoItem();
    }

    @Override
    public String toString() {
        return "Crate";
    }
}
