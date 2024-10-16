package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Crate;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Mirror;
import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Button;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserInput;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Tube;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Orientation;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for lasers and laser interaction
 *
 * @author Carla Parinas 300653631
 */
public class LaserTest {

    public void checkLocation(Map<Location, String> map, Location expected, String orientation) {
        String check = map.remove(expected);
        if (check == null) {
            throw new AssertionError("No Location: " + expected);
        } else {
            assertEquals(check, orientation);
        }
    }

    @Test
    public void testRightFacing() {
        var gameboard = Mock.getGameBoard();
        LaserSource track = new LaserSource(Direction.RIGHT, true, 0, 5);
        gameboard.getBoard().get(0).get(5).item = track;
        gameboard.getGameState().laserSources().add(track);

        gameboard.action(Direction.NONE);

        Map<Location, String> lasers = track.getLasers();

        checkLocation(lasers, new Location(5, 5), "horizontal");
        checkLocation(lasers, new Location(4, 5), "horizontal");
        checkLocation(lasers, new Location(3, 5), "horizontal");
        checkLocation(lasers, new Location(2, 5), "horizontal");
        checkLocation(lasers, new Location(1, 5), "horizontal");

    }

    @Test
    public void testDownFacing() {
        var gameboard = Mock.getGameBoard();
        LaserSource track = new LaserSource(Direction.DOWN, true, 0, 5);
        gameboard.getBoard().get(0).get(5).item = track;
        gameboard.getGameState().laserSources().add(track);

        gameboard.action(Direction.NONE);

        Map<Location, String> lasers = track.getLasers();

        checkLocation(lasers, new Location(0, 4), "vertical");
        checkLocation(lasers, new Location(0, 3), "vertical");
        checkLocation(lasers, new Location(0, 2), "vertical");
        checkLocation(lasers, new Location(0, 1), "vertical");
        checkLocation(lasers, new Location(0, 0), "vertical");
    }

    @Test
    public void testItemsInteraction() {
        var gameboard = Mock.getGameBoard();
        LaserSource track = new LaserSource(Direction.RIGHT, true, 0, 5);
        gameboard.getBoard().get(5).get(0).item = track;
        gameboard.getGameState().laserSources().add(track);

        // add mirror
        Mirror mTrack = new Mirror(Orientation.TWO, 5, 5);
        gameboard.getGameState().boxes().add(mTrack);

        // add box
        MovableBox bTrack = new MovableBox(5, 0);
        gameboard.getGameState().boxes().add(bTrack);

        // add crate
        Crate cTrack = new Crate(5, 2);
        gameboard.getGameState().boxes().add(cTrack);

        // add tube
        gameboard.getBoard().get(5).get(3).item = new Tube();


        gameboard.action(Direction.NONE);

        Map<Location, String> lasers = track.getLasers();

        // lasersource works
        checkLocation(lasers, new Location(1, 5), "horizontal");
        checkLocation(lasers, new Location(2, 5), "horizontal");

        // tube works and passes
        checkLocation(lasers, new Location(3, 5), "horizontal");
        checkLocation(lasers, new Location(4, 5), "horizontal");

        // mirror works and right orientation
        checkLocation(lasers, new Location(5, 5), "Reflectrightup");

        // check if mirror reflects
        checkLocation(lasers, new Location(5, 4), "vertical");
        checkLocation(lasers, new Location(5, 3), "vertical");

        // laser destroys the create and passes through
        checkLocation(lasers, new Location(5, 2), "vertical");
        checkLocation(lasers, new Location(5, 1), "vertical");
        assertFalse(gameboard.getGameState().boxes().contains(cTrack));


        // block
        assertThrows(AssertionError.class, () -> checkLocation(lasers, new Location(5, 0),
                "vertical"));
    }

    @Test
    public void playerDiesFromLaser() {
        var gameboard = Mock.getGameBoard();
        var player = gameboard.getGameState().player();
        LaserSource track = new LaserSource(Direction.DOWN, true, 5, 5);
        gameboard.getBoard().get(0).get(5).item = track;
        gameboard.getGameState().laserSources().add(track);

        gameboard.action(Direction.NONE);

        gameboard.action(Direction.RIGHT);
        gameboard.action(Direction.RIGHT);

        assert player.isDead();
    }

    @Test
    public void laserInputWorks() {
        var gameboard = Mock.getGameBoard();

        Button track = new LaserInput();
        Tile<Item> tileTrack = gameboard.getGameState().board().get(2).get(5);
        tileTrack.item = track;

        LaserSource ltrack = new LaserSource(Direction.DOWN, true, 5, 5);
        gameboard.getBoard().get(0).get(5).item = ltrack;
        gameboard.getGameState().laserSources().add(ltrack);

        assert !track.isPressed;

        gameboard.configureButtons();

        gameboard.action(Direction.NONE);
        assert track.isPressed;

        // TODO: CHECK IF INTERACTION WITH NEARBY OBJECT WORKS
    }

}
