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


    @Override
    public void doMove(Direction direction, GameBoard gameBoard) {
        this.playerFacing = direction;
        GameBoard.domainLogger.log(Level.INFO, "Player is facing:" + playerFacing + " should try to move " + playerFacing);
        var currentTile = findTileInSpecificLocation(gameBoard, location);
        Location nextLocation = direction.act(this.location);

        if(locationIsValid(nextLocation, gameBoard)) {

            Tile<Item> nextTile = findTileInSpecificLocation(gameBoard, nextLocation);

            if (nextTile.canStepOn(this)) {
                currentTile.onExit(this);
                nextTile.onEntry(this);
                updateActorLocation(nextLocation);
                GameBoard.domainLogger.log(Level.INFO, "Player is at:" + location + " after moving " + direction);
            } else {
                GameBoard.domainLogger.log(Level.INFO, "Player tried to move to but blocked:" + location.toString());
            }
            if(gameBoard.getGameState().robots().stream().anyMatch((x)->x.getLocation().equals(this.location))){
                isDead = true;
                gameBoard.onGameOver();
            }
        } else {
            GameBoard.domainLogger.log(Level.INFO, "Player tried to move to invalid location:" + nextLocation);
        }
    }

    @Override
    public void updateActorLocation(Location location) {
        this.location = new Location(location.x(), location.y());
    }

    private boolean locationIsValid(Location location, GameBoard gameBoard) {
        return location.x() >= 0 && location.x() < gameBoard.getWidth() &&
               location.y() >= 0 && location.y() < gameBoard.getHeight();
    }


    private Tile<Item> findTileInSpecificLocation(GameBoard gameBoard, Location targetLocation) {
        return gameBoard.getGameState().board().stream()
                .flatMap(Collection::stream)
                .filter(x->x.location.equals(targetLocation))
                .toList()
                .getFirst();
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
}
