package nz.ac.wgtn.swen225.lc.domain.GameActor;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.Key;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.ArrayList;
import java.util.List;

public class Player implements Actor {

    private final List<Item> treasure = new ArrayList<>();

    private Location location;

    private Direction playerFacing = Direction.DOWN;

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

    public Player(Location location) {
        this.location = location;
    }

    public boolean addTreasure(Item item) {
        return treasure.add(item);
    }

    public boolean removeTreasure(Item item) {
        return treasure.remove(item);
    }


    @Override
    public void prepareMove(Direction direction, GameBoard gameBoard) {
        this.playerFacing = direction;
        //find current player location and the tile the player is on.
        //check if player can move onto the tile.
        // TODO logic
        Location newLoc = direction.act(this.location); // location to move to
        Tile tile = gameBoard.getBoard().get(newLoc.x()).get(newLoc.y()); // tile on this location
        Tile prevTile = gameBoard.getBoard().get(this.location.x()).get(this.location.y());

        if (tile.canStepOn(this)) {
            doMove(newLoc);
            tile.onEntry(this);
            tile.onExit(this);
        }

        //Tile<Key> tk = new Tile<>(new Key(ItemColor.BLUE), new Location(1,1)); //target tile
//        if(tile.canStepOn(this)){
//            doMove(tile.location);
//            this.location = tile.location;
//            //TODO check if this location has robot
//            if(gameBoard.getGameState().robots().stream().anyMatch((x)->x.getLocation().equals(this.location))){
//                //filter out if it's killer robot?
//
//                //need to discuss with app how to decide game is over.
//            };
//            tile.onEntry(this);
//            tile.onExit(this);
//        };
    }

    @Override
    public void doMove(Location location) {
        this.location = new Location(location.x(), location.y());
        //updateGameState after move
    }
}
