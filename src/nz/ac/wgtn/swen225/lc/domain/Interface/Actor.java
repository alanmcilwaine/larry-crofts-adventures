package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.Collection;

/**
 * A game character that moves around.
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

    /**
     * Performs the move of the actor
     * @param direction direction actor is facing
     * @param gameBoard gameboard of current level
     */
    void doMove(Direction direction, GameBoard gameBoard);

    default void actOnTile(Direction dir, GameBoard gameBoard, Tile current, Tile next) {
        current.onExit(this);
        next.onEntry(this);
        updateActorLocation(next.location);
    }

    /**
     * Updates current actor's location to new location
     * @param location new location to update with
     */
    void updateActorLocation(Location location);

    /**
     * Finds the tile in the specified location
     * @param gameBoard current gameBoard
     * @param targetLocation location to find
     * @return tile in that location
     */
    default Tile<Item> findTileInSpecificLocation(GameBoard gameBoard, Location targetLocation) {
        return gameBoard.getGameState().board().stream()
                .flatMap(Collection::stream)
                .filter(x->x.location.equals(targetLocation))
                .toList()
                .getFirst();
    }

    /**
     * Checks if the given location is not out of bounds
     * @param location location to check
     * @param gameBoard current GameBoard
     * @return true if it's valid, false if not
     */
    default boolean locationIsValid(Location location, GameBoard gameBoard) {
        return location.x() >= 0 && location.x() < gameBoard.getWidth() &&
                location.y() >= 0 && location.y() < gameBoard.getHeight();
    }

}
