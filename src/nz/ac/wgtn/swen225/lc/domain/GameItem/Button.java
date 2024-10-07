package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public class Button implements Item {
  boolean isPressed = false;
  Item attached;

  public Button(Item attached) {
    this.attached = attached;
  }

  public Button() {}

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    // Robots should not press the button but MovableBoxes can
    if (actor instanceof Robot) { return; }
    isPressed = !isPressed; // switch it


  }

  @Override
  public String toString() {
    return isPressed ? "PressedButton" : "Button";
  }
}
