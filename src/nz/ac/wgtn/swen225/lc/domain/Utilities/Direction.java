package nz.ac.wgtn.swen225.lc.domain.Utilities;

public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0)
    ;

    private Location location;

    public Location act(Location loc) { return location.add(loc); }
    Direction(int x, int y ) { location = new Location(x, y); }
}
