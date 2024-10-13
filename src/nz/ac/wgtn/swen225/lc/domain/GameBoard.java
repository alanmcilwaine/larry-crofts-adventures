package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.KillerRobot;
import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Crate;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Button;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LaserSource;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedDoor;
import nz.ac.wgtn.swen225.lc.domain.GameItem.LockedExit;
import nz.ac.wgtn.swen225.lc.domain.Interface.GameStateObserver;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * The main game board
 * @author Yee Li, Maria Louisa Carla Parinas
 */
public class GameBoard {
    public static final Logger domainLogger = DomainLogger.LOGGER.getLogger();

    private static final List<GameStateObserver> obs = new CopyOnWriteArrayList<>();

    private final List<List<Tile<Item>>> board;

    private final Player player;

    private final List<Robot> robots;

    private final List<LaserSource> laserSources;

    private final List<MovableBox> boxes;

    private final int timeLeft;

    private final int level;

    private final int width;

    private final int height;

    private final int totalTreasure;
//    private final int totalKeys;

    public GameBoard(GameBoardBuilder builder) {
        this.board = builder.getBoard();
        this.player = builder.getPlayer();
        this.robots = builder.getRobots();
        this.boxes = builder.getBoxes();
        this.laserSources = builder.getLaserSources();
        this.timeLeft = builder.getTimeLeft();
        this.level = builder.getLevel();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.totalTreasure = builder.getTotalTreasure();
//        this.totalKeys = builder.getTotalKeys();
        var l = getLockedExit();
        if (l != null) {
            subscribeGameState(l);
        }

        configureButtons();
        activateLasers();
        subscribeGameState(getLockedExit());
        playerMove(Direction.NONE, this);
    }

    /**
     * Take an action on current board.
     *
     * @param direction the direction player wants to go.
     */
    public void action(Direction direction) {
        Util.checkNull(direction, "Direction is null");

        //activateLasers();
        robotsMove();
        playerMove(direction, this);
        notifyObservers();

        board.forEach(x -> x.forEach(y -> y.item.tick()));
    }

    private void playerMove(Direction direction, GameBoard gameBoard) {
        player.attemptMove(direction, gameBoard);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //TODO this is for testing?
    public void addRobot(int x, int y) {
        robots.add(new KillerRobot(x, y));
    }

    public void addBox(MovableBox box) {
        boxes.add(box);
    }

    public void addLaserSource(LaserSource ls) {
        laserSources.add(ls);
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

    public void activateLasers() {
        laserSources.forEach(ls -> ls.updateLasers(this));
    }

    /**
     * Get the board
     *
     * @return Board
     */
    public List<List<Tile<Item>>> getBoard() {
        return Collections.unmodifiableList(board);
    }

    public Tile<Item> itemOnTile(Location target) {
        return board.stream()
                .flatMap(Collection::stream)
                .filter(x->x.location.equals(target))
                .toList()
                .getFirst();
    }

    public void configureButtons() {
        board.forEach(x -> x.forEach(y -> {
            if (y.item instanceof Button b) {
                b.attachTiles(surroundingTilesAt(y.location));
            }
        }));
    }

    private List<Tile<Item>> surroundingTilesAt(Location l) {
        List<Tile<Item>> ls = new ArrayList<>();

        for(int x = l.x() - 1; x <= l.x() + 1; x++) {
            for(int y = l.y() - 1; y <= l.y() + 1; y++) {
                if (locationisValid(new Location(x, y))) { ls.add(board.get(y).get(x)); }
            }
        }
        return ls;
    }

    private boolean locationisValid(Location location) {
        return location.x() >= 0 && location.x() < getWidth() &&
                location.y() >= 0 && location.y() < getHeight();
    }

    /**
     * Gives a deep copy of a given gameState
     *
     * @return new deep copy of gameState
     */
    public GameBoard copyOf() {
        List<List<Tile<Item>>> newBoard = board.stream().map(x -> x.stream()
                                                            .map(y -> new Tile<>(y.item.makeNew(), y.location))
                                                            .toList())
                                                            .toList();


        // might have more types of robots in the future, could change this
        List<Robot> newRobots = robots.stream()
                                        .map(r -> (Robot) new KillerRobot(r.getLocation().x(), r.getLocation().y()))
                                        .toList();

        List<MovableBox> newBoxes = boxes.stream()
                                            .map(b -> b instanceof Crate ?
                                                    new Crate(b.getLocation().x(), b.getLocation().y()) :
                                                    new MovableBox(b.getLocation().x(), b.getLocation().y()))
                                            .toList();

        List<LaserSource> newLasers = laserSources.stream()
                .map(ls -> (LaserSource) ls.makeNew()).toList();

        // make new board
        return new GameBoardBuilder().addBoard(newBoard).addBoardSize(width, height)
                                    .addLevel(level).addPlayer(new Player(player.getLocation()))
                                    .addRobots(newRobots).addBoxes(newBoxes).addLaserSources(newLasers)
                                    .addTimeLeft(timeLeft)
                                    .addTreasure(totalTreasure)
//                                    .addKeys(totalKeys)
                                    .build();
    }

    /**
     * Get current game board state.
     *
     * @return GameState
     */
    public GameState getGameState() {
        return new GameState(board, player, robots, boxes, laserSources, timeLeft, level, width, height, totalTreasure);
    }

    private static void attach(GameStateObserver ob) {
        obs.add(ob);
    }

    private static void detach(GameStateObserver ob) {
        obs.remove(ob);
    }

    private static <T extends GameStateObserver> void subscribeGameState(T observer) {
        attach(observer);
    }

    public static <T extends GameStateObserver> void unsubscribeGameState(T observer) {
        detach(observer);
    }

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
                .orElse(null);
    }
}
