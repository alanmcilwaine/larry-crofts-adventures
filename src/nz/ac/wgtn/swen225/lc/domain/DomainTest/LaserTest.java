package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LaserTest {

  @Test
  public void testRightFacing() {
    var gameboard = Mock.getGameBoard();
    var player = gameboard.getGameState().player();
    gameboard.getBoard().get(0).get(5).item = new LaserSource(Direction.RIGHT, true, 0 ,5);
    ;
    System.out.println(gameboard.getBoard().get(1).get(5).item.toString());

    // check if the other lasers have been implemented
    assertEquals(gameboard.getBoard().get(1).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(2).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(3).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(4).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(5).get(5).item.toString(), "Laser");

    // check if player dies from touching the laser
    System.out.println(player.getLocation());
    gameboard.action(Direction.RIGHT);
    System.out.println(player.getLocation());
    assertEquals(player.isDead(), true);

    gameboard.getGameState().boxes().add(new MovableBox(5,2));
  }

  @Test
  public void testMovableBoxReset() {
    var gameboard = Mock.getGameBoard();
    var player = gameboard.getGameState().player();
    gameboard.getBoard().get(0).get(5).item = new LaserSource(Direction.RIGHT, true, 0, 5);
    ;
    System.out.println(gameboard.getBoard().get(1).get(5).item.toString());

    // check if the other lasers have been implemented
    assertEquals(gameboard.getBoard().get(1).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(2).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(3).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(4).get(5).item.toString(), "Laser");
    assertEquals(gameboard.getBoard().get(5).get(5).item.toString(), "Laser");

    // check if player dies from touching the laser
    gameboard.action(Direction.DOWN);
    gameboard.action(Direction.DOWN);
    gameboard.action(Direction.LEFT);

    gameboard.getGameState().boxes().add(new MovableBox(4,2));
    gameboard.action(Direction.RIGHT);
    gameboard.action(Direction.RIGHT);

    System.out.println(player.getLocation());

    assertEquals(gameboard.getBoard().get(2).get(5).item.toString(), "Tile");
    assertEquals(gameboard.getBoard().get(3).get(5).item.toString(), "Tile");
    assertEquals(gameboard.getBoard().get(4).get(5).item.toString(), "Tile");
    assertEquals(gameboard.getBoard().get(5).get(5).item.toString(), "Tile");
  }

  @Test
  public void testMirrorWorking() {

  }

  @Test
  public void doorWorking() {

  }

}
