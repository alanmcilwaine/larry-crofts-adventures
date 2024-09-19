package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameItemTest {

    private final Location testLocation = new Location(1, 1);

    /* Wall item test */
    @Test
    public void wallWillBlockPlayerMove() {
        var tile = new Tile<>(new Wall(), testLocation);

        assertFalse(tile.canStepOn(Mock.getPlayer()));
    }

    @Test
    public void stepOnWallThrowException() {
        var tile = new Tile<>(new Wall(), testLocation);

        assertThrows(IllegalArgumentException.class, () -> {
            tile.onEntry(Mock.getPlayer());
        });
    }

    /* Key item test */
    @Test
    public void playerShouldPickupKey() {
        var key = new Key(ItemColor.RED);
        var tile = new Tile<>(key, testLocation);
        var player = Mock.getPlayer();

        assertTrue(tile.canStepOn(Mock.getPlayer()));
        assertInstanceOf(Key.class, tile.item);

        tile.onEntry(player);

        assertTrue(player.getTreasure().contains(key));
        assertEquals(1, player.getTreasure().size());
        assertInstanceOf(NoItem.class, tile.item);
    }

    /* NoItem item test */
    @Test
    public void playerCanMoveToFreeTile() {
        var tile = new Tile<>(new NoItem(), testLocation);

        assertTrue(tile.canStepOn(Mock.getPlayer()));
        assertDoesNotThrow(() -> tile.onEntry(Mock.getPlayer()));
    }

    /* Treasure item test */
    @Test
    public void playerShouldPickupTreasure() {
        var treasure = new Treasure();
        var tile = new Tile<>(treasure, testLocation);
        var player = Mock.getPlayer();

        assertTrue(tile.canStepOn(player));
        assertInstanceOf(Treasure.class, tile.item);

        tile.onEntry(player);

        assertTrue(player.getTreasure().contains(treasure));
        assertEquals(1, player.getTreasure().size());
        assertInstanceOf(NoItem.class, tile.item);
    }

    /* Locked door item test */

    @Test
    public void lockedDoorShouldBlockPlayerWithoutKey() {
        var lockedDoor = new LockedDoor(ItemColor.BLUE);
        var tile = new Tile<>(lockedDoor, testLocation);
        var player = Mock.getPlayer();

        assertFalse(tile.canStepOn(player));
    }

    @Test
    public void lockedDoorShouldBlockPlayerWithWrongKey() {
        var lockedDoor = new LockedDoor(ItemColor.BLUE);
        var tile = new Tile<>(lockedDoor, testLocation);
        var player = Mock.getPlayer();

        player.addTreasure(new Key(ItemColor.RED));

        assertFalse(tile.canStepOn(player));
    }

    @Test
    public void lockedDoorShouldNotBlockPlayerWithRightKey() {
        var lockedDoor = new LockedDoor(ItemColor.BLUE);
        var tile = new Tile<>(lockedDoor, testLocation);
        var player = Mock.getPlayer();

        player.addTreasure(new Key(ItemColor.BLUE));

        assertTrue(tile.canStepOn(player));
    }

    @Test
    public void openLockedDoorWillConsumeKey() {
        var lockedDoor = new LockedDoor(ItemColor.BLUE);
        var tile = new Tile<>(lockedDoor, testLocation);
        var player = Mock.getPlayer();
        var key = new Key(ItemColor.BLUE);

        assertEquals("LockedDoorBLUE", tile.getItemOnTile());

        player.addTreasure(key);

        assertTrue(tile.canStepOn(player));
        tile.onEntry(player);

        assertFalse(player.getTreasure().contains(key));
        assertInstanceOf(UnLockedDoor.class, tile.item);
    }

    /* Unlocked Door item test */
    @Test
    public void unLockedDoorIsFreeToPass() {
        var unLockedDoor = new UnLockedDoor(ItemColor.BLUE);
        var tile = new Tile<>(unLockedDoor, testLocation);
        var player = Mock.getPlayer();

        assertTrue(tile.canStepOn(player));
    }

    /* LockedExit item test */
    @Test
    public void lockedExitBlocksPlayer() {
        var lockedExit = new LockedExit();
        var tile = new Tile<>(lockedExit, testLocation);
        var player = Mock.getPlayer();

        assertFalse(tile.canStepOn(player));
    }
    @Test
    public void lockedExitBecomesUnlockedUponFullTreasure() {
        GameBoard gameBoard = Mock.getGameBoard();
        GameBoard.setTotalTreasure(0);
        assertInstanceOf(LockedExit.class, gameBoard.getBoard().getFirst().get(3).item);

        gameBoard.notifyObservers();

        assertInstanceOf(Exit.class, gameBoard.getBoard().getFirst().get(3).item);
    }

    @Test
    public void lockedExitBecomesUnlockedUponFullTreasure3() {
        GameBoard gameBoard = Mock.getGameBoard();
        GameBoard.setTotalTreasure(3);
        Player player = gameBoard.getGameState().player();

        assertInstanceOf(LockedExit.class, gameBoard.getBoard().getFirst().get(3).item);

        var ls = List.of(new Treasure(), new Treasure(), new Treasure());
        ls.forEach(player::addTreasure);
        assertEquals(ls.size(), 3);

        gameBoard.notifyObservers();

        assertInstanceOf(Exit.class, gameBoard.getBoard().getFirst().get(3).item);
    }

    /* Exit item test */
    @Test
    public void gameMovesToNextLevelOnEntry() {

    }
    @Test
    public void exitIsAvailableUponFullTreasure(){
        var exit = new Exit();
        var tile = new Tile<>(exit, testLocation);
        var player = Mock.getPlayer();

        assertTrue(tile.canStepOn(player));
        tile.onEntry(player);
    }

    /* Info item test */
    @Test
    public void infoToggleOnEntryExit(){
        var info = new Info("info");
        var tile = new Tile<>(info,testLocation);
        var player = Mock.getPlayer();

        assertFalse(player.isShowPlayerInfo());

        tile.onEntry(player);
        assertTrue(player.isShowPlayerInfo());
        tile.onExit(player);
        assertFalse(player.isShowPlayerInfo());
        tile.onEntry(player);
        assertTrue(player.isShowPlayerInfo());
    }
}