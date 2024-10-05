package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.Collection;

/**
 * A game character that moves around.
 * @author Yee Li, Maria Louisa Carla Parinas
 */
public interface Actor  {

    /**
     * @return current location of Actor
     */
    Location getLocation();

    /**
     * @return current Direction the Actor is facing
     */
    Direction getActorFacing();
    void setActorFacing(Direction d);


    default boolean attemptMove(Direction direction, GameBoard gameBoard) {
        setActorFacing(direction);

        Location nextLocation = direction.act(this.getLocation());

        if (!locationIsValid(direction.act(this.getLocation()), gameBoard)) {
            //GameBoard.domainLogger.log(Level.INFO, "Player tried to move to invalid location:" + nextLocation);
            return false;
        }

        var nextTile = findTileInSpecificLocation(gameBoard, nextLocation);
        var currentTile = findTileInSpecificLocation(gameBoard, this.getLocation());
        doMove(direction, gameBoard, currentTile, nextTile);
        return true;
    }

    /**
     * Performs the move of the actor
     * @param direction direction actor is facing
     * @param gameBoard game board of current level
     */
    void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next);

    default void actOnTile(Direction dir, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {
        current.onExit(this);
        next.onEntry(this);
        if(!(next.item instanceof TeleportItem)) {
            updateActorLocation(next.location);
        }
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
