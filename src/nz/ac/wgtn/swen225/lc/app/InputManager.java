package nz.ac.wgtn.swen225.lc.app;

public interface InputManager {
    /**
     * The time it takes before registering another movement command.
     * Default is 300ms.
     * @return Time in milliseconds.
     */
    default public int movementWait() {
        return 300;
    }

    Command currentCommand();

}
