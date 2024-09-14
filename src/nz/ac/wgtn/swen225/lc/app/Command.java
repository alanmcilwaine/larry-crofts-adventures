package nz.ac.wgtn.swen225.lc.app;

public enum Command{
    // FIXME pass in Direction instead of strings. Strings are a placeholder.
    Left("Left"),
    Right("Right"),
    Up("Up"),
    Down("Down");

    String direction;
    Command(String direction){ // FIXME change String direction to Direction
        this.direction = direction;
    }
    public String getSaveData(){
        return direction.toString();
    }
    public static Command generate(String command){
        return switch(command){
            case "Left" -> Command.Left;
            case "Right" -> Command.Right;
            case "Up" -> Command.Up;
            case "Down" -> Command.Down;
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }
}