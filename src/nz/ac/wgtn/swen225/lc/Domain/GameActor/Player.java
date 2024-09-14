package nz.ac.wgtn.swen225.lc.Domain.GameActor;

import nz.ac.wgtn.swen225.lc.Domain.GameBoard;
import nz.ac.wgtn.swen225.lc.Domain.GameItem.Key;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.Domain.Tile;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.Direction;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.KeyColor;
import nz.ac.wgtn.swen225.lc.Domain.Utilities.Location;

import java.util.ArrayList;
import java.util.List;

public class Player implements Actor {

    private final List<Item> treasure = new ArrayList<>();

    private Location location;

    public Player(Location location) {
        this.location = location;
    }

    public boolean addTreasure(Item item) {
        return treasure.add(item);
    }

    public boolean removeTreasure(Item item) {
        return treasure.remove(item);
    }

    public void prepareMove(Direction direction, GameBoard gameBoard) {
        //find current player location and the tile the player is on.
        //check if player can move onto the tile.
        // TODO logic
        Tile<Key> tk = new Tile<>(new Key(KeyColor.BLUE), new Location(1,1)); //target tile
        if(tk.canStepOn(this)){
            doMove(gameBoard);
            tk.onEntry(this);
            tk.onExit(this);
        };
    }

    private void doMove(GameBoard gameBoard) {
        //updateGameState after move
        gameBoard.updateGameState();
    }
}
