import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {
    /**
     * Saves the given GameState object as a JSON format to a file.
     * @param level
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    //Write Value as String throws an exception if fails
    public String writeValueAsString(GameState level) throws IOException {
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
     * @param actions
     * @return String The JSON string to be saved.
     * @throws IOException
     */
    //Write Action List as String throws an exception if fails
    public String writeValueAsString(List<Command> actions) throws IOException {
        // write the list of actions to a file JSON format
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"levelNumber\":" + actions.get(0).getLevelNumber() + ",");
        json.append("\"actions\":");
        json.append("[");
        for (Command action : actions) {
            json.append("{");
            json.append("\"actionType\":\"" + action.getActionType() + "\",");
            json.append("}");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Saves the given GameState object as a JSON format to a file.
     *
     * @author zhoudavi1
     * @param json The string to be converted to a GameState object.
     * @return GameState The GameState object to be loaded.
     */
    public GameState readValue(String json) {
        // read the JSON string and convert it to a GameState object
        String name = json.substring(json.indexOf("\"name\":\"") + 8, json.indexOf("\",\"Time Limit\":"));
        int timeLimit = Integer.parseInt(json.substring(json.indexOf("\"Time Limit\":") + 13, json.indexOf("}")));
        return new GameState(name, timeLimit);
    }

    /**
     * Saves the given list of actions as a JSON format to a file.
     *
     * @author zhoudavi1
     * @param json The string to be converted to a list of actions.
     * @return List<Action> The list of actions to be loaded.
     */
    public List<Command> readActionValue(String json) {
        // read the JSON string and convert it to a list of actions
        List<Command> actions = new ArrayList<>();
        String levelNumber = json.substring(json.indexOf("\"levelNumber\":") + 14, json.indexOf(",\"actions\":"));
        String actionsString = json.substring(json.indexOf("\"actions\":") + 11, json.lastIndexOf("]"));
        String[] actionStrings = actionsString.split("\\{");
        for (String actionString : actionStrings) {
            if (actionString.length() > 0) {
                String actionType = actionString.substring(actionString.indexOf("\"actionType\":\"") + 14, actionString.indexOf("\"}"));
                actions.add(new Command(actionType, Integer.parseInt(levelNumber), 0));
            }
        }
        return actions;
    }
}
