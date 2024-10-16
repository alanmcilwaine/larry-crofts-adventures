package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Actor;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Interface.Togglabble;
import nz.ac.wgtn.swen225.lc.domain.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a button that can all it's nearby toggleable items.
 * When stepped on as a player, it will call all the tiles nearby 1 space
 * and will toggle if it is toggleable.
 *
 * @author Carla Parinas 300653631
 */
public class Button implements Item {
    public boolean isPressed = false;
    List<Tile<Item>> surroundingTiles = new ArrayList<>();
    public boolean isBig = false;

    /**
     * Attach the surrounding tiles unto the button
     *
     * @param surroundingTiles the list of tiles around the button
     */
    public void attachTiles(List<Tile<Item>> surroundingTiles) {
        this.surroundingTiles.addAll(surroundingTiles);
    }

    @Override
    public <T extends Item> void onTouch(Actor actor, Tile<T> tile) {
        // Robots should not press the button but MovableBoxes can
        if (actor instanceof Robot) {
            return;
        }

        if (isBig && !(actor instanceof MovableBox)) {
            return;
        }
        toggleSurroundingTiles();
    }

    /**
     * Toggle each tile around the button. Streams through the list of tiles and
     * calls toggle on them if it is toggleable.
     */
    public void toggleSurroundingTiles() {
        if (isPressed) {
            return;
        }
        surroundingTiles.forEach(t -> {
            if (t.item instanceof Togglabble tg) {
                tg.toggle(t);
            }
        });
        isPressed = true;
    }

    @Override
    public <T extends Item> void onExit(Actor actor, Tile<T> tile) {
        isPressed = false;
    }

    @Override
    public String toString() {
        if (isBig) {
            return isPressed ? "PressedButton" : "Button";
        } else {
            return isPressed ? "PressedButtonSmall" : "ButtonSmall";
        }
    }

    @Override
    public Item makeNew() {
        return new Button();
    }
}
