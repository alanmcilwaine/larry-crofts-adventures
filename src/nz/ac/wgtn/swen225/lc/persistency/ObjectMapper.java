package nz.ac.wgtn.swen225.lc.persistency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.Utilities.*;

public class ObjectMapper {
    static final Map<String, String> gameItems = new HashMap<>();
    private static final Map<String, Function<ItemColor, Item>> itemConstructors = new HashMap<>(); 

    static {
        itemConstructors.put("X", color -> new Exit());
        itemConstructors.put("K", Key::new);
        itemConstructors.put("LD", LockedDoor::new);
        itemConstructors.put("LX", color -> new LockedExit());
        itemConstructors.put("F", color -> new NoItem());
        itemConstructors.put("T", color -> new Treasure());
        itemConstructors.put("UD", UnLockedDoor::new);
        itemConstructors.put("W", color -> new Wall());
        itemConstructors.put("Tu", color -> new Tube());
        itemConstructors.put("B", color -> new Button());
        itemConstructors.put("LI", color -> new LaserInput());

        gameItems.put("Exit", "X");
        gameItems.put("Info", "I");
        gameItems.put("Key", "K");
        gameItems.put("LockedDoor", "LD");
        gameItems.put("LockedExit", "LX");
        gameItems.put("NoItem", "F");
        gameItems.put("Treasure", "T");
        gameItems.put("UnLockedDoor", "UD");
        gameItems.put("Wall", "W");
        gameItems.put("OneWayTeleport", "TP"); //Special case (has own creator)
        gameItems.put("MovableBox", "MB"); //Special case (has own creator)
        gameItems.put("Crate", "C"); //Special case (has own creator)
        gameItems.put("Tube", "Tu"); 
        gameItems.put("LaserSource", "L"); //Special case (has own creator)
        gameItems.put("Mirror", "M"); //Special case (has own creator)
        gameItems.put("Button", "B");
        gameItems.put("LaserInput", "LI");
    }

