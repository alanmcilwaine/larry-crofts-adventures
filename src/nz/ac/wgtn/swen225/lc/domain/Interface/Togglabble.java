package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.Tile;

public interface Togglabble extends Item {
  void toggle(Tile<Item> tile);
}
