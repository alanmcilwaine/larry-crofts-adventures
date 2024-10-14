package nz.ac.wgtn.swen225.lc.domain.Utilities;

/**
 * Represents the direction that an actor may be facing. Options are NONE, UP, DOWN, LEFT, RIGHT.
 * Each direction has a Location field, and is able to update the actors position using act(Location)
 *
 * @author Carla Parinas 300653631
 */
public enum Direction {
    NONE(0,0),
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0)
    ;

    private final Location location;

    /**
     * Returns what would be the resulting location if a location were to act under this direction.
     * @param loc location to update
     * @return updated location
     */
    public Location act(Location loc) { return location.add(loc); }

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     */
    Direction(int x, int y ) { location = new Location(x, y); }
}