    /**
     * Saves the given GameBoard object as a JSON format to a file.
     * @author zhoudavi1 300652444
     * @param level The GameBoard object to be saved.
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    public String saveLeveltoFile(GameBoard level) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Board
        List<List<String>> stringBoard = convertBoardToStrings(level);
        json.append("  \"board\": [\n");
        for (int i = 0; i < stringBoard.size(); i++) {
            json.append("    [");
            List<String> row = stringBoard.get(i);
            for (int j = 0; j < row.size(); j++) {
                json.append("\"").append(row.get(j)).append("\"");
                if (j < row.size() - 1) {
                    json.append(", "); // Comma between cells
                }
            }
            json.append("]");
            if (i < stringBoard.size() - 1) {
                json.append(",\n"); // Comma between rows
            } else {
                json.append("\n"); //End of the board
            }
        }
        json.append("  ],\n");
        
        // Level
        json.append("  \"level\": {\n");
        json.append("    \"number\": ").append(level.getGameState().level()).append(",\n");
        json.append("    \"Time Limit\": ").append(level.getGameState().timeLeft()).append(",\n");
        
        //Total Treasure
        json.append("    \"Total Treasure\": ").append(level.getGameState().totalTreasure()).append("\n");

        json.append("  },\n"); // Closing the level object
    
        // Info
        Info info = new Info("Null");
        int x = 0, y = 0;
        for (List<Tile<Item>> row : level.getGameState().board()) {
            for (Tile<Item> tile : row) {
                if (tile.item instanceof Info info1) {
                    info = info1;
                    x = row.indexOf(tile);
                    y = level.getBoard().indexOf(row);
                }
            }
        }
    
        json.append("  \"info\": {\n");
        json.append("    \"info\": \"").append(info.info()).append("\",\n");
        json.append("    \"x\": ").append(x).append(",\n");
        json.append("    \"y\": ").append(y).append("\n");
        json.append("  }\n"); // Closing the info object
    
        json.append("}"); // Closing the entire JSON object
    
        return json.toString();
    }

    /**
     * Saves the given list of actions as a JSON format to a file.
     * @author zhoudavi1 300652444
     * @param actions The list of actions to be saved.
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    //Write Action List as String throws an exception if fails
    public String saveCommandstoFile(List<Command> actions, int level) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        String levelFile = "levels/level" + level + ".json";
        json.append("  \"levelFileName\": \"").append(levelFile).append("\",\n");
        json.append("  \"actions\": [\n");
        for (int i = 0; i < actions.size(); i++) {
            Command action = actions.get(i);
            json.append("    {\n");
            json.append("      \"actionType\": \"").append(action.getSaveData()).append("\"\n");
            json.append("    }");
            if (i < actions.size() - 1) {
                json.append(",\n"); // Comma between actions
            } else {
                json.append("\n"); // No comma after the last action
            }
        }
        json.append("  ]\n");
        json.append("}"); // Closing the entire JSON object
    
        return json.toString();
    }
    

    /**
     * Read the given JSON string and return a GameBoard object.
     *
     * @author zhoudavi1 300652444
     * @param json The string to be converted to a GameState object.
     * @return GameBoard The GameBoard made from the JSON string.
     */
    public GameBoard convertJSONtoGameBoard(String json) {
        // Parse board
        List<List<Tile<Item>>> board = new ArrayList<>();
        Player player = null;
        List<Robot> robots = new ArrayList<>();
        List<MovableBox> moveableBoxes = new ArrayList<>();
    
        // Find the board section
        int boardStart = json.indexOf("\"board\": [");
        if (boardStart == -1) {
            throw new IllegalArgumentException("Board not found in JSON");
        }
        String boardString = json.substring(boardStart + 10, json.indexOf("\"level\""));
    
        String[] rowStrings = boardString.split("\\],\\s*\\[");
        for (int y = 0; y < rowStrings.length; y++) {
            List<Tile<Item>> row = new ArrayList<>();
            String[] cellStrings = rowStrings[y].replace("[", "").replace("]", "").split(",\\s*");
    
            for (int x = 0; x < cellStrings.length; x++) {
                String cellCode = cellStrings[x].replace("\"", "").trim(); // Trim whitespaces
                String[] parts = cellCode.split("-"); // Find the dash to indicate actors
                Item item = createItemFromCode(parts[0]);

                // Check if the cell contains the player (-P)
                if (parts.length > 1 && parts[1].equals("P")) {
                    player = new Player(new Location(x, y));
                }
    
                // Check if the cell contains a robot (-R)
                else if (parts.length > 1 && parts[1].startsWith("R")) {
                    KillerRobot robot = new KillerRobot(x, y);
                    //Check for path after =
                    String path = parts[1].substring(1);
                    robot.setActorPath(pathDefinining(path));
                    robots.add(robot);
                }

                //Check for moveable box
                else if (parts.length > 1 && parts[1].equals("MB")) {
                    moveableBoxes.add(new MovableBox(x, y));
                }

                //Check for crate
                else if (parts.length > 1 && parts[1].equals("C")) {
                    moveableBoxes.add(new Crate(x, y));
                }

                //Check for mirror
                else if (parts.length > 1 && parts[1].startsWith("M")) {
                    String orientation = parts[1].substring(2); //Get the orientation
                    Orientation o = orientationDefining(orientation);
                    moveableBoxes.add(new Mirror(o, x, y));
                }

                //Check for laser source
                else if (parts.length > 1 && parts[1].startsWith("L->")) {
                    String direction = parts[1].substring(2);
                    item = new LaserSource(returnDirection(direction), true, x , y);
                }

                row.add(new Tile<>(item, new Location(x, y))); // Add the item to the board row
            }
            board.add(row);
        }
    
        // Parse level info
        int levelStart = json.indexOf("\"level\": {");
        if (levelStart == -1) {
            throw new IllegalArgumentException("Level info not found in JSON");
        }
        String levelString = json.substring(levelStart, json.indexOf("}", levelStart));

        int levelNumber = Integer.parseInt(extractValue(levelString, "\"number\": "));
        int timeLimit = Integer.parseInt(extractValue(levelString, "\"Time Limit\": "));
        int totalTreasure = Integer.parseInt(extractValue(levelString, "\"Total Treasure\": "));

        // Parse info
        int infoStart = json.indexOf("\"info\": {");
        if (infoStart == -1) {
            throw new IllegalArgumentException("Info not found in JSON");
        }
        String infoString = json.substring(infoStart + 9, json.indexOf("}", infoStart) + 1);
    
        String infoText = extractValue(infoString, "\"info\": \"");
        int x = Integer.parseInt(extractValue(infoString, "\"x\": "));
        int y = Integer.parseInt(extractValue(infoString, "\"y\": "));
    
        board.get(y).get(x).item = new Info(infoText);
    
        int width = board.get(0).size();
        int height = board.size();
    
        return new GameBoardBuilder().addBoard(board).addBoardSize(width, height).addTimeLeft(timeLimit)
                .addTreasure(totalTreasure).addLevel(levelNumber).addPlayer(player).addRobots(robots).addBoxes(moveableBoxes).build();
    }

