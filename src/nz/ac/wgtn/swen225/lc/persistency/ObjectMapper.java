package nz.ac.wgtn.swen225.lc.persistency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.GameState;

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
        json.append("{");
        json.append("\"level\":");
        json.append("{");
        json.append("\"name\":\"" + level.getName() + "\",");
        json.append("\"Time Limit\":" + level.getTimeLimit() + ",");
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
        String name = json.substring(json.indexOf("\"name\":\"") + 8, json.indexOf("\",\"Time Limit\":"));
        int timeLimit = Integer.parseInt(json.substring(json.indexOf("\"Time Limit\":") + 13, json.indexOf("}")));
        return new GameState(name, timeLimit);
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
