package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Exit;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedExit;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerMoveTest {
    @Test
    public void playerMoveTestsSet01() {
        //setup test
        var gameboard = Mock.getGameBoard();

        var player = gameboard.getGameState().player();
        var playerStartLocation = gameboard.getGameState().player().getLocation();

        //should not take null.
        assertEquals(playerStartLocation, new Location(4, 4));
        Exception exception = assertThrows(NullPointerException.class, () -> {
            gameboard.action(null);
        });
        assertEquals("Direction is null", exception.getMessage());

        assertEquals(player.getLocation(), new Location(4, 4));

        gameboard.action(Direction.UP);
        //can't step on wall
        gameboard.action(Direction.LEFT);
        assertEquals(player.getLocation(), new Location(4, 4));

        //go collect treasure
        gameboard.action(Direction.DOWN);
        assertEquals(player.getLocation(), new Location(4, 3));

        gameboard.action(Direction.DOWN);
        assertEquals(player.getLocation(), new Location(4, 2));

        assertTrue(gameboard.getBoard()
                .stream()
                .flatMap(Collection::stream)
                .noneMatch(e -> e.item instanceof Exit));
        assertTrue(gameboard.getBoard()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(e -> e.item instanceof LockedExit));

        gameboard.action(Direction.LEFT);
        assertEquals(player.getLocation(), new Location(3, 2));
        assertEquals(player.getTreasure().size(), 1);

        //exit becomes available after collect all treasures
        assertTrue(gameboard.getBoard()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(e -> e.item instanceof Exit));
        assertTrue(gameboard.getBoard()
                .stream()
                .flatMap(Collection::stream)
                .noneMatch(e -> e.item instanceof LockedExit));

        //go the locked red door
        gameboard.action(Direction.DOWN);
        assertEquals(player.getTreasure().size(), 2);
        assertEquals(player.getLocation(), new Location(3, 1));

        gameboard.action(Direction.UP);
        assertEquals(player.getLocation(), new Location(3, 2));
        //has blue key, but block by red closed door
        gameboard.action(Direction.UP);
        assertEquals(player.getLocation(), new Location(3, 2));
        //go collect red key
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.LEFT);
        gameboard.action(Direction.LEFT);
        //go back open the door
        gameboard.action(Direction.UP);
        gameboard.action(Direction.UP);
        //pass red open door, without losing key
        assertEquals(player.getLocation(), new Location(1, 3));
        //continue
        gameboard.action(Direction.RIGHT);
        gameboard.action(Direction.RIGHT);
        assertEquals(player.getLocation(), new Location(3, 3));
        assertEquals(player.getTreasure().size(), 2);
        //go to locked blue door
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.LEFT);
        gameboard.action(Direction.LEFT);
        gameboard.action(Direction.DOWN);
        assertEquals(player.getLocation(), new Location(1, 0));
        assertEquals(player.getTreasure().size(), 1);
        //go to info
        assertFalse(player.isShowPlayerInfo());
        gameboard.action(Direction.UP);
        gameboard.action(Direction.RIGHT);
        gameboard.action(Direction.RIGHT);
        gameboard.action(Direction.RIGHT);
        gameboard.action(Direction.DOWN);
        assertTrue(player.isShowPlayerInfo());
        //go to exit
        gameboard.action(Direction.LEFT);
        assertFalse(player.isShowPlayerInfo());
        assertTrue(player.isNextLevel());
    }
}
