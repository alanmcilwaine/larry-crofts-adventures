package nz.ac.wgtn.swen225.lc.persistency.Tests;

import nz.ac.wgtn.swen225.lc.app.Inputs.Command;
import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import nz.ac.wgtn.swen225.lc.persistency.ObjectMapper;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Persistency class
 * @author zhoudavi1 300652444
 */
public class PersistencyTest {
    private GameBoard originalState;

    @BeforeEach
    public void setUp() {
        originalState = createSampleGameState();
    }

    /**
     * Create a sample game state for testing
     * @return GameBoard
     */
    private static GameBoard createSampleGameState() {
        List<List<Tile<Item>>> board = new ArrayList<>();
        for (int y = 0; y < 5; y++) {
            List<Tile<Item>> row = new ArrayList<>();
            for (int x = 0; x < 5; x++) {
                Item item = (x == 0 || y == 0 || x == 4 || y == 4) ? new Wall() : new NoItem();
                row.add(new Tile<>(item, new Location(x, y)));
            }
            board.add(row);
        }

        // Add some items to the board
        board.get(1).get(3).item = new Key(ItemColor.RED);
        board.get(3).get(3).item = new LockedDoor(ItemColor.RED);
        board.get(2).get(2).item = new Info("Hello chap!");
        board.get(2).get(4).item = new OneWayTeleport(new Location(2, 4));
        
        //Treasure
        board.get(3).get(2).item = new Treasure();

        //Locked Exit
        board.get(3).get(4).item = new LockedExit();

        Player player = new Player(new Location(1, 1));
        player.addTreasure(new Key(ItemColor.RED));
        List<Robot> robots = new ArrayList<>();
        robots.add(new KillerRobot(2, 3));

        List<MovableBox> boxes = new ArrayList<>();
        boxes.add(new MovableBox(2, 1));

        return new GameBoardBuilder().addBoard(board).addBoardSize(5, 5).addTimeLeft(120).addTreasure(1).addLevel(1).addPlayer(player).addRobots(robots).addBoxes(boxes).build();
    }

    /**
     * Test reading JSON from a file
     * @throws IOException
     */
    @Test
    public void testReadJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Convert GameState to JSON
        String json = mapper.saveLeveltoFile(originalState);

        // Convert JSON back to GameBoard
        GameBoard convertedBoard = mapper.convertJSONtoGameBoard(json);

        // Compare original and converted GameState
        assertEquals(originalState.getGameState().level(), convertedBoard.getGameState().level(), "Level number should match");
        assertEquals(originalState.getGameState().timeLeft(), convertedBoard.getGameState().timeLeft(), "Time left should match");
        assertEquals(originalState.getGameState().player().getLocation(), convertedBoard.getGameState().player().getLocation(), "Player location should match");
        assertEquals(originalState.getGameState().robots().size(), convertedBoard.getGameState().robots().size(), "Number of robots should match");
        assertEquals(originalState.getGameState().robots().get(0).getLocation(), convertedBoard.getGameState().robots().get(0).getLocation(), "Robot location should match");
        assertEquals(originalState.getGameState().totalTreasure(), convertedBoard.getGameState().totalTreasure(), "Number of treasures should match");
        assertEquals(originalState.getGameState().boxes().size(), convertedBoard.getGameState().boxes().size(), "Number of boxes should match");

        //Compare Player Inventory
        for (int i = 0; i < originalState.getGameState().player().getTreasure().size(); i++) {
            Item originalItem = originalState.getGameState().player().getTreasure().get(i);
            Item convertedItem = convertedBoard.getGameState().player().getTreasure().get(i);
            assertEquals(
                    originalItem.getClass(),
                    convertedItem.getClass(),
                    "Item at index " + i + " should be of the same class"
            );
        }

        // Compare board
        for (int y = 0; y < originalState.getGameState().board().size(); y++) {
            for (int x = 0; x < originalState.getGameState().board().get(y).size(); x++) {
                Item originalItem = originalState.getGameState().board().get(y).get(x).item;
                Item convertedItem = convertedBoard.getGameState().board().get(y).get(x).item;
                assertEquals(originalItem.getClass(), convertedItem.getClass(), "Board items should match at position (" + x + ", " + y + ")");
            }
        }

