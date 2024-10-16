package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.Collection;

/**
 * A game character that moves around.
 *
 * @author Yee Li, Maria Louisa Carla Parinas
 */
public interface Actor {

    /**
     * @return current location of Actor
     */
    Location getLocation();

    /**
     * @return current Direction the Actor is facing
     */
    Direction getActorFacing();

    void setActorFacing(Direction d);

    /**
     * Checks if the actor can attempt a move on the next tile.
     *
     * @param direction the direction the actor is heading in
     * @param gameBoard current gameboard
     * @return true if the actor is able to move, false if not
     */
    default boolean attemptMove(Direction direction, GameBoard gameBoard) {
        setActorFacing(direction);

        Location nextLocation = direction.act(this.getLocation());

        if (!locationIsValid(direction.act(this.getLocation()), gameBoard)) {
            return false;
        }

        var nextTile = findTileInSpecificLocation(gameBoard, nextLocation);
        var currentTile = findTileInSpecificLocation(gameBoard, this.getLocation());

        // checks for a box and if it can move
        MovableBox box = gameBoard.findBoxAtLocation(nextTile.location);

        if (box != null && !box.attemptMove(direction, gameBoard)) {
            return false;
        } else {
            gameBoard.activateLasers();
        }

        if (!nextTile.canStepOn(this) && !(this instanceof Robot)) {
            return false;
        }

        doMove(direction, gameBoard, currentTile, nextTile);
        return true;
    }

    /**
     * Performs the move of the actor
     *
     * @param direction direction actor is facing
     * @param gameBoard game board of current level
     */
    void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next);

    default void actOnTile(Direction dir, GameBoard gameBoard, Tile<Item> current,
                           Tile<Item> next) {
        if (dir.equals(Direction.NONE)) {
            return;
        }
        current.onExit(this);
        next.onEntry(this);
        if (!(next.item instanceof TeleportItem)) {
            updateActorLocation(next.location);
        }
    }

    /**
     * Updates current actor's location to new location
     *
     * @param location new location to update with
     */
    void updateActorLocation(Location location);

    /**
     * Finds the tile in the specified location
     *
     * @param gameBoard      current gameBoard
     * @param targetLocation location to find
     * @return tile in that location
     */
    default Tile<Item> findTileInSpecificLocation(GameBoard gameBoard, Location targetLocation) {
        return gameBoard.getGameState().board().stream()
                .flatMap(Collection::stream)
                .filter(x -> x.location.equals(targetLocation))
                .toList()
                .getFirst();
    }

    /**
     * Checks if the given location is not out of bounds
     *
     * @param location  location to check
     * @param gameBoard current GameBoard
     * @return true if it's valid, false if not
     */
    default boolean locationIsValid(Location location, GameBoard gameBoard) {
        return location.x() >= 0 && location.x() < gameBoard.getGameState().width() &&
                location.y() >= 0 && location.y() < gameBoard.getGameState().height();
    }

    Actor makeNew();

}
