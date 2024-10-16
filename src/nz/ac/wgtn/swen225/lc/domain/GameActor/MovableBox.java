package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

/**
 * Represents a box that can be moved around by actors. Does not move when there
 * you are pushing the box into another box.
 *
 * @author Carla Parinas 300653631
 */
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
    public boolean attemptMove(Direction direction, GameBoard gameBoard) {
        Location next = direction.act(location);
        MovableBox box = gameBoard.findBoxAtLocation(next);
        Player p = gameBoard.getGameState().player();

        // if there is already a box then don't do anything
        if (box != null) { return false; }
        if (p.getLocation().equals(next)) { p.die(); }

        return Actor.super.attemptMove(direction, gameBoard);
    }

    @Override
    public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current,
                       Tile<Item> next) {
        actOnTile(direction, gameBoard, current, next);
    }

    @Override
    public void updateActorLocation(Location location) {
        this.location = new Location(location.x(), location.y());
    }

    @Override
    public String toString() { return "MovableBox"; }

    @Override
    public Actor makeNew() {
        return new MovableBox(location.x(), location.y());
    }
}