    /**
     * helper method to extract values from a JSON string
     * @param jsonString The JSON string to extract the value from
     * @param key The key to search for
     * @return String The value associated with the key
     */
    private String extractValue(String jsonString, String key) {
        int keyStart = jsonString.indexOf(key);
        if (keyStart == -1) {
            throw new IllegalArgumentException("Key not found in JSON: " + key);
        }
        keyStart += key.length();
        
        // Skip spaces and the colon
        while (keyStart < jsonString.length() && (jsonString.charAt(keyStart) == ' ' || jsonString.charAt(keyStart) == ':' || jsonString.charAt(keyStart) == '\n' || jsonString.charAt(keyStart) == '\t')) {
            keyStart++;
        }
        
        // Find the end of the value
        int valueEnd = jsonString.indexOf(",", keyStart);
        if (valueEnd == -1) {
            valueEnd = jsonString.indexOf("}", keyStart);
        }
        if (valueEnd == -1) {
            valueEnd = jsonString.length();
        }
    
        String value = jsonString.substring(keyStart, valueEnd).trim();
        return value.replace("\"", "").trim(); // Remove quotes
    }
    
    /**
     * Read the given list of actions as a JSON format from a file.
     *
     * @author zhoudavi1 300652444
     * @param json The string to be converted to a list of actions.
     * @return List<Action> The list of actions to be loaded.
     */
    public List<Command> convertJSONtoActions(String json) {
        List<Command> actions = new ArrayList<>();
        String actionsString = json.substring(json.indexOf("\"actions\": [") + 13, json.lastIndexOf("]"));
        
        String[] actionStrings = actionsString.split("\\},\\s*\\{");
        for (String actionString : actionStrings) {
            actionString = actionString.replace("{", "").replace("}", "").trim();
            if (!actionString.isEmpty()) {
                String actionType = extractValue(actionString, "\"actionType\": \"");
                actions.add(Command.generate(actionType));
            }
        }
        return actions;
    }
    
    /**
     * Convert an item to a string code for saving to JSON.
     * @author zhoudavi1 300652444
     * @param item The item to be converted.
     * @return String The string code for the item.
     */
    public String gameItemCode(Item item) {
        // Get the base code from the item class name
        String itemClassName = item.getClass().getSimpleName();
        String baseCode = gameItems.get(itemClassName);
    
        if (baseCode == null) {
            throw new IllegalArgumentException("Unknown item: " + itemClassName);
        }

        //Special case for OneWayTeleport
        if (item instanceof OneWayTeleport tp) {
            return baseCode + "(" + tp.destination().x() + "@" + tp.destination().y() + ")";
        }

        //Special case for LaserSource
        if (item instanceof LaserSource laserSource) {
           return baseCode + "->" + laserSource.getDirection();
        }
    
        // Add color code if the item has a color
        String colorCode = "";
        if (item instanceof Key key) {
            colorCode = getColorCode(key.itemColor());
        } else if (item instanceof LockedDoor lockedDoor) {
            colorCode = getColorCode(lockedDoor.itemColor());
        } else if (item instanceof UnLockedDoor unlockedDoor) {
            colorCode = getColorCode(unlockedDoor.itemColor());
        }
        return baseCode + colorCode;
    }
    
    /**
     * Get the color code for an item color.
     * @author zhoudavi1 300652444
     * @param color The color to get the code for.
     * @return String The color code.
     */
    private String getColorCode(ItemColor color) {
        // Map the ItemColor to the appropriate code
        return switch (color) {
            case BLUE -> "#B";
            case RED -> "#R";
            default -> ""; // No color, return an empty string
        };
    }
    
    /**
     * Create an item from a string code for loading from JSON.
     * @author zhoudavi1 300652444
     * @param code The string code to be converted.
     * @return Item The item created from the code.
     */
    private Item createItemFromCode(String code) {
        //Special case for TP as it takes a location
        if (code.startsWith("TP(")) {
            String location = code.substring(3, code.indexOf(")"));
            Location l = locationMaker(location);
            return new OneWayTeleport(l);
        }

        // Split the code into the main item and the optional color code
        String[] parts = code.split("#");
        String itemCode = parts[0];  // First part is the item code
        ItemColor color = null;      // Initialize color as null

        //Special case for Info
        if (itemCode.equals("I")) {
            return createInfo();
        }

        // If there's a second part, interpret it as the color
        if (parts.length > 1) {
            switch (parts[1]) {
                case "B" -> color = ItemColor.BLUE;
                case "R" -> color = ItemColor.RED;
                default -> throw new IllegalArgumentException("Unknown color code: " + parts[1]);
            }
        }

        // Get the constructor for the item
        Function<ItemColor, Item> constructor = itemConstructors.get(itemCode);
        if (constructor == null) {
            throw new IllegalArgumentException("Unknown item code: " + itemCode);
        }

        return constructor.apply(color);
    }

