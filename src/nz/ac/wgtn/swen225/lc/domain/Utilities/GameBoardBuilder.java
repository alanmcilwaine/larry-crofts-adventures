package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

import java.util.List;
import java.util.Map;

/**
 * A game board builder.
 * All entities on game board shall meet certain rules.
 */
public class GameBoardBuilder {
    private List<List<Tile<Item>>> board;

    private Player player;

    private List<Robot> robots;

    private int timeLeft = -1;

    private int level = -1;

    private int width = -1;

    private int height = -1;

    private int totalTreasure = -1;

    public GameBoard build() {
        Util.checkNegative(Map.of("timeLeft",timeLeft,
                "level",level,
                 "width",width,
                "height",height,
                "totalTreasure",totalTreasure));
        Util.checkNull(List.of(board, player));
        return new GameBoard(this);
    }

    public GameBoardBuilder addBoard(List<List<Tile<Item>>> board) {
        Util.checkNull(board, String.format("%s is null.", "board"));
        this.board = board;
        return this;
    }

    public GameBoardBuilder addPlayer(Player player) {
        Util.checkNull(player, String.format("%s is null.", "player"));
        this.player = player;
        return this;
    }

    public GameBoardBuilder addRobots(List<Robot> robots) {
        Util.checkNull(robots, String.format("%s is null.", "robots"));
        this.robots = robots;
        return this;
    }

    public GameBoardBuilder addTimeLeft(int timeLeft) {
        Util.checkNegative("Time Left", timeLeft);
        this.timeLeft = timeLeft;
        return this;
    }

    public GameBoardBuilder addBoardSize(int width, int height) {
        Util.checkNegative("Width", width);
        Util.checkNegative("Height", height);

        this.width = width;
        this.height = height;
        return this;
    }

    public GameBoardBuilder addTreasure(int totalTreasure) {
        Util.checkNegative("Total Treasure", totalTreasure);
        this.totalTreasure = totalTreasure;
        return this;
    }

    public GameBoardBuilder addLevel(int level) {
        Util.checkNegative("Level", level);
        this.level = level;
        return this;
    }

    public List<List<Tile<Item>>> getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getLevel() {
        return level;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTotalTreasure() {
        return totalTreasure;
    }
}
