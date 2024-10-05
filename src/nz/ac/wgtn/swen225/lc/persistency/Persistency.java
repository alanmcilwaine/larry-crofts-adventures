package nz.ac.wgtn.swen225.lc.persistency;

import java.io.*;
import java.util.List;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;



public class Persistency{

    private static String json;
    public static String path = "src/nz/ac/wgtn/swen225/lc/persistency/levels/";

    /**
     * Saves the given GameState object as a JSON format to a file.
     *
     * @author zhoudavi1 300652444
     * @param filename The name of the file to save the GameState to.
     * @param level The GameState object to be saved.
     */
    public static void saveGameBoard(GameBoard level){
        // convert level to JSON format
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.saveLeveltoFile(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write JSON string to file
        
        String filename = path + "level" + level.getGameState().level() + ".json";    
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
     * @author zhoudavi1 300652444
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
        String filename = path + level + "_commands.json";
        //Unique Filename (to avoid overriding when having multiple recordings)
        filename = uniqueFilename(filename);
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
     * Generates a unique filename by adding a number to the end of the filename.
     * If the filename already exists, it will keep adding numbers until it finds a unique filename.
     * For example, if filename is "level1.json" and it already exists, it will return "level1.1.json".
     * If "level1.1.json" already exists, it will return "level1.2.json", and so on.
     *
     * @author zhoudavi1 300652444 
     * @param filename The filename to be made unique.
     * @return String A unique filename.
     */
    public static String uniqueFilename(String filename){
        // check if the filename already exists
        File file = new File(filename);
        if(file.exists()){
            // if it does, add a .number to the end of the filename
            int i = 1;
            while(file.exists()){
                filename = filename.substring(0, filename.indexOf(".")) + "." + i + ".json";
                file = new File(filename);
                i++;
            }
        }
        return filename;
    }

    /**
     * Loads a GameState object from a level number.
     *
     * @author zhoudavi1 300652444
     * @param level The level of the file to load the GameBoard from.
     * @return GameBoard Loading GameBoard from a file.
     */
    public static GameBoard loadGameBoard(int levelNum){
        // load the level GameState from a level number
            //Read JSON string from file
            String filename = path + "level" + levelNum + ".json";
            return loadwithFilePath(filename);
    }

        /**
     * Loads a GameState object from a level number.
     *
     * @author zhoudavi1 300652444
     * @param level The level of the file to load the GameBoard from.
     * @return GameBoard Loading GameBoard from a file.
     */
    public static GameBoard loadwithFilePath(String filename){
        // load the level GameState from a file path
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
            GameBoard level = mapper.convertJSONtoGameBoard(json);
            return level;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a recording of actions from a file.
     *
     * @author zhoudavi1 300652444
     * @param filename The name of the file which to load gameBoard from
     * @return List<Action> Loading list of actions from a file.
     */
    public static GameBoard loadRecording(Recorder r, String filename){
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
            List<Command> actions = mapper.convertJSONtoActions(json);
            r.setCommands(actions);
            return loadwithFilePath(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}