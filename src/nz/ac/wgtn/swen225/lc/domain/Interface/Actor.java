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

    Location getLocation();

    Direction getActorFacing();

    void doMove(Direction direction, GameBoard gameBoard);

    void updateActorLocation(Location location);

    default Tile<Item> findTileInSpecificLocation(GameBoard gameBoard, Location targetLocation) {
        return gameBoard.getGameState().board().stream()
                .flatMap(Collection::stream)
                .filter(x->x.location.equals(targetLocation))
                .toList()
                .getFirst();
    }

    default boolean locationIsValid(Location location, GameBoard gameBoard) {
        return location.x() >= 0 && location.x() < gameBoard.getWidth() &&
                location.y() >= 0 && location.y() < gameBoard.getHeight();
    }

}
