package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public class Player implements Actor {

    private final List<Item> treasure = new ArrayList<>();

    private Location location;

    private Direction playerFacing = Direction.DOWN;

    private boolean isDead = false;

    private boolean showPlayerInfo = false;

    private boolean nextLevel = false;

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Direction getActorFacing() {
        return playerFacing;
    }

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

    public boolean attemptMove(Direction direction, GameBoard gameBoard) {
        // i moved this here so we could have cleaner code and not have nesting if-statements
        this.playerFacing = direction;

        if (!locationIsValid(direction.act(this.location), gameBoard)) {
            //GameBoard.domainLogger.log(Level.INFO, "Player tried to move to invalid location:" + nextLocation);
            return false;
        }

        doMove(direction, gameBoard);
        return true;
    }

    @Override
    public void doMove(Direction direction, GameBoard gameBoard) {
        GameBoard.domainLogger.log(Level.INFO, "Player is facing:" + playerFacing + " should try to move " + playerFacing);

        var currentTile = findTileInSpecificLocation(gameBoard, location);
        Location nextLocation = direction.act(this.location);
        Tile<Item> nextTile = findTileInSpecificLocation(gameBoard, nextLocation);

        if (nextTile.canStepOn(this)) {
            if (nextTile.item instanceof MovableBox m && !m.attemptPush(direction, gameBoard)) { return; }
            actOnTile(direction, gameBoard, currentTile, nextTile);

            GameBoard.domainLogger.log(Level.INFO, "Player is at:" + location + " after moving " + direction);
        } else {
            GameBoard.domainLogger.log(Level.INFO, "Player tried to move to but blocked:" + nextLocation);
        }

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
