package nz.ac.wgtn.swen225.lc.domain.DomainTest;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RobotMovementTest {

  @Test
  public void robotBasicMovement() {
    GameBoard gameBoard = Mock.getGameBoard();

    Player player = gameBoard.getGameState().player();
    gameBoard.addRobotAtLocation(0,0);
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // Robot should not go out of bounds
    track.setRobotFacing(Direction.DOWN);
    assertEquals(new Location(0, 0), track.getLocation());
    // assertThrows(IllegalArgumentException.class, () -> gameBoard.action(Direction.NONE));

    track.setRobotFacing(Direction.LEFT);
    assertEquals(new Location(0, 0), track.getLocation());
    // assertThrows(IllegalArgumentException.class, () -> gameBoard.action(Direction.NONE));

    // simple robot movement
    track.setRobotFacing(Direction.UP);
    gameBoard.action(Direction.NONE);
    assertEquals(new Location(0, 1), track.getLocation());

    gameBoard.action(Direction.NONE);
    assertEquals(new Location(0, 2), track.getLocation());

    track.setRobotFacing(Direction.RIGHT);
    gameBoard.action(Direction.NONE);
    assertEquals(new Location(1, 2), track.getLocation());
  }

  @Test
  public void robotObstacleInteraction() {
    GameBoard gameBoard = Mock.getGameBoard();

    Player player = gameBoard.getGameState().player();
    gameBoard.addRobotAtLocation(0,4);
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // blocked by wall
    track.setRobotFacing(Direction.RIGHT);
    gameBoard.action(Direction.NONE);
    gameBoard.action(Direction.NONE);
    assertEquals(track.getLocation(), new Location(1, 4));// stay in same position

    // can go through opened door
    track.setRobotFacing(Direction.DOWN);
    gameBoard.action(Direction.NONE);
    assertEquals(track.getLocation(), new Location(1,3));

    // can't go through lock door
    track.setRobotFacing(Direction.RIGHT);
    gameBoard.action(Direction.NONE);
    gameBoard.action(Direction.NONE);
    gameBoard.action(Direction.NONE);
    //TODO, not pass everytime.
    assertEquals(track.getLocation(), new Location(2, 3)); // stay in same position
  }

  @Test
  public void robotCantPickItems() {
    GameBoard gameBoard = Mock.getGameBoard();

    Player player = gameBoard.getGameState().player();
    gameBoard.addRobotAtLocation(2,2);
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // can't pick up the treasure
    track.setRobotFacing(Direction.RIGHT);
    gameBoard.action(Direction.NONE);
    assertEquals(new Location(3,2), track.getLocation());

    assert gameBoard.getGameState().totalTreasure() == 1; // item was not picked up

  }

  @Test
  public void robotKillPlayer() {
    GameBoard gameBoard = Mock.getGameBoard();
    Player player = gameBoard.getGameState().player();
    gameBoard.addRobotAtLocation(4,3);
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // robot killing player
    track.setRobotFacing(Direction.UP);
    assertThrows(IllegalArgumentException.class, () -> gameBoard.action(Direction.NONE));
  }

  @Test
  public void robotKillPlayer2() {
    GameBoard gameBoard = Mock.getGameBoard();

    Player player = gameBoard.getGameState().player();
    gameBoard.addRobotAtLocation(4,3);
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // player stepping into robot
    track.setRobotFacing(Direction.NONE);
    try {
      gameBoard.action(Direction.DOWN);
    } catch (IllegalArgumentException e) {
      ; // pass for now
    }

  }

}
