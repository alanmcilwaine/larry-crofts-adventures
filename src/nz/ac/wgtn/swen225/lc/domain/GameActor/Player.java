package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.TeleportItem;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Player on the board
 * @author Yee Li
 */
public class Player implements Actor {

    private final List<Item> treasure = new ArrayList<>();

    private Location location;

    private Direction playerFacing = Direction.DOWN;

    private boolean isDead = false;

    private boolean showPlayerInfo = false;

    private boolean nextLevel = false;

    private GameBoard gameBoard;

    public GameBoard getGameBoard() {
        return gameBoard;
    }
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Direction getActorFacing() {
        return playerFacing;
    }

    @Override
    public void setActorFacing(Direction direction) { this.playerFacing = direction; }

    public List<Item> getTreasure() {
        return List.copyOf(treasure);
    }

    public boolean isDead() {
        return isDead;
    }

    public Player(Location location) {
        this.location = location;
    }

    public void addTreasure(Item item) {
        treasure.add(item);
        GameBoard.domainLogger.log(Level.INFO, String.format("Player has %s treasure", treasure.size()));
    }

    public void removeTreasure(Item item) {
        treasure.remove(item);
        GameBoard.domainLogger.log(Level.INFO, String.format("Player has %s treasure", treasure.size()));
    }

    @Override
    public void doMove(Direction direction, GameBoard gameBoard, Tile<Item> current, Tile<Item> next) {
        if(Objects.isNull(this.gameBoard)) {
            this.gameBoard = gameBoard; // not ideal, but to support extended features.
        }
        if (direction.equals(Direction.NONE) || isDead || nextLevel ) return;

        this.playerFacing = direction;

        GameBoard.domainLogger.log(Level.INFO, "Player is facing:" + playerFacing + " should try to move " + playerFacing);

        MovableBox box = gameBoard.getGameState().boxes()
                                    .stream().filter(b -> b.getLocation().equals(next.location))
                                    .findFirst().orElseGet(() -> null);

        if (!next.canStepOn(this) || box != null && !box.attemptMove(direction, gameBoard)) { return; }
        actOnTile(direction, gameBoard, current, next);

        GameBoard.domainLogger.log(Level.INFO, "Player is at:" + location + " after moving " + direction);

        // check for game over
        if(gameBoard.getGameState().robots().stream().anyMatch((x)->x.getLocation().equals(this.location))){
            die();
            gameBoard.onGameOver();
        }
    }

    @Override
    public void updateActorLocation(Location location) {
        this.location = new Location(location.x(), location.y());
    }

    public boolean isShowPlayerInfo() {
        return showPlayerInfo;
    }

    public void setShowPlayerInfo(boolean showPlayerInfo) {
        this.showPlayerInfo = showPlayerInfo;
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(boolean nextLevel) {
        this.nextLevel = nextLevel;
    }

    /**
     * Kills the player
     */
    public void die() { isDead = true; }

    @Override
    public String toString() { return "Player"; }
}
