package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.GameBoard;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;

import java.io.File;
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
     * @return A GameBoard of the save or level.
     */
    public GameBoard loadSave() {
        File save = new File(Persistency.path + "save.json"); // We open the file to see if it exists
        if (save.exists() && !save.isDirectory()){
            return Persistency.loadwithFilePath(Persistency.path + "save.json");
        } else {
            return Persistency.loadGameBoard(0);
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
        app.recorder().setCommands(List.of());
        app.startTick(Persistency.loadGameBoard(level));
    }

    /**
     * Loads the given level from a path.
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
}
