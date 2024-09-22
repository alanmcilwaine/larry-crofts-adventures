package nz.ac.wgtn.swen225.lc.domain.Utilities;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;

import java.util.List;

public class GameBoardBuilder {
    private List<List<Tile<Item>>> board;

    private Player player;

    private List<Robot> robots;

    private int timeLeft;

    private int level;

    private int width;

    private int height;

    private int totalTreasure;

    public GameBoard build() {
        Util.checkNegative(List.of(timeLeft, level, width, height, totalTreasure));
        Util.checkNull(List.of(board, player));
        return new GameBoard(this);
    }

    public GameBoardBuilder addBoard(List<List<Tile<Item>>> board) {
        Util.checkNull(board, String.format("%s is null.", "board" ));
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

    public GameBoardBuilder setLevel(int level) {
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
