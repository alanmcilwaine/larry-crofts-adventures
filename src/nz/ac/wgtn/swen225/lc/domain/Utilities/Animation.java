package nz.ac.wgtn.swen225.lc.domain.Utilities;

/**
 * Animation functionality of the items. Allows items to have sprites that
 * could be animated based on the length and the ticks per frame.
 */
public class Animation {

    /**
     * Creates an animation object that will produce the sprite name
     *
     * @param name          name of the sprite
     * @param length        how many frames
     * @param ticksPerFrame how many ticks it should advance to next frame
     */
    public Animation(String name, int length, int ticksPerFrame) {
        this.name = name;
        this.length = length * ticksPerFrame;
        this.ticksPerFrame = ticksPerFrame;
    }

    private final int length;
    private final String name;
    private final int ticksPerFrame;
    private int frame;

    /**
     * Ticks the animation
     */
    public void tick() {
        frame++;
        if (frame >= length) frame = 0;
    }

    public String toString() {
        return name + (frame / ticksPerFrame);
    }
}