        //Save the converted GameBoard to a file
        Persistency.saveGameBoard(convertedBoard);
    }

    /**
     * Test saving and loading a GameState
     * @throws IOException
     */
    @Test
    public void testLoadGameState() throws IOException {
        Persistency.saveGameBoard(originalState);
        GameBoard loadedState = Persistency.loadGameBoard(1);

        //Check if the loaded GameState is the same as the original GameState
        assertEquals(originalState.getGameState().level(), loadedState.getGameState().level(), "Level number should match");
        assertEquals(originalState.getGameState().timeLeft(), loadedState.getGameState().timeLeft(), "Time left should match");
        assertEquals(originalState.getGameState().player().getLocation(), loadedState.getGameState().player().getLocation(), "Player location should match");
        assertEquals(originalState.getGameState().robots().size(), loadedState.getGameState().robots().size(), "Number of robots should match");
        assertEquals(originalState.getGameState().robots().get(0).getLocation(), loadedState.getGameState().robots().get(0).getLocation(), "Robot location should match");
        assertEquals(originalState.getGameState().totalTreasure(), loadedState.getGameState().totalTreasure(), "Number of treasures should match");

        //Compare Player Inventory
        for (int i = 0; i < originalState.getGameState().player().getTreasure().size(); i++) {
            Item originalItem = originalState.getGameState().player().getTreasure().get(i);
            Item convertedItem = loadedState.getGameState().player().getTreasure().get(i);
            assertEquals(
                    originalItem.getClass(),
                    convertedItem.getClass(),
                    "Item at index " + i + " should be of the same class"
            );
        }

        // Compare board
        for (int y = 0; y < originalState.getGameState().board().size(); y++) {
            for (int x = 0; x < originalState.getGameState().board().get(y).size(); x++) {
                Item originalItem = originalState.getGameState().board().get(y).get(x).item;
                Item loadedItem = loadedState.getGameState().board().get(y).get(x).item;
                assertEquals(originalItem.getClass(), loadedItem.getClass(), "Board items should match at position (" + x + ", " + y + ")");
            }
        }
    }

    /**
     * Test making a filename unique
     */
    @Test
    public void testUniqueFilename() {
        String filename = Persistency.path + "level1.json";
        String uniqueFilename = Persistency.uniqueFilename(filename);
        File file = new File(filename);
        if(file.exists()){
            int i = 1;
            while(file.exists()){
                filename = filename.substring(0, filename.indexOf(".")) + "." + i + ".json";
                file = new File(filename);
                i++;
            }
        }
        assertEquals(uniqueFilename, filename, "Unique filename should match expected result");
    }

    /**
     * Test saving and loading commands
     */
    @Test
    public void testSaveCommands(){
        List<Command> actions = new ArrayList<>();
        actions.add(Command.generate("Right"));
        actions.add(Command.generate("Left"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Up"));

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.saveCommandstoFile(actions, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Command> loadedCommands = mapper.convertJSONtoActions(json);

        assertEquals(actions.size(), loadedCommands.size(), "Number of commands should match");
        for (int i = 0; i < actions.size(); i++) {
            assertEquals(actions.get(i).direction(), loadedCommands.get(i).direction(), "Direction should match");
        }
    }
    /**
     * Test saving commands in Persistency
     */
    @Test
    public void testSaveCommandsInPersistency(){
        List<Command> actions = new ArrayList<>();
        actions.add(Command.generate("Right"));
        actions.add(Command.generate("Left"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Up"));
        Persistency.saveCommands(actions, 1);
    }

    /**
     * Test loading commands
     */
    @Test
    public void loadRecording(){
        MockRecorder recorder = new MockRecorder();
        GameBoard b = Persistency.loadRecording(recorder ,Persistency.path + "1_commands.json");
        assertNotNull(b, "GameBoard should not be null");
        //Assert commands are loaded correctly
        assertEquals(5, recorder.getCommands().size(), "Number of commands should match");
        assertEquals(Command.Right, recorder.getCommands().get(0), "First command should be Right");
        assertEquals(Command.Left, recorder.getCommands().get(1), "Second command should be Left");
        assertEquals(Command.Down, recorder.getCommands().get(2), "Third command should be Down");
        assertEquals(Command.Down, recorder.getCommands().get(3), "Fourth command should be Down");
        assertEquals(Command.Up, recorder.getCommands().get(4), "Fifth command should be Up");
    }

    /**
     * Test saving progress
     */
    @Test
    public void testSaveProgress(){
        List<Command> actions = new ArrayList<>();
        actions.add(Command.generate("Right"));
        actions.add(Command.generate("Left"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Up"));
        GameBoard gameBoard = createSampleGameState();
        Persistency.saveProgress(actions, 1);
    }

    /**
     * Test load from file path
     */
    @Test
    public void testLoadFromFile(){
        GameBoard gameBoard = Persistency.loadwithFilePath(Persistency.path + "level1.json");
        assertNotNull(gameBoard, "GameBoard should not be null");
    }

}