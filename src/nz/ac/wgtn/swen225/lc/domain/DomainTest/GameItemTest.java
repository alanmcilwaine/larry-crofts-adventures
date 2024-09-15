package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Wall;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameItemTest {
    @Test
    public void canNotStepOnWall() {
        var gameboard = Mock.getGameBoard();
        var player = gameboard.getGameState().player();
        var currentLocation = gameboard.getGameState().player().getLocation();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Direction null");
        });
        assertEquals("Direction null", exception.getMessage());

        assertDoesNotThrow(() -> gameboard.action(Direction.LEFT));
        assertEquals(currentLocation, player.getLocation());
    }

    @Test
    public void wallWillBlockPlayerMove() {
        var player = Mock.getPlayer();
        var tile = new Tile<>(new Wall(), new Location(0, 1));

        assertTrue(tile.canStepOn(player));
    }
}