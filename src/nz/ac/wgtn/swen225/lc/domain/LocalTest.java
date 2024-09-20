package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameItem.UnLockedDoor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class LocalTest {
    public static void test() {
        var t = new Tile<>(new UnLockedDoor(ItemColor.RED), new Location(1, 1));

        System.out.println(t.getItemOnTile());
    }

    public static void main(String[] s) {
        test();
    }
}
