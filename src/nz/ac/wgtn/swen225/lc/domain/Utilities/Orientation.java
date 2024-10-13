package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Orientation {
  ONE {

    @Override
    public void setReflectionMap() {
      reflectionMap = Map.of(Direction.DOWN, Direction.LEFT,
                             Direction.UP, Direction.RIGHT,
                             Direction.RIGHT, Direction.UP,
                             Direction.LEFT, Direction.DOWN );
    }
  },
  TWO {
    @Override
    public void setReflectionMap() {
      reflectionMap = Map.of(Direction.DOWN, Direction.RIGHT,
                             Direction.UP, Direction.LEFT,
                             Direction.RIGHT, Direction.DOWN,
                             Direction.LEFT, Direction.UP);
    }
  };
  Map<Direction, Direction> reflectionMap = new HashMap<>();
  String lastUsedDir;

  /**
   * Changes the direction of the source's laser
   */
  public void reflectLaser(LaserSource source) {
    Direction reflect = reflectionMap.get(source.currentDirection);

    if (reflect != null) {
      Location o = source.target;
      Location n = reflect.act(source.target);

      lastUsedDir = "Reflect" + (n.x() < o.x() ? "left" : "right") + (n.y() < o.y() ? "up" : "down");
      source.getLasers().put(source.target, lastUsedDir);

      source.currentDirection = reflect;
      source.target = n;
      source.orientation = lastUsedDir;
    }

  }

  Orientation() { setReflectionMap(); }

  public void setReflectionMap() {}

  public Map<Direction, Direction> getReflectionMap() {
    return Collections.unmodifiableMap(reflectionMap);
  }

  @Override
  public String toString() { return lastUsedDir; }
}
