package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Crate;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Mirror;
import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.List;
import java.util.function.Supplier;

public class Laser implements Item {
  Direction direction;
  Tile<Item> targetTile;
  Location target;
  Laser childLaser;

  public Laser(GameBoard gameBoard, Direction direction, Location location) {
    this.direction = direction;
    target = direction.act(location);

    if(target.x() < gameBoard.getWidth() && target.y() < gameBoard.getHeight()) {
      targetTile = gameBoard.getBoard().get(target.x()).get(target.y());
      childLaser = new Laser(gameBoard, direction, target);
      passLaser(() -> childLaser);
    }
  }

  public Direction getDirection() { return direction; }

  public void passLaser(Supplier<Item> itemSupplier) {
    if (targetTile != null && !(targetTile.item instanceof Wall)) {
      targetTile.item = itemSupplier.get();
      childLaser.passLaser(itemSupplier);
    }
  }

  @Override
  public boolean blockActor(Actor actor) { return false; }

  @Override
  public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
    // if player then die and if crate then disappear

    if (actor instanceof Player p) { p.die(); }
    if (actor instanceof MovableBox m) {
      tile.item = new NoItem();
      passLaser(NoItem::new);
    }
  }

  @Override
  public <T extends Item> void onExit(Actor actor, Tile<T> tile) {
    tile.item = childLaser;
    passLaser(() -> childLaser);
  }

  @Override
  public String toString() { return "Laser"; }
}
