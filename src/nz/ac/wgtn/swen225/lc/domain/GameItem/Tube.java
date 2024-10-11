package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

public class Tube implements Item {

  @Override
  public boolean blockActor(Actor actor) { return true; }

  @Override
  public String toString() { return "Tube"; }

  @Override
  public Item makeNew() {
    return new Tube();
  }
}
