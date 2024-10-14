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

import java.util.HashMap;
import java.util.Map;

/**
 * An item that produces a line of lasers.
 *
 * @author Carla Parinas 300653631
 */
public class LaserSource implements Togglabble {
  private final Location location; // needs to set the laser somewhere
  private final Direction direction; // has a direction but it should not move as well as blocks all actors
  public String orientation;

  private boolean laserToggle; // auto set to false
  private final Map<Location, String> lasers = new HashMap<>();

  public Location target;
  public Direction currentDirection;

  public LaserSource(Direction direction, boolean toggle, int x, int y) {
    this.direction = direction;
    this.location = new Location(x, y);
    this.laserToggle = toggle;
  }

  /**
   * Set the orientation of the laser based on the direction of the laser
   */
  private void setOrientation()  {
    switch (currentDirection) {
      case UP, DOWN -> orientation = "vertical";
      case LEFT, RIGHT -> orientation = "horizontal";
    }
  }

  /**
   * Update the path of the laser -- loops until the laser finds a part it can't reach
   * @param gameBoard current Gameboard
   */
  public void updateLasers(GameBoard gameBoard) {

    // clear the lasers and set initial values for direction and target
    lasers.clear();
    currentDirection = direction;
    target = direction.act(location);

    if (!laserToggle) return;

    while(target != null &&
            target.x() >= 0 && target.x() < gameBoard.getWidth() &&
            target.y() >= 0 && target.y() < gameBoard.getHeight())
    {

      Item targetItem = gameBoard.itemOnTile(target).item;

      // checks for a movable box in path of the laser
      MovableBox box = gameBoard.getGameState().boxes()
              .stream().filter(b -> b.getLocation().equals(target))
              .findFirst().orElse(null);

      if (box != null) {
        if (box instanceof Mirror m) {
          m.reflectLaser(this);
          // reset the loop again and run same checks
          continue;
        }
        else if (box instanceof Crate c)  { c.explode(gameBoard.getGameState().boxes()); }
        else { break; }
      }

      if(targetItem instanceof LaserInput l) {
        l.toggleSurroundingTiles();
      }

      if(targetItem instanceof NoItem || targetItem instanceof Tube || targetItem instanceof LaserInput) {
        setOrientation();
        lasers.put(target, orientation);
        target = currentDirection.act(target);
      } else {
        break;
      }
    }
  }

  /**
   * Gets the direction of the lasersource, NOT the laser
   * @return laser source direction
   */
  public Direction getDirection() { return direction; }

  /**
   * Gets the list of locations that the lasers are occupying, along with their orientation
   * @return a map of location and strings representing the lasers
   */
  public Map<Location, String> getLasers() { return lasers; }

  /**
   * Toggles the laser on and off
   */
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
