package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class MovableBox implements Actor {
    private Location location;
    private Direction wallFacing = Direction.NONE; // set to none on initialization

    public MovableBox(int x, int y) {
        this.location = new Location(x, y);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Direction getActorFacing() {
        return this.wallFacing;
    }

    @Override
    public void setActorFacing(Direction d) {
        this.wallFacing = d;
    }

    @Override
    public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {
        actOnTile(direction, gameBoard, current, next);
    }

    @Override
    public void updateActorLocation(Location location) {
        this.location = new Location(location.x(), location.y());
    }

    @Override
    public String toString() {
        return "MovableBox";
    }
}
