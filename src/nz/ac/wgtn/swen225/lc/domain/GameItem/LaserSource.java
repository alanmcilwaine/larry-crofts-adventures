package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Crate;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Mirror;
import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.Togglabble;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An item that produces a line of lasers.
 *
 * @author Carla Parinas
 */
public class LaserSource implements Togglabble {
// TODO: FIX THIS UP
  private Location location; // needs to set the laser somewhere
  private Direction direction; // has a direction but it should not move as well as blocks all actors
  private String orientation;

  private boolean laserToggle = true; // auto set to false
  private Map<Location, String> lasers = new HashMap<>();

  public Location target;
  public Direction currentDirection;


  public LaserSource(Direction direction, boolean toggle, int x, int y) {
    this.direction = direction;
    this.location = new Location(x, y);
    this.laserToggle = toggle;

  }

  private void setOrientation()  {
    switch (currentDirection) {
      case UP, DOWN -> orientation = "vertical";
      case LEFT, RIGHT -> orientation = "horizontal";
    }
  }

  public void updateLasers(GameBoard gameBoard) {
    lasers.clear();
    currentDirection = direction;
    target = direction.act(location);

    Item targetItem = gameBoard.itemOnTile(target).item;

    while(target != null &&
            target.x() >= 0 && target.x() < gameBoard.getWidth() &&
            target.y() >= 0 && target.y() < gameBoard.getHeight() &&
            targetItem.equals(new NoItem())) {

      MovableBox box = gameBoard.getGameState().boxes()
              .stream().filter(b -> b.getLocation().equals(target))
              .findFirst().orElse(null);

      if (box != null) {
        if (box instanceof Mirror m) {
          lasers.put(target, orientation);
          m.reflectLaser(this);
        }
        if (box instanceof Crate c)  { c.explode(gameBoard.itemOnTile(target)); }
      }

        setOrientation();
        lasers.put(target, orientation);
        target = currentDirection.act(target);
    }
  }

  public Direction getDirection() { return direction; }

  public Map<Location, String> getLasers() { return lasers; }

  public void setLaserToggle() {
    laserToggle = !laserToggle;
  }

  @Override
  public boolean blockActor(Actor actor) { return true; }

  @Override
  public void toggle(Tile<Item> tile) {
    setLaserToggle();
  }

  @Override
  public String toString() { return "LaserSource"; }

  @Override
  public Item makeNew() {
    return new LaserSource(direction, laserToggle, location.x(), location.y());
  }
}
