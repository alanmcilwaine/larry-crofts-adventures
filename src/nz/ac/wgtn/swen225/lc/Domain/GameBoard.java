package nz.ac.wgtn.swen225.lc.Domain;

import nz.ac.wgtn.swen225.lc.Domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.Domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.Direction;

import java.util.List;

public class GameBoard {
    public List<List<Tile<Item>>> getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public void setRobots(List<Robot> robots) {
        this.robots = robots;
    }

    private final List<List<Tile<Item>>> board;

    private Player player;

    private List<Robot> robots;

    private final int TimeLimit;

    public final int width;

    public final int height;

    private GameBoard(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int timeLimit, int width, int height) {
        this.board = board;
        this.player = player;
        this.robots = robots;
        TimeLimit = timeLimit;
        this.width = width;
        this.height = height;
    }

    public static GameBoard of(List<List<Tile<Item>>> board, Player player, List<Robot> robots) {
        return new GameBoard(board, player, robots, 10, 0, 0);
    }

    public void playerMove(Direction direction, GameBoard gameBoard) {
        player.prepareMove(direction, gameBoard);
        updateGameState();
    }

    public void updateGameState() {
    }

    public GameState getGameState() {
        return new GameState(board, player, robots, 10, 10);
    }
}
