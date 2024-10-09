package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Mirror;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Orientation;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    var player = gameboard.getGameState().player();
    LaserSource track = new LaserSource(Direction.RIGHT, true, 0, 5);
    gameboard.getBoard().get(0).get(5).item = track;
    gameboard.addLaserSourceAtLocation(track);

    gameboard.action(Direction.NONE);

    Map<Location, String> lasers = track.getLasers();

    checkLocation(lasers, new Location(5,5), "horizontal");
    checkLocation(lasers, new Location(4,5), "horizontal");
    checkLocation(lasers, new Location(3,5), "horizontal");
    checkLocation(lasers, new Location(2,5), "horizontal");
    checkLocation(lasers, new Location(1,5), "horizontal");

  }

  @Test
  public void testDownFacing() {
    var gameboard = Mock.getGameBoard();
    var player = gameboard.getGameState().player();
    LaserSource track = new LaserSource(Direction.DOWN, true, 0, 5);
    gameboard.getBoard().get(0).get(5).item = track;
    gameboard.addLaserSourceAtLocation(track);

    gameboard.action(Direction.NONE);

    Map<Location, String> lasers = track.getLasers();

    checkLocation(lasers, new Location(0,4), "vertical");
    checkLocation(lasers, new Location(0,3), "vertical");
    checkLocation(lasers, new Location(0,2), "vertical");
    checkLocation(lasers, new Location(0,1), "vertical");
    checkLocation(lasers, new Location(0,0), "vertical");
  }

  @Test
  public void testMirrorWorking() {
    var gameboard = Mock.getGameBoard();
    var player = gameboard.getGameState().player();
    LaserSource track = new LaserSource(Direction.RIGHT, true, 0, 5);
    gameboard.getBoard().get(0).get(5).item = track;
    gameboard.addLaserSourceAtLocation(track);

    Mirror mTrack = new Mirror(Orientation.BOTTOMLEFTFACING, 5,5);
    gameboard.getGameState().boxes().add(mTrack);

    gameboard.action(Direction.NONE);

    Map<Location, String> lasers = track.getLasers();

    checkLocation(lasers, new Location(1,5), "horizontal");
    checkLocation(lasers, new Location(2,5), "horizontal");
    checkLocation(lasers, new Location(3,5), "horizontal");
    checkLocation(lasers, new Location(4,5), "horizontal");
    checkLocation(lasers, new Location(5,5), "horizontal");

    // check if mirror reflects
    checkLocation(lasers, new Location(5,4), "vertical");
    checkLocation(lasers, new Location(5,3), "vertical");
    checkLocation(lasers, new Location(5,2), "vertical");
    checkLocation(lasers, new Location(5,1), "vertical");
    checkLocation(lasers, new Location(5,0), "vertical");
  }

  @Test
  public void doorWorking() {

  }

}
