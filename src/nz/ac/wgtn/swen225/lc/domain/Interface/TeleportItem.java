package nz.ac.wgtn.swen225.lc.domain.Interface;

import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

/**
 * Teleport like item interface
 *
 * @author Yee Li
 */
public interface TeleportItem extends Item {
    Location destination();
}
