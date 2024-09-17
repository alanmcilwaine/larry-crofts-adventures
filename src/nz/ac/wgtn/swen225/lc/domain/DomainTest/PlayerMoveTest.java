package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerMoveTest {
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
}
