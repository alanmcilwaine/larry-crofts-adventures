package nz.ac.wgtn.swen225.lc.persistency;

import java.io.*;
import java.util.List;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;


public class Persistency{

    private static String json;

    /**
     * Saves the given GameState object as a JSON format to a file.
     *
     * @author zhoudavi1
     * @param filename The name of the file to save the GameState to.
     * @param level The GameState object to be saved.
     */
    public static void saveGameState(String filename, GameState level){
        // convert level to JSON format
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.saveLeveltoFile(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write JSON string to file
        filename = "levels/level" + level.getName() + ".json";
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
     * @param level The level of the actions to be saved.
     * @param actions Saving the list of actions to a file.
     */
    public static void saveCommands(List<Command> actions, int level){
        // save the list of actions to a file
        // convert level to JSON format
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.saveCommandstoFile(actions, level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write JSON string to file
        String filename = "levels/level" + level + ".json";
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
    public static GameState loadGameState(String filename){
        // load the level GameState from a file
        try{
            //Read JSON string from file
            filename = "levels/" + filename;
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
            GameState level = mapper.convertJSONtoGameState(json);
            return level;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a recording of actions from a file.
     *
     * @author zhoudavi1
     * @param filename The name of the file to save the GameState to.
     * @return List<Action> Loading list of actions from a file.
     */
    public static GameState loadRecording(Recorder r, String filename){
        // load the list of actions from a file
        try{
            //Read JSON string from file
            filename = "levels/" + filename;
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
            List<Command> actions = mapper.convertJSONtoActions(json);
            r.setCommands(actions);
            return loadGameState(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}