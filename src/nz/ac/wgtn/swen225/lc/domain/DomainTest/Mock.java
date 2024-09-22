package nz.ac.wgtn.swen225.lc.domain.DomainTest;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.List;

public class Mock {

    /**
     * Get a mock game board with layout:
     * <br>
     * "f f w w p"
     * <br>
     * "f rod f rcd f"
     * <br>
     * "f f f t f "
     * <br>
     * "f rk f bk f"
     * <br>
     * "f bcd f le i";
     * <br>
     * w is wall, p is player, rod is red opened door,
     * rcd is red closed door, rk is red key, bk is blue key,
     * bcd is black closed door,
     * e is exit, le is locked exit,
     * t is treasure, i is info.
     *
     * @return mocked game board
     */
    public static GameBoard getGameBoard() {
        return constructGameBoard();
    }

    /**
     * Get a new mock player at position 0,0
     *
     * @return a mock player.
     */
    public static Player getPlayer() {
        return new Player(new Location(0, 0));
    }

    public static Player getPlayer(Location location) {
        return new Player(location);
    }

    private static GameBoard constructGameBoard() {
        int width = 5;
        int height = 5;
        int level = 1;
        int timeLeft = 999;

        Player p = getPlayer(new Location(4, 4));

        List<Robot> robots = new ArrayList<>();

        List<List<Tile<Item>>> tiles = constructTiles();
        addItemToTile(tiles);

        return new GameBoardBuilder().addBoard(tiles).addBoardSize(width,height).addTimeLeft(timeLeft)
                .addTreasure(1).addLevel(level).addPlayer(p).addRobots(robots).build();
    }

    private static List<List<Tile<Item>>> constructTiles() {
        List<List<Tile<Item>>> t = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ArrayList<Tile<Item>> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(new Tile<>(new NoItem(), new Location(j, i)));
            }
            t.add(row);
        }
        return t;
    }

    private static void addItemToTile(List<List<Tile<Item>>> l) {
        l.getFirst().get(1).item = new LockedDoor(ItemColor.BLUE);
        l.getFirst().get(3).item = new LockedExit();
        l.getFirst().get(4).item = new Info("Info");

        l.get(1).get(1).item = new Key(ItemColor.RED);
        l.get(1).get(3).item = new Key(ItemColor.BLUE);

        l.get(2).get(3).item = new Treasure();

        l.get(3).get(1).item = new UnLockedDoor(ItemColor.RED);
        l.get(3).get(3).item = new LockedDoor(ItemColor.RED);

        l.get(4).get(2).item = new Wall();
        l.get(4).get(3).item = new Wall();
    }


}
