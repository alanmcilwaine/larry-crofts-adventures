package nz.ac.wgtn.swen225.lc.domain.GameItem;

import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

/**
 * Represents a big button that functions the same as a button.
 * The difference is that a big button can only be pressed by a movable box
 *
 * @author Carla Parinas 300653631
 */
public class BigButton extends Button {
    {
        isBig = true;
    }

    @Override
    public Item makeNew() {
        return new BigButton();
    }
}
