package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.KillerRobot;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedExit;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class GameBoard {
    public static final Logger domainLogger = DomainLogger.LOGGER.getLogger();

    private static final List<GameStateObserver> obs = new CopyOnWriteArrayList<>();

    private final List<List<Tile<Item>>> board;

    private final Player player;

    private final List<Robot> robots;

    private final int timeLeft;

    private final int level;

    private final int width;

    private final int height;

    private final int totalTreasure;

    public GameBoard(GameBoardBuilder builder) {
        this.board = builder.getBoard();
        this.player = builder.getPlayer();
        this.robots = builder.getRobots();
        this.timeLeft = builder.getTimeLeft();
        this.level = builder.getLevel();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.totalTreasure = builder.getTotalTreasure();
        subscribeGameState(getLockedExit());
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

    //TODO this is for testing?
    public void addRobotAtLocation(int x, int y) {
        robots.add(new KillerRobot(x, y));
    }

    /**
     * Moves all the robots in the level
     */
    private void robotsMove() {
        if (robots.isEmpty()) {
            return;
        }
        robots.forEach(r -> r.update(this));
    }

    /**
     * Get the board
     *
     * @return Board
     */
    public List<List<Tile<Item>>> getBoard() {
        return Collections.unmodifiableList(board);
    }

    /**
     * Get current game board state.
     *
     * @return GameState
     */
    public GameState getGameState() {
        return new GameState(board, player, robots, timeLeft, level, width, height, totalTreasure);
    }

    //TODO
    public void onGameOver() {
        throw new IllegalArgumentException("Game Over"); // temporary
    }

    private static void attach(GameStateObserver ob) {
        obs.add(ob);
    }

    private static void detach(GameStateObserver ob) {
        obs.remove(ob);
    }

    public static <T extends GameStateObserver> void subscribeGameState(T observer) {
        attach(observer);
    }

    public static <T extends GameStateObserver> void unsubscribeGameState(T observer) {
        detach(observer);
    }

    //TODO
    public void notifyObservers() {
        for (GameStateObserver observer : obs) {
            observer.update(getGameState());
        }
    }


    /**
     * Get the tile hosting the exit item.
     *
     * @return tile with exit.
     */
    private LockedExit getLockedExit() {
        return getGameState()
                .board()
                .stream()
                .flatMap(Collection::stream)
                .map(tile -> tile.item)
                .filter(item -> item instanceof LockedExit)
                .map(item -> (LockedExit) item)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Map must have a locked exit."));
    }
}


