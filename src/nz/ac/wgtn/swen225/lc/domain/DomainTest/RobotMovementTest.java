package nz.ac.wgtn.swen225.lc.domain.DomainTest;
import nz.ac.wgtn.swen225.lc.domain.GameActor.KillerRobot;
import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ActorPath;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nz.ac.wgtn.swen225.lc.domain.DomainTest.Mock.addBoxAtLocation;
import static nz.ac.wgtn.swen225.lc.domain.DomainTest.Mock.addRobotAtLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RobotMovementTest {

  public void advanceOneDelay(GameBoard gameBoard, Location expected, Robot track) {
    for(int i = 0 ; i < 12 ; i++) {
      gameBoard.action(Direction.NONE);
    }
    assertEquals(expected, track.getLocation());
  }

  // TODO: FIX THESE TESTS

  @Test
  public void robotBasicMovement() {
    GameBoard gameBoard = Mock.getGameBoard();

    Player player = gameBoard.getGameState().player();
    addRobotAtLocation(gameBoard.getRobots(), 3,5);
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

  // TODO: Fix this interaction
  @Test
  public void robotObstacleInteraction() {
    GameBoard gameBoard = Mock.getGameBoard();

    Player player = gameBoard.getGameState().player();
    addBoxAtLocation(gameBoard.getBoxes(),0,0);
    List<Robot> robots = gameBoard.getGameState().robots();
    Robot track = robots.getFirst();

    // blocked by wall
    track.setActorFacing(Direction.RIGHT);
    gameBoard.action(Direction.NONE);
    gameBoard.action(Direction.NONE);
    assertEquals(track.getLocation(), new Location(1, 4));// stay in same position

    // can go through opened door
    track.setActorFacing(Direction.DOWN);
    gameBoard.action(Direction.NONE);
    assertEquals(track.getLocation(), new Location(1,3));

    // can't go through lock door
    track.setActorFacing(Direction.RIGHT);
    gameBoard.action(Direction.NONE);
    gameBoard.action(Direction.NONE);
    gameBoard.action(Direction.NONE);
    //TODO, not pass everytime.
    assertEquals(track.getLocation(), new Location(2, 3)); // stay in same position
  }

//  @Test
//  public void robotCantPickItems() {
//    GameBoard gameBoard = Mock.getGameBoard();
//
//    Player player = gameBoard.getGameState().player();
//    gameBoard.addRobotAtLocation(4,2);
//    List<Robot> robots = gameBoard.getGameState().robots();
//    Robot track = robots.getFirst();
//
//    // can't pick up the treasure
//    advanceOneDelay(gameBoard, new Location(3,2), track);
//
//    assert gameBoard.getGameState().totalTreasure() == 1; // item was not picked up
//
//  }

//  @Test
//  public void robotKillPlayer2() {
//    GameBoard gameBoard = Mock.getGameBoard();
//    Player player = gameBoard.getGameState().player();
//    gameBoard.addRobotAtLocation(4,3);
//    List<Robot> robots = gameBoard.getGameState().robots();
//    Robot track = robots.getFirst();
//
//    // robot killing player
//    track.setActorFacing(Direction.UP);
//    assertEquals(Player);
//  }
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
