package nz.ac.wgtn.swen225.lc.app;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Direction;

/**
 * Command --- Contains the keys pressed by the user as a Command. Used to send to recorder because
 * recorder cannot access Direction.
 *
 * @author Alan McIlwaine 300653905
 */
public enum Command{
    Left(Direction.LEFT),
    Right(Direction.RIGHT),
    Up(Direction.UP),
    Down(Direction.DOWN),
    None(Direction.NONE);

    private final Direction direction;

    /**
     * Constructor for the enum constants where each Command has a Direction it stores.
     * @param direction Direction that the player will move in.
     */
    Command(Direction direction){
        this.direction = direction;
    }

    /**
     * getSaveData()
     * Access to the toString of the Direction.
     * @return The save string that Recorder can use to save the command to an output.
     */
    public String getSaveData(){
        return direction.toString();
    }

    /**
     * direction()
     * Getter for the direction of the Command.
     * @return The Direction of the Command.
     */
    public Direction direction(){
        return direction;
    }

    /**
     * generate()
     *
     * @param command Command in the form of a string, read from a file, as recorder cannot create Command
     * @return The command corresponding to the string. The player input.
     */
    public static Command generate(String command){
        return switch(command){
            case "Left" -> Command.Left;
            case "Right" -> Command.Right;
            case "Up" -> Command.Up;
            case "Down" -> Command.Down;
            case "None" -> Command.None;
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }
}