package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;

import nz.ac.wgtn.swen225.lc.domain.GameItem.Exit;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedExit;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Treasure;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;

import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Util;

import java.util.*;
import java.util.logging.Logger;

public class GameBoard {
    private final List<GameStateObserver> obs = new ArrayList<>();
    private final List<List<Tile<Item>>> board;

    private final Player player;

    private final List<Robot> robots;

    private final int timeLeft;

    private final int level;

    public static final Logger domainLogger = DomainLogger.LOGGER.getLogger();

    public final int width;

    public final int height;

    public static int totalTreasure = 6;

    private GameBoard(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int timeLeft, int level, int width, int height) {
        this.board = board;
        this.player = player;
        this.robots = robots;
        this.timeLeft = timeLeft;
        this.level = level;
        this.width = width;
        this.height = height;
        subscribeGameState(getExitTile());
    }

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

    /**
     * Take an action on current board.
     *
     * @param direction the direction player wants to go.
     */
    public void action(Direction direction) {
        Util.checkNull(direction, "Direction is null");

        robotsMove();
        playerMove(direction, this);
        notifyObservers();
    }


    private void playerMove(Direction direction, GameBoard gameBoard) {
        player.doMove(direction, gameBoard);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static void setTotalTreasure(int totalTreasure) {
        GameBoard.totalTreasure = totalTreasure;
    }

    /**
     * Moves all the robots in the level
     */
    private void robotsMove() { robots.forEach(r -> r.update(this)); }

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

    private void attach(GameStateObserver ob) {
        obs.add(ob);
    }

    private void detach(GameStateObserver ob) {
        obs.remove(ob);
    }

    public <T extends GameStateObserver> void subscribeGameState(T observer) {
        attach(observer);
    }

    public <T extends GameStateObserver> void unsubscribeGameState(T observer) {
        detach(observer);
    }

    public void notifyObservers() {
        for (GameStateObserver observer : obs) {
            observer.update(getGameState());
        }
    }


    /**
     * Get the tile hosting the exit item.
     * @return tile with exit.
     */
    private Tile<Item> getExitTile() {
        try{
            return getGameState()
                    .board()
                    .stream()
                    .flatMap(Collection::stream)
                    .filter(t -> t.item instanceof LockedExit)
                    .toList()
                    .getFirst();
        }
       catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Map must have a locked exit." + e.getMessage());
       }
    }
}


