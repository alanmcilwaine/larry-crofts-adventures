package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public class Button implements Item {
  boolean isPressed = false;

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    isPressed = !isPressed; // switch it
  }

  @Override
  public String toString() {
    return isPressed ? "ButtonPressed" : "Button";
  }
}
