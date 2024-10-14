package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the MovableBox and as well as it's interactions
 *
 * @author Carla Parinas 300653631
 */
public class MovableBoxTest {

    @Test
    public void boxPushOpen() {
        var gameboard = Mock.getGameBoard();
        gameboard.getGameState().boxes().add(new MovableBox(2, 2));

        List<MovableBox> boxes = gameboard.getGameState().boxes();
        MovableBox track = boxes.getFirst();
        var player = gameboard.getGameState().player();

        // assert correct spawn
        assertEquals(new Location(2, 2), track.getLocation());

        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.LEFT);

        //at this point the left of the player should have a movable box
        gameboard.action(Direction.LEFT);
        //assertEquals(player.findTileInSpecificLocation(gameboard, new Location(1, 2)).item, new
        // MovableBox(new Location(1, 2)))
        assertEquals(new Location(2, 2), player.getLocation());
        assertEquals(new Location(1, 2), track.getLocation());

        gameboard.action(Direction.LEFT);
        assertEquals(new Location(1, 2), player.getLocation());
        assertEquals(new Location(0, 2), track.getLocation());
    }

    @Test
    public void boxPushBlocked() {
        var gameboard = Mock.getGameBoard();
        gameboard.getGameState().boxes().add(new MovableBox(2, 2));

        List<MovableBox> boxes = gameboard.getGameState().boxes();
        MovableBox track = boxes.getFirst();
        var player = gameboard.getGameState().player();

        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.LEFT);

        //at this point the left of the player should have a movable box
        gameboard.action(Direction.LEFT);
        gameboard.action(Direction.LEFT);
        gameboard.action(Direction.LEFT);

        // player should still be at same position
        assertEquals(new Location(1, 2), player.getLocation());
        assertEquals(new Location(0, 2), track.getLocation());
    }

    // TODO : Add more tests for items
    @Test
    public void shouldNotPushIntoItems() {

    }

    @Test
    public void shouldNotPushIntoBox() {
    }

}
