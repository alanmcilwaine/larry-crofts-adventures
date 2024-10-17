package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an orientation for the mirror with it's respective reflection directions
 * Has two types: ONE and TWO.
 *
 * @author Carla Parinas 300653631
 */
public enum Orientation {
    ONE {
        @Override
        public void setReflectionMap() {
            reflectionMap = Map.of(Direction.DOWN, Direction.LEFT,
                    Direction.UP, Direction.RIGHT,
                    Direction.RIGHT, Direction.UP,
                    Direction.LEFT, Direction.DOWN);
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
        // get the reflection from the map
        Direction reflect = reflectionMap.get(source.currentDirection);

        if (reflect != null) {
            // set locations to old and new target, as well as get mirror location
            Location o = source.lastTarget;
            Location n = reflect.act(source.target);
            Location mirror = source.target;

            // decide which side is the laser coming from to decide the sprite
            Location leftRight = mirror.y() == n.y() ? n : o;
            Location upDown = mirror.x() == n.x() ? n : o;

            lastUsedDir = "Reflect" + (leftRight.x() < mirror.x() ? "left" : "right") + (upDown.y() < mirror.y() ? "up"
                    : "down");

            source.getLasers().put(source.target, lastUsedDir);

            source.currentDirection = reflect;
            source.lastTarget = source.target;
            source.target = n;
            source.orientation = lastUsedDir;
        }

    }

    /**
     * Constructor sets the reflection map based on the enum type
     */
    Orientation() {
        setReflectionMap();
    }

    /**
     * Sets the reflection map based on the orientation type. The map contains
     * all four directions (not including Direction.NONE) and it's corresponding reflection
     * resultant.
     */
    public void setReflectionMap() {
    }

    @Override
    public String toString() {
        return lastUsedDir;
    }
}
