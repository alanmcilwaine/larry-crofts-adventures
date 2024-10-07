package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.Togglabble;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.List;

public class Button implements Item {
  boolean isPressed = false;
  List<Tile<Item>> surroundingTiles = new ArrayList<>();

  public void attachTiles(List<Tile<Item>> surroundingTiles) {
    this.surroundingTiles = surroundingTiles;
  }

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    // Robots should not press the button but MovableBoxes can
    if (actor instanceof Robot) { return; }

    isPressed = true; // set true
    surroundingTiles.forEach(t -> { if (t.item instanceof Togglabble tg) { tg.toggle(t); } });

  }

  @Override
  public String toString() {
    return isPressed ? "PressedButton" : "Button";
  }
}
