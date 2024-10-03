package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

public class Laser implements Item {
// TODO should this be an actor as well ? it's behaviour is interesting

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    // if player then die then if crate then disappear

    if (actor instanceof Player p) { p.die(); }
    if (actor instanceof Crate) { tile.item = new NoItem(); }
  }

  @Override
  public String toString() { return "Laser"; }
}
