package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.KillerRobot;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ActorPath;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotMovementTest {

    public void advanceOneDelay(GameBoard gameBoard, Location expected, Robot track) {
        for (int i = 0; i <= ActorPath.getDelay() + 1; i++) {
            gameBoard.action(Direction.NONE);
        }
        assertEquals(expected, track.getLocation());
    }

    // TODO: FIX THESE TESTS

    @Test
    public void robotBasicMovement() {
        GameBoard gameBoard = Mock.getGameBoard();

        gameBoard.getGameState().robots().add(new KillerRobot(3, 5));

        List<Robot> robots = gameBoard.getGameState().robots();
        Robot track = robots.getFirst();

    // Robot should not go out of bounds
    track.setActorFacing(Direction.DOWN);
    assertEquals(new Location(3, 5), track.getLocation());

    track.setActorFacing(Direction.LEFT);
    assertEquals(new Location(3, 5), track.getLocation());

    // simple robot movement

    // set as LEFT TO RIGHT FIRST
    track.getActorPath().resetStepCount();
    advanceOneDelay(gameBoard, new Location(2, 5), track);
    advanceOneDelay(gameBoard, new Location(1, 5), track);
    advanceOneDelay(gameBoard, new Location(0, 5), track);

    // hit road block from here, at this point the robot should have switched it's direction after one delay

    // now going RIGHT to LEFT
    advanceOneDelay(gameBoard, new Location(1,5), track);
    advanceOneDelay(gameBoard, new Location(2,5), track);
    advanceOneDelay(gameBoard, new Location(3,5), track);

  }

  @Test
  public void robotCantPickItems() {
    GameBoard gameBoard = Mock.getGameBoard();

    gameBoard.getGameState().robots().add(new KillerRobot(4,2));
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // can't pick up the treasure
    advanceOneDelay(gameBoard, new Location(2,2), track);

    assert gameBoard.getGameState().totalTreasure() == 1; // item was not picked up
  }

  @Test
  public void robotKillPlayer() {
    GameBoard gameBoard = Mock.getGameBoard();
    Player player = gameBoard.getGameState().player();
    gameBoard.getGameState().robots().add(new KillerRobot(0, 5));
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // robot killing player
    gameBoard.action(Direction.UP);
    gameBoard.action(Direction.UP);

    for(int i=0; i <=100; i++) {
      gameBoard.action(Direction.NONE);
    }

    assert player.isDead();
  }

  @Test
  public void robotKillPlayer2() {
    GameBoard gameBoard = Mock.getGameBoard();
    Player player = gameBoard.getGameState().player();
    gameBoard.getGameState().robots().add(new KillerRobot(0, 5));
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // robot killing player
    gameBoard.action(Direction.UP);
    gameBoard.action(Direction.UP);
    gameBoard.action(Direction.LEFT);
    gameBoard.action(Direction.LEFT);
    gameBoard.action(Direction.LEFT);
    gameBoard.action(Direction.LEFT);

    assert player.isDead();
  }
//
//  @Test
//  public void robotKillPlayer() {
//    GameBoard gameBoard = Mock.getGameBoard();
//
//    Player player = gameBoard.getGameState().player();
//    gameBoard.addRobotAtLocation(4,3);
//    List<Robot> robots = gameBoard.getGameState().robots();
//    Robot track = robots.getFirst();
//
//    // player stepping into robot
//    track.setActorFacing(Direction.NONE);
//    try {
//      gameBoard.action(Direction.DOWN);
//    } catch (IllegalArgumentException e) {
//      ; // pass for now
//    }
//
//  }

}
