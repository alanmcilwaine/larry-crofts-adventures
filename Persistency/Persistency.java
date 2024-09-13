import java.io.*;
import java.util.List; 

public class Persistency{

    private static String json;

    /**
     * Saves the given GameState object as a JSON format to a file.
     *
     * @author zhoudavi1
     * @param filename The name of the file to save the GameState to.
     * @param level The GameState object to be saved.
     */
    public static void saveToFile(String filename, GameState level){
        // convert level to JSON format
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write JSON string to file
        File file = new File(filename);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the given list of actions as a JSON format to a file.
     *
     * @author zhoudavi1
     * @param filename The name of the file to save the GameState to.
     * @param actions Saving the list of actions to a file.
     */
    public static void saveFromActions(String filename, List<Command> actions){
        // save the list of actions to a file
        // convert level to JSON format
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(actions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write JSON string to file
        File file = new File(filename);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a GameState object from a file.
     *
     * @author zhoudavi1
     * @param filename The name of the file to save the GameState to.
     * @return GameState Loading GameState from a file.
     */
    public static GameState loadFromFile(String filename){
        // load the level GameState from a file
        try{
            //Read JSON string from file
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            json = stringBuilder.toString();
            //Convert JSON string to GameState object
            ObjectMapper mapper = new ObjectMapper();
            GameState level = mapper.readValue(json);
            return level;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a list of actions for recorder
     *
     * @author zhoudavi1
     * @param filename The name of the file to save the GameState to.
     * @return List<Action> Loading list of actions from a file.
     */
    public static List<Command> loadActionsFromFile(String filename){
        // load the list of actions from a file
        try{
            //Read JSON string from file
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            json = stringBuilder.toString();
            //Convert JSON string to List of Action objects
            ObjectMapper mapper = new ObjectMapper();
            List<Command> actions = mapper.readActionValue(json);
            return actions;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}