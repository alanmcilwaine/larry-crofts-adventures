package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * All standard game item tests plus
 * Oneway teleport item tests.
 *
 * @author Yee Li
 */
public class GameItemTest {

    private final Location testLocation = new Location(1, 1);

    /* Wall item test */
    @Test
    public void wallWillBlockPlayerMove() {
        var tile = new Tile<>(new Wall(), testLocation);

        assertEquals("Wall", tile.item.toString());
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

        assertEquals("KeyRed", tile.item.toString());
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

        assertEquals("Tile", tile.item.toString());
        assertTrue(tile.canStepOn(Mock.getPlayer()));
        assertDoesNotThrow(() -> tile.onEntry(Mock.getPlayer()));
    }

    /* Treasure item test */
    @Test
    public void playerShouldPickupTreasure() {
        var treasure = new Treasure();
        var tile = new Tile<>(treasure, testLocation);
        var player = Mock.getPlayer();

        assertEquals("Treasure", tile.item.toString());
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

        assertEquals("LockedDoorBlue", tile.item.toString());
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

        assertEquals("LockedDoorBlue", tile.getItemOnTile());

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

        assertEquals("UnlockedDoor", tile.item.toString());
        assertTrue(tile.canStepOn(player));
    }

    /* LockedExit item test */
    @Test
    public void lockedExitBlocksPlayer() {
        var lockedExit = new LockedExit();
        var tile = new Tile<>(lockedExit, testLocation);
        var player = Mock.getPlayer();

        assertEquals("LockedExit", tile.item.toString());
        assertFalse(tile.canStepOn(player));
    }

    @Test
    public void lockedExitBecomesUnlockedUponFullTreasure() {
        GameBoard gameBoard = Mock.getGameBoard();

        assertInstanceOf(LockedExit.class, gameBoard.getBoard().getFirst().get(3).item);

        gameBoard.getGameState().player().addTreasure(new Treasure());
        gameBoard.notifyObservers();

        assertInstanceOf(Exit.class, gameBoard.getBoard().getFirst().get(3).item);
    }

    @Test
    public void lockedExitBecomesUnlockedUponFullTreasure3() {
        GameBoard gameBoard = Mock.getGameBoard();

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
        var exit = new Exit();
        var tile = new Tile<>(exit, testLocation);
        var player = Mock.getPlayer();

        tile.onEntry(player);
        assertEquals("Exit", tile.item.toString());
        assertTrue(player.isNextLevel());
    }

    @Test
    public void exitIsFreeToPass() {
        var exit = new Exit();
        var tile = new Tile<>(exit, testLocation);
        var player = Mock.getPlayer();

        assertTrue(tile.canStepOn(player));
    }

    /* Info item test */
    @Test
    public void infoToggleOnEntryExit() {
        var info = new Info("info");
        var tile = new Tile<>(info, testLocation);
        var player = Mock.getPlayer();

        assertFalse(player.isShowPlayerInfo());

        tile.onEntry(player);
        assertTrue(player.isShowPlayerInfo());
        tile.onExit(player);
        assertFalse(player.isShowPlayerInfo());
        tile.onEntry(player);
        assertTrue(player.isShowPlayerInfo());
        assertEquals("Info", tile.item.toString());
    }

    /*
    Teleport item test
     */
    @Test
    public void teleportWillTransferPlayerToValidTile() {
        //Arrange
        var gameboard = Mock.getGameBoard();
        var destination = new Location(5, 0);
        var player = gameboard.getGameState().player();
        gameboard.getBoard().get(5).get(4).item = new OneWayTeleport(destination);

        //Act
        gameboard.action(Direction.UP);
        //Assert
        assertEquals(player.getLocation(), destination);
        assertEquals("OneWayTeleport", gameboard.getBoard().get(5).get(4).item.toString());
    }

    @Test
    public void teleportWillNotTransferPlayerToInvalidTile() {
        //Arrange
        var gameboard = Mock.getGameBoard();
        var destination = new Location(5, 0);
        var player = gameboard.getGameState().player();

        gameboard.getBoard().get(5).get(4).item = new OneWayTeleport(destination);
        gameboard.getBoard().get(0).get(5).item = new Wall();

        //Act
        gameboard.action(Direction.UP);
        //Assert
        assertEquals(player.getLocation(), new Location(4, 5));
    }

    @Test
    public void teleportMeetGeneralRules() {
        //Arrange
        var gameboard = Mock.getGameBoard();
        var destination = new Location(5, 0);
        var player = gameboard.getGameState().player();
        gameboard.getBoard().get(5).get(4).item = new OneWayTeleport(destination);
        gameboard.getBoard().get(0).get(5).item = new LockedDoor(ItemColor.RED);
        //Act
        gameboard.action(Direction.UP);
        //Assert, player can't be teleported to locked door.
        assertEquals(player.getLocation(), new Location(4, 5));

        //player can't be teleported to locked exit.
        var destination2 = new Location(3, 0);
        gameboard.getBoard().get(5).get(1).item = new OneWayTeleport(destination2);
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.UP);

        assertEquals(player.getLocation(), new Location(4, 5));


        //Player can be teleported to locked door if it has key.
        player.addTreasure(new Key(ItemColor.RED));
        gameboard.action(Direction.DOWN);
        gameboard.action(Direction.UP);

        assertEquals(player.getLocation(), destination);
        assertEquals(player.getTreasure().size(), 0);
    }

    @Test
    public void teleportShouldNotGoInfinite() {
        //Arrange, loop teleport
        var gameboard = Mock.getGameBoard();
        var destination1 = new Location(0, 5);
        var destination2 = new Location(4, 5);
        var player = gameboard.getGameState().player();

        gameboard.getBoard().get(5).get(0).item = new OneWayTeleport(destination2);
        gameboard.getBoard().get(5).get(4).item = new OneWayTeleport(destination1);

        assertThrows(IllegalArgumentException.class, () -> gameboard.action(Direction.UP));
    }

    @Test
    public void relayTeleport() {
        //Arrange, relay teleport
        var gameboard = Mock.getGameBoard();
        var destination1 = new Location(0, 5);
        var destination2 = new Location(3, 5);
        var player = gameboard.getGameState().player();

        gameboard.getBoard().get(5).get(0).item = new OneWayTeleport(destination2);
        gameboard.getBoard().get(5).get(4).item = new OneWayTeleport(destination1);

        assertEquals(player.getLocation(), new Location(4, 4));
        gameboard.action(Direction.UP);
        assertEquals(player.getLocation(), new Location(3, 5));
    }

    @Test
    public void itemDefaultTest() {
        var wall = new Wall();
        var tile = new Tile<>(wall, testLocation);
        var player = Mock.getPlayer();

        assertThrows(IllegalStateException.class, ()->wall.onTouch(player,tile));
    }
}