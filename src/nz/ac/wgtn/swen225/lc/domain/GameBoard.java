package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameItem.NoItem;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameBoard {
    private final List<List<Tile<Item>>> board;

    private Player player;

    private final List<Robot> robots;

    private final int timeLeft;

    private final int level;

    public final int width;

    public final int height;

    private GameBoard(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int timeLeft, int level, int width, int height) {
        this.board = board;
        this.player = player;
        this.robots = robots;
        this.timeLeft = timeLeft;
        this.level = level;
        this.width = width;
        this.height = height;
    }

    public static GameBoard of(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int width, int height) {
        return new GameBoard(board, player, robots, 10, 0, width, height);
    }

    private void playerMove(Direction direction, GameBoard gameBoard) {
        player.prepareMove(direction, gameBoard);
    }

    /**
     * Moves all the robots in the level
     */
    private void robotsMove() { robots.forEach(r -> r.update(this)); }

    /**
     * Generate a game board.
     *
     * @param board  game board.
     * @param player player on the board.
     * @param robots robots on the board.
     * @return a game board.
     */
    public static GameBoard of(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int timeLeft, int width, int height, int level) {
        checkValid(timeLeft, width, height, level);
        return new GameBoard(board, player, robots, timeLeft, level,width,height);
    }

    private static void checkValid(int timeLeft, int width, int height, int level) {
        if (timeLeft <= 0 || width < 2 || height < 2 || level < 1) {
            throw new IllegalArgumentException("Invalid game board");
        }
    }

    public List<List<Tile<Item>>> getBoard() { return Collections.unmodifiableList(board); }

    /**
     * Get current game board state.
     *
     * @return GameState
     */
    public GameState getGameState() {
        return new GameState(board, player, robots, timeLeft, level);
    }


    /**
     * Take an action on current board.
     *
     * @param direction the direction player wants to go.
     */
    public void action(Direction direction) {
        if(Objects.isNull(direction)){
            throw new IllegalArgumentException("Direction null");
        }

        robotsMove();
        playerMove(direction, this);

    }

}
