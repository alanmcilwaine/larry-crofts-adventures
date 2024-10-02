package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovableBoxTest {


  @Test
  public void boxPushOpen() {
    var gameboard = Mock.getGameBoard();

    var player = gameboard.getGameState().player();

    gameboard.action(Direction.DOWN);
    gameboard.action(Direction.DOWN);
    gameboard.action(Direction.LEFT);

    //at this point the left of the player should have a movable box
    gameboard.action(Direction.LEFT);
    //assertEquals(player.findTileInSpecificLocation(gameboard, new Location(1, 2)).item, new MovableBox(new Location(1, 2)))
    assertEquals(new Location(2, 2), player.getLocation());

    gameboard.action(Direction.LEFT);
    assertEquals(player.getLocation(), new Location(1, 2));
  }

  @Test
  public void boxPushBlocked() {
    var gameboard = Mock.getGameBoard();

    var player = gameboard.getGameState().player();

    gameboard.action(Direction.DOWN);
    gameboard.action(Direction.DOWN);
    gameboard.action(Direction.LEFT);

    //at this point the left of the player should have a movable box
    gameboard.action(Direction.LEFT);
    gameboard.action(Direction.LEFT);
    gameboard.action(Direction.LEFT);

    // player should still be at same position
    assertEquals(new Location(1, 2), player.getLocation());
  }

}
