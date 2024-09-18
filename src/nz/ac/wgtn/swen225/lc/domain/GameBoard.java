package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Exit;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Treasure;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;

import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameBoard {
    private List<GameStateObserver> obs = new ArrayList<>();
    private final List<List<Tile<Item>>> board;

    private final Player player;

    private final List<Robot> robots;

    private final int timeLeft;

    private final int level;

    public final int width;

    public final int height;

    //can have a setter to set total treasure. to discuss.
    public final static int totalTreasure = 6;

    private GameBoard(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int timeLeft, int level, int width, int height) {
        this.board = board;
        this.player = player;
        this.robots = robots;
        this.timeLeft = timeLeft;
        this.level = level;
        this.width = width;
        this.height = height;
        attach(getExitTile());
    }

    public static GameBoard of(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int width, int height) {
        return new GameBoard(board, player, robots, 10, 0, width, height);
    }

    private void playerMove(Direction direction, GameBoard gameBoard) {
        player.prepareMove(direction, gameBoard);
        notifyObservers();
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
        return new GameBoard(board, player, robots, timeLeft, level, width, height);
    }

    private static void checkValid(int timeLeft, int width, int height, int level) {
        if (timeLeft <= 0 || width < 2 || height < 2 || level < 1) {
            throw new IllegalArgumentException("Invalid game board");
        }
    }

    /**
     * Get the board
     * @return Board
     */
    public List<List<Tile<Item>>> getBoard() { return Collections.unmodifiableList(board); }
    public Player getPlayer() { return player; }

    /**
     * Get current game board state.
     *
     * @return GameState
     */
    public GameState getGameState() {
        return new GameState(board, player, robots, timeLeft, level);
    }

    public void onGameOver() {
        throw new IllegalArgumentException("Game Over"); // temporary
    }


    /**
     * Take an action on current board.
     *
     * @param direction the direction player wants to go.
     */
    public void action(Direction direction) {
        if (Objects.isNull(direction)) {
            throw new IllegalArgumentException("Direction null");
        }

        robotsMove();
        playerMove(direction, this);

    }

    public void attach(GameStateObserver ob) {
        obs.add(ob);
    }

    public void detach(GameStateObserver ob) {
        obs.remove(ob);
    }

    public void notifyObservers() {
        for (GameStateObserver observer : obs) {
            observer.update(player.getTreasure().stream().filter(e -> e instanceof Treasure).toList().size());
        }
    }

    private Tile<Item> getExitTile() {
        return getGameState()
                .board()
                .stream()
                .flatMap(e -> e.stream())
                .filter(t -> t.item instanceof Exit)
                .toList()
                .getFirst();
    }
}
