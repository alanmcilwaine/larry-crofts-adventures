package nz.ac.wgtn.swen225.lc.Domain.GameItem;

import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.ItemColor;

public record UnLockedDoor(ItemColor itemColor) implements Item {
}
