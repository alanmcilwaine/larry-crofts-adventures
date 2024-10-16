package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * GameLoader --- Record to handle the loading of levels in App.
 * @param app App module that contains references to domain and recorder.
 *
 * @author Alan McIlwaine 300653905
 */
public record GameLoader(App app) {
    /**
     * Checks if a save exists and loads that. Otherwise, loads level 1.
     */
    public void loadSave() {
        String latestSave = getLatestSave(new File(Persistency.path).list());
        File save = new File(Persistency.path + latestSave);
        if (save.exists() && !save.isDirectory()){
            loadRecording(Persistency.loadRecording(app.recorder(), Persistency.path + latestSave));
            app.recorder().redoAll();
        } else {
            app.startTick(Persistency.loadGameBoard(0));
        }
    }

    /**
     * Goes to the next level in the game.
     * @param level The level we go to.
     */
    public void loadLevel(int level) {
        File checkExists = new File(Persistency.path + "level" + level + ".json");
        if (!checkExists.exists()) {
            app.startTick(Persistency.loadGameBoard(1));
            return;
        }
        loadLevel(Persistency.loadGameBoard(level));
    }

    /**
     * Loads the given level from a path. Different from a recording, we set time here and there
     * are no commands.
     * @param b The level board.
     */
    public void loadLevel(GameBoard b) {
        app.recorder().setCommands(List.of());
        app.startTick(b);
    }

    /**
     * Loads a recording. Doesn't clear the list of commands that Persistency sets up.
     * @param b The level board.
     */
    public void loadRecording(GameBoard b) {
        app.startTick(b);
    }

    /**
     * Given a list of strings of the files in the directory, we find the latest save we've made.
     * @param saves List of all files in the directory.
     * @return A filename of the latest save.
     */
    private String getLatestSave(String[] saves){
        String out = Arrays.stream(saves)
                .reduce((a,b) -> extractNumber(a) < extractNumber(b) ? b : a)
                .orElseThrow(IllegalArgumentException::new);
        return out.contains("save_") ? out : "";
    }

    /**
     * Helper method for getLatestSave(). This will gather the save number of the save.
     * @param s String input.
     * @return The number inside the save string.
     */
    private int extractNumber(String s){
        if(!s.contains("save_")) return -1;
        int index = s.indexOf("_");
        return Integer.parseInt(s.substring(index+1,index+2));
    }
}
