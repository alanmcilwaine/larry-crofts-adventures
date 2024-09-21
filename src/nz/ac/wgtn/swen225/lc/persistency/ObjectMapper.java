package nz.ac.wgtn.swen225.lc.persistency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.GameState;
import nz.ac.wgtn.swen225.lc.domain.Tile;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;

public class ObjectMapper {
    /**
     * Saves the given GameState object as a JSON format to a file.
     * @author zhoudavi1 300652444
     * @param level
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    //Write Value as String throws an exception if fails
    public String saveLeveltoFile(GameState level) throws IOException {
        // write the level object to a file JSON format
        StringBuilder json = new StringBuilder();
        //Board
        json.append("{");
        json.append("\"board\":");
        json.append("[");
        for (int i = 0; i < level.board().size(); i++) {
            json.append("[");
            for (int j = 0; j < level.board().get(i).size(); j++) {
                json.append("{");
                json.append("\"x\":" + level.board().get(i).get(j).location.x() + ",");
                json.append("\"y\":" + level.board().get(i).get(j).location.y() + ",");
                json.append("\"item\":\"" + level.board().get(i).get(j).getItemOnTile().getClass().getSimpleName() + "\"");
                json.append("}");
                if (j < level.board().get(i).size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            if (i < level.board().size() - 1) {
                json.append(",");
            }
        }
        //Level
        json.append("{");
        json.append("\"level\":");
        json.append("{");
        json.append("\"name\":\"" + level.level() + "\",");
        json.append("\"Time Limit\":" + level.timeLeft() + ",");
        //Player
        json.append("\"player\":");
        json.append("{");
        json.append("\"x\":" + level.player().getLocation().x() + ",");
        json.append("\"y\":" + level.player().getLocation().y() + ",");
        json.append("\"playerFacing\":\"" + level.player().getActorFacing() + "\",");
        json.append("\"isDead\":" + level.player().isDead() + ",");
        json.append("},");
        //Robots
        json.append("\"robots\":");
        json.append("[");
        for (int i = 0; i < level.robots().size(); i++) {
            json.append("{");
            json.append("\"x\":" + level.robots().get(i).getLocation().x() + ",");
            json.append("\"y\":" + level.robots().get(i).getLocation().y() + ",");
            json.append("\"robotFacing\":\"" + level.robots().get(i).getActorFacing() + "\",");
            json.append("}");
            if (i < level.robots().size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
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
        // read the JSON string and convert it to a GameState object
        //Board
        List<List<Tile<Item>>> board = new ArrayList<>();
        String boardString = json.substring(json.indexOf("\"board\":") + 8, json.indexOf("\"level\":"));
        String[] rowStrings = boardString.split("\\[");
        for (String rowString : rowStrings) {
            if (rowString.length() > 0) {
                List<Tile<Item>> row = new ArrayList<>();
                String[] tileStrings = rowString.split("\\{");
                for (String tileString : tileStrings) {
                    if (tileString.length() > 0) {
                        int tileX = Integer.parseInt(tileString.substring(tileString.indexOf("\"x\":") + 4, tileString.indexOf("\",\"y\":")));
                        int tileY = Integer.parseInt(tileString.substring(tileString.indexOf("\"y\":") + 4, tileString.indexOf("\",\"item\":\"")));
                        String item = tileString.substring(tileString.indexOf("\"item\":\"") + 8, tileString.indexOf("\"}"));
                        row.add(new Tile<>(Item.generate(item), new Location(tileX, tileY)));
                    }
                }
                board.add(row);
            }
        }
        //Level
        String name = json.substring(json.indexOf("\"name\":\"") + 8, json.indexOf("\",\"Time Limit\":"));
        int timeLimit = Integer.parseInt(json.substring(json.indexOf("\"Time Limit\":") + 13, json.indexOf("}")));
        //Player
        int playerX = Integer.parseInt(json.substring(json.indexOf("\"x\":") + 4, json.indexOf("\",\"y\":")));
        int playerY = Integer.parseInt(json.substring(json.indexOf("\"y\":") + 4, json.indexOf("\",\"playerFacing\":")));
        String playerFacing = json.substring(json.indexOf("\"playerFacing\":\"") + 16, json.indexOf("\",\"isDead\":"));
        boolean isDead = Boolean.parseBoolean(json.substring(json.indexOf("\"isDead\":") + 9, json.indexOf("},")));
        Player player = new Player(new Location(playerX, playerY));
        //Robots
        List<Robot> robots = new ArrayList<>();
        String robotsString = json.substring(json.indexOf("\"robots\":") + 10, json.lastIndexOf("]"));
        String[] robotStrings = robotsString.split("\\{");
        for (String robotString : robotStrings) {
            if (robotString.length() > 0) {
                int robotX = Integer.parseInt(robotString.substring(robotString.indexOf("\"x\":") + 4, robotString.indexOf("\",\"y\":")));
                int robotY = Integer.parseInt(robotString.substring(robotString.indexOf("\"y\":") + 4, robotString.indexOf("\",\"robotFacing\":")));
                String robotFacing = robotString.substring(robotString.indexOf("\"robotFacing\":\"") + 15, robotString.indexOf("\"}"));
                robots.add(new Robot(new Location(robotX, robotY)));
            }
        }
        return new GameState(board, player, robots, timeLimit, name);
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
}
