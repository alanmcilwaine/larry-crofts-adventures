package nz.ac.wgtn.swen225.lc.persistency;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

public class ObjectMapper {
    static Map<String, String> gameItems = new HashMap<String, String>();
    static{
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
            json.append("\n");
        }
        json.append("  ],\n");

        // Level
        json.append("  \"level\": {\n");
        json.append("    \"number\": \"").append(level.level()).append("\",\n");
        json.append("    \"Time Limit\": ").append(level.timeLeft()).append(",\n");

        // Player
        json.append("    \"player\": {\n");
        json.append("      \"x\": ").append(level.player().getLocation().x()).append(",\n");
        json.append("      \"y\": ").append(level.player().getLocation().y()).append("\n");
        json.append("    },\n");

        // Robots
        json.append("    \"robots\": [\n");
        for (int i = 0; i < level.robots().size(); i++) {
            Robot robot = level.robots().get(i);
            json.append("      {\n");
            json.append("        \"x\": ").append(robot.getLocation().x()).append(",\n");
            json.append("        \"y\": ").append(robot.getLocation().y()).append("\n");
            json.append("      }");
            if (i < level.robots().size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("    ]\n");
        json.append("  }\n");
        json.append("}");

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
        String boardString = json.substring(json.indexOf("\"board\": [") + 10, json.indexOf("],\n  \"level\""));
        String[] rowStrings = boardString.split("\\],\\s*\\[");
        for (String rowString : rowStrings) {
            List<Tile<Item>> row = new ArrayList<>();
            String[] cellStrings = rowString.replace("[", "").replace("]", "").split(",\\s*");
            for (int x = 0; x < cellStrings.length; x++) {
                String cellCode = cellStrings[x].replace("\"", "");
                String[] parts = cellCode.split("-");
                Item item = createItemFromCode(parts[0]);
                row.add(new Tile<>(item, new Location(x, row.size())));
            }
            board.add(row);
        }

        // Parse level info
        String levelString = json.substring(json.indexOf("\"level\": {") + 10);
        int levelNumber = Integer.parseInt(levelString.substring(levelString.indexOf("\"number\": \"") + 11, levelString.indexOf("\",\n")));
        int timeLimit = Integer.parseInt(levelString.substring(levelString.indexOf("\"Time Limit\": ") + 13, levelString.indexOf(",\n")));

        // Parse player
        String playerString = levelString.substring(levelString.indexOf("\"player\": {"));
        int playerX = Integer.parseInt(playerString.substring(playerString.indexOf("\"x\": ") + 5, playerString.indexOf(",\n")));
        int playerY = Integer.parseInt(playerString.substring(playerString.indexOf("\"y\": ") + 5, playerString.indexOf("\n    }")));
        Player player = new Player(new Location(playerX, playerY));

        // Parse robots
        List<Robot> robots = new ArrayList<>();
        String robotsString = levelString.substring(levelString.indexOf("\"robots\": [") + 11);
        String[] robotStrings = robotsString.split("\\{");
        for (String robotString : robotStrings) {
            if (robotString.contains("\"x\":")) {
                int robotX = Integer.parseInt(robotString.substring(robotString.indexOf("\"x\": ") + 5, robotString.indexOf(",")));
                int robotY = Integer.parseInt(robotString.substring(robotString.indexOf("\"y\": ") + 5, robotString.indexOf("\n      }")));
                robots.add(new KillerRobot(new Location(robotX, robotY)));
            }
        }

        return new GameState(board, player, robots, timeLimit, levelNumber);
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

    //gameItem Checker
    public String gameItemCode(String item) {
        return gameItems.get(item);
    }

    private Item createItemFromCode(String code) {
        switch (code) {
            case "X": return new Exit();
            case "I": return new Info();
            case "K": return new Key();
            case "LD": return new LockedDoor();
            case "LX": return new LockedExit();
            case "NI": return new NoItem();
            case "T": return new Treasure();
            case "UD": return new UnlockedDoor();
            case "W": return new Wall();
            default: throw new IllegalArgumentException("Unknown item code: " + code);
        }
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
                .map((Tile<Item> tile) -> gameItemCode(tile.getItemOnTile())) // Convert each Tile<Item> to the item's string code
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
