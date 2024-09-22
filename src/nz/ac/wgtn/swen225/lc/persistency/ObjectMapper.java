package nz.ac.wgtn.swen225.lc.persistency;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.HashMap;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class ObjectMapper {
    static Map<String, String> gameItems = new HashMap<String, String>();
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

        gameItems.put("Exit", "X");
        gameItems.put("Info", "I");
        gameItems.put("Key", "K");
        gameItems.put("LockedDoor", "LD");
        gameItems.put("LockedExit", "LX");
        gameItems.put("NoItem", "F");
        gameItems.put("Treasure", "T");
        gameItems.put("UnlockedDoor", "UD");
        gameItems.put("Wall", "W");
    }

    /**
     * Saves the given GameState object as a JSON format to a file.
     * @author zhoudavi1 300652444
     * @param level
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    public String saveLeveltoFile(GameState level) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
    
        // Board
        List<List<String>> stringBoard = convertBoardToStrings(level);
        json.append("  \"board\": [\n");
        for (int i = 0; i < stringBoard.size(); i++) {
            json.append("    [");
            // Loop through each cell in the row
            List<String> row = stringBoard.get(i);
            for (int j = 0; j < row.size(); j++) {
                json.append("\"").append(row.get(j)).append("\"");
                if (j < row.size() - 1) {
                    json.append(", "); // Add a comma between cells, but not after the last cell
                }
            }
            json.append("]");
            if (i < stringBoard.size() - 1) {
                json.append(","); // Add a comma between rows
            }
            json.append("\n");
        }
        json.append("  ],\n");
    
        // Level
        json.append("  \"level\": {\n");
        json.append("    \"number\": \"").append(level.level()).append("\",\n");
        json.append("    \"Time Limit\": ").append(level.timeLeft()).append("\n");
        json.append("  }\n"); // Closing the level object

        //Get info object from the board
        Info info = new Info("Null");
        int x = 0;
        int y = 0;
        for (List<Tile<Item>> row : level.board()) {
            for (Tile<Item> tile : row) {
                if (tile.item instanceof Info) {
                    info = ((Info) tile.item);
                    x = row.indexOf(tile);
                    y = level.board().indexOf(row);
                }
            }
        }

        // Info
        json.append("  \"info\": {\n");
        json.append("    \"info\": \"").append(info.info()).append("\"\n");
        json.append("    \"x\": \"").append(x).append("\",\n");
        json.append("    \"y\": \"").append(y).append("\"\n");
        json.append("  }\n"); // Closing the info object

        json.append("}\n"); // Closing the entire JSON object
    
        return json.toString();
    }

    /**
     * Saves the given list of actions as a JSON format to a file.
     * @author zhoudavi1 300652444
     * @param actions
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    //Write Action List as String throws an exception if fails
    public String saveCommandstoFile(List<Command> actions,int level) throws IOException {
        // write the list of actions to a file JSON format
        StringBuilder json = new StringBuilder();
        json.append("{");
        String levelFile = "levels/level" + level + ".json";
        json.append("\"levelFileName\":" + levelFile + ",");
        json.append("\"actions\":");
        json.append("[");
        for (Command action : actions) {
            json.append("{");
            json.append("\"actionType\":\"" + action.getSaveData() + "\",");
            json.append("}");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Read the given GameState object as a JSON format to a file.
     *
     * @author zhoudavi1 300652444
     * @param json The string to be converted to a GameState object.
     * @return GameState The GameState object to be loaded.
     */
    public GameState convertJSONtoGameState(String json) {
        // Parse board
        List<List<Tile<Item>>> board = new ArrayList<>();
        Player player = null;
        List<Robot> robots = new ArrayList<>();
    
        // Find the board section
        int boardStart = json.indexOf("\"board\": [");
        if (boardStart == -1) {
            throw new IllegalArgumentException("Board not found in JSON");
        }
        String boardString = json.substring(boardStart + 10, json.indexOf("],\n  \"level\""));
    
        // Ensure we are correctly capturing the board rows
        String[] rowStrings = boardString.split("\\],\\s*\\[");
        for (int y = 0; y < rowStrings.length; y++) {
            List<Tile<Item>> row = new ArrayList<>();
            String[] cellStrings = rowStrings[y].replace("[", "").replace("]", "").split(",\\s*");
            
            for (int x = 0; x < cellStrings.length; x++) {
                String cellCode = cellStrings[x].replace("\"", "").trim(); // Trim whitespace
                String[] parts = cellCode.split("-"); // Find the dash to indicate entity
                Item item = createItemFromCode(parts[0]); // Create item from the first part of the code
                
                // Check if the cell contains the player (-P)
                if (parts.length > 1 && parts[1].equals("P")) {
                    player = new Player(new Location(x, y));
                }
                
                // Check if the cell contains a robot (-R)
                if (parts.length > 1 && parts[1].equals("R")) {
                    robots.add(new KillerRobot(new Location(x, y)));
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
        String levelString = json.substring(levelStart + 10);
        int levelNumber = Integer.parseInt(levelString.substring(levelString.indexOf("\"number\": \"") + 11, levelString.indexOf("\",\n")));
        int timeLimit = Integer.parseInt(levelString.substring(levelString.indexOf("\"Time Limit\": ") + 14, levelString.indexOf("\n  }")));

        // Parse info
        int infoStart = json.indexOf("\"info\": {");
        if (infoStart == -1) {
            throw new IllegalArgumentException("Info not found in JSON");
        }
        String infoString = json.substring(infoStart + 9); // Skip past `"info": {`
        int infoEnd = infoString.indexOf("}"); // Find the closing brace
        if (infoEnd == -1) {
            throw new IllegalArgumentException("Closing brace for info not found in JSON");
        }
        infoString = infoString.substring(0, infoEnd); // Get the content before the closing brace

        System.out.println("Info String: " + infoString); // Debug print

        String infoText = extractValue(infoString, "\"info\": \"");
        int x = Integer.parseInt(extractValue(infoString, "\"x\": \""));
        int y = Integer.parseInt(extractValue(infoString, "\"y\": \""));

        board.get(y).get(x).item = new Info(infoText);

        return new GameState(board, player, robots, timeLimit, levelNumber);
    }

    // Helper method to extract values
    private String extractValue(String jsonString, String key) {
        int keyStart = jsonString.indexOf(key);
        if (keyStart == -1) {
            throw new IllegalArgumentException("Key not found in info: " + key);
        }
        keyStart += key.length();
        int valueEnd = jsonString.indexOf("\"", keyStart);
        if (valueEnd == -1) {
            throw new IllegalArgumentException("Value not found for key: " + key);
        }
        return jsonString.substring(keyStart, valueEnd);
    }  
    
    /**
     * Read the given list of actions as a JSON format from a file.
     *
     * @author zhoudavi1 300652444
     * @param json The string to be converted to a list of actions.
     * @return List<Action> The list of actions to be loaded.
     */
    public List<Command> convertJSONtoActions(String json) {
        // read the JSON string and convert it to a list of actions
        List<Command> actions = new ArrayList<>();
        String actionsString = json.substring(json.indexOf("\"actions\":") + 11, json.lastIndexOf("]"));
        String[] actionStrings = actionsString.split("\\{");
        for (String actionString : actionStrings) {
            if (actionString.length() > 0) {
                String actionType = actionString.substring(actionString.indexOf("\"actionType\":\"") + 14, actionString.indexOf("\"}"));
                actions.add(Command.generate(actionType));
            }
        }
        return actions;
    }

    public String gameItemCode(Item item) {
        // Get the base code from the item class name
        String itemClassName = item.getClass().getSimpleName();
        String baseCode = gameItems.get(itemClassName);
    
        if (baseCode == null) {
            throw new IllegalArgumentException("Unknown item: " + itemClassName);
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
    
    private String getColorCode(ItemColor color) {
        // Map the ItemColor to the appropriate code
        return switch (color) {
            case BLUE -> "#B";
            case RED -> "#R";
            default -> ""; // No color, return an empty string
        };
    }
    

    private Item createItemFromCode(String code) {
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
                case "B": color = ItemColor.BLUE; break;
                case "R": color = ItemColor.RED; break;
                default: throw new IllegalArgumentException("Unknown color code: " + parts[1]);
            }
        }

        // Get the constructor for the item
        Function<ItemColor, Item> constructor = itemConstructors.get(itemCode);
        if (constructor == null) {
            throw new IllegalArgumentException("Unknown item code: " + itemCode);
        }

        return constructor.apply(color);
    }

    private Item createInfo() {
        return new Info("Info");
    }

    /**
     * 2D representation of the game board.
     * Convert the board of Tile<Item> to List<List<String>>.
     * @author zhoudavi1 300652444
     * @param level The level to be converted.
     * @return List<List<String>> The converted level.
     */
    public List<List<String>> convertBoardToStrings(GameState level) {
        //Convert the board of Tile<Item> to List<List<String>>
        List<List<String>> stringBoard = level.board().stream()
            .map((List<Tile<Item>> row) -> row.stream()
                .map((Tile<Item> tile) -> gameItemCode(tile.item)) // Convert each Tile<Item> to the item's string code
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());
    
        //Place the player on the board at their X and Y position
        int playerX = level.player().getLocation().x();
        int playerY = level.player().getLocation().y();
    
        //Modify the specific tile to include "P" for player
        stringBoard.get(playerY).set(playerX, stringBoard.get(playerY).get(playerX) + "-P");

        //Robot
        for (Robot robot : level.robots()) {
            int robotX = robot.getLocation().x();
            int robotY = robot.getLocation().y();
            stringBoard.get(robotY).set(robotX, stringBoard.get(robotY).get(robotX) + "-R");
        }
    
        return stringBoard;
    }
}