    /**
     * Create a default Info item for the board.
     * @author zhoudavi1 300652444
     * @return Item The Info item.
     */
    private Item createInfo() {
        return new Info("Info");
    }

    /**
     * Create a location from a string code for loading from JSON.
     * @author zhoudavi1 300652444
     * @param location The string code to be converted.
     * @return Location The location created from the code.
     */
    public Location locationMaker(String location){
        String[] coordinates = location.split("@");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        return new Location(x, y);
    }

    /**
     * Create a direction from a string code for loading from JSON.
     * @author zhoudavi1 300652444
     * @param direction The string code to be converted.
     * @return Direction The direction created from the code.
     */
    public Direction returnDirection(String direction){
        return switch(direction){
            case "Left" -> Direction.LEFT;
            case "Right" -> Direction.RIGHT;
            case "Up" -> Direction.UP;
            case "Down" -> Direction.DOWN;
            case "None" -> Direction.NONE;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    /**
     * 2D representation of the game board.
     * Convert the board of Tile<Item> to List<List<String>>.
     * @author zhoudavi1 300652444
     * @param level The level to be converted.
     * @return List<List<String>> The converted level.
     */
    public List<List<String>> convertBoardToStrings(GameBoard level) {
        //Convert the board of Tile<Item> to List<List<String>>
        List<List<String>> stringBoard = level.getBoard().stream()
            .map((List<Tile<Item>> row) -> row.stream()
                .map((Tile<Item> tile) -> gameItemCode(tile.item)) // Convert each Tile<Item> to the item's string code
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());
    
        //Place the player on the board at their X and Y position
        int playerX = level.getGameState().player().getLocation().x();
        int playerY = level.getGameState().player().getLocation().y();
    
        //Modify the specific tile to include "P" for player
        stringBoard.get(playerY).set(playerX, stringBoard.get(playerY).get(playerX) + "-P");

        //Robot
        for (Robot robot : level.getGameState().robots()) {
            int robotX = robot.getLocation().x();
            int robotY = robot.getLocation().y();
            stringBoard.get(robotY).set(robotX, stringBoard.get(robotY).get(robotX) + "-R");
            stringBoard.get(robotY).set(robotX, stringBoard.get(robotY).get(robotX) + "=" + pathDefinining(robot.getActorPath()));
        }

        //Boxes and Crates -MB or -C
        for (MovableBox box : level.getGameState().boxes()) {
            int boxX = box.getLocation().x();
            int boxY = box.getLocation().y();
            if (box instanceof Crate) {
                stringBoard.get(boxY).set(boxX, stringBoard.get(boxY).get(boxX) + "-C");
            }
            else if(box instanceof Mirror mirror){
                stringBoard.get(boxY).set(boxX, stringBoard.get(boxY).get(boxX) + "-M");
                stringBoard.get(boxY).set(boxX, stringBoard.get(boxY).get(boxX) + "=" + orientationDefining(mirror.getOrientation()));
            }
            else {
                stringBoard.get(boxY).set(boxX, stringBoard.get(boxY).get(boxX) + "-MB");
            }
        }

        return stringBoard;
    }
    /**
     * Convert path to string
     * @param path The path to be converted.
     * @return String The string representation of the path
     */
    public String pathDefinining (ActorPath path){
        if(path == ActorPath.UPDOWN){
            return "1";
        } else {
            return "0";
        }
    }
    /**
     * Convert string back to path
     * @param path The path to be converted.
     * @return ActorPath The path for robot
     */
    public ActorPath pathDefinining (String path){
        if(path.equals("1")){
            return ActorPath.UPDOWN;
        } else {
            return ActorPath.LEFTRIGHT;
        }
    }

    /**
     * Convert string to Orientation
     * @param orientation The orientation to be converted.
     * @return String The string representation of the orientation
     */
    public Orientation orientationDefining (String orientation){
        if(orientation.equals("R")){
            return Orientation.TOPRIGHTFACING;
        } else {
            return Orientation.BOTTOMLEFTFACING;
        }
    }

    /**
     * Convert Orientation back to string
     * @param orientation The orientation to be converted.
     * @return String The string representation of the orientation
     */
    public String orientationDefining (Orientation orientation){
        if(orientation.equals(Orientation.TOPRIGHTFACING)){
            return "R";
        } else {
            return "L";
        }
    }
}
