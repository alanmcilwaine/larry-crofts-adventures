package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.Togglabble;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.List;

public class Button implements Item {
  public boolean isPressed = false;
  List<Tile<Item>> surroundingTiles = new ArrayList<>();
  public boolean isBig = false;

  public void attachTiles(List<Tile<Item>> surroundingTiles) {
    this.surroundingTiles = surroundingTiles;
    System.out.println("break");
    System.out.println("break");
  }

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    // Robots should not press the button but MovableBoxes can
    if (actor instanceof Robot) { return; }

    if (isBig && !(actor instanceof MovableBox)) { return; }
    toggleSurroundingTiles();
  }

  public void toggleSurroundingTiles() {
    if (isPressed) { return; }
    surroundingTiles.forEach(t -> {
      if (t.item instanceof Togglabble tg) {
        tg.toggle(t);
      }
    });
    isPressed = true;
  }

  @Override
  public <T extends Item> void onExit(Actor actor, Tile<T> tile) {
    isPressed = false;
  }

  @Override
  public String toString() {
    if (isBig) {
      return isPressed ? "PressedButton" : "Button";
    } else {
      return isPressed ? "PressedButtonSmall" : "ButtonSmall";
    }
  }

  @Override
  public Item makeNew() { return new Button(); }
}
