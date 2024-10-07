package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

public class LaserInput implements Item {
  boolean isHitByLaser = false;

  @Override
  public boolean blockActor(Actor actor) { return true; }


  @Override
  public String toString() { return "LaserInput"; }

  @Override
  public Item makeNew() { return new LaserInput(); }
}
