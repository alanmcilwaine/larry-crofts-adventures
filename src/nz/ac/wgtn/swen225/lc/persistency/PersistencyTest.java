package nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistencyTest {
    private GameBoard originalState;

    @BeforeEach
    public void setUp() {
        originalState = createSampleGameState();
    }

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
        List<Robot> robots = new ArrayList<>();
        robots.add(new KillerRobot(2, 3));

        List<MovableBox> boxes = new ArrayList<>();
        boxes.add(new MovableBox(2, 1));

        return new GameBoardBuilder().addBoard(board).addBoardSize(5, 5).addTimeLeft(120).addTreasure(1).addLevel(1).addPlayer(player).addRobots(robots).addBoxes(boxes).build();
    }

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
        
        // Compare board
        for (int y = 0; y < originalState.getGameState().board().size(); y++) {
            for (int x = 0; x < originalState.getGameState().board().get(y).size(); x++) {
                Item originalItem = originalState.getGameState().board().get(y).get(x).item;
                Item loadedItem = loadedState.getGameState().board().get(y).get(x).item;
                assertEquals(originalItem.getClass(), loadedItem.getClass(), "Board items should match at position (" + x + ", " + y + ")");
            }
        }
    }

    @Test
    public void testUniqueFilename() {
        String filename = Persistency.path + "level1.json";
        String uniqueFilename = Persistency.uniqueFilename(filename);
        assertNotEquals(filename, uniqueFilename, "Unique filename should be different from the original");
        assertTrue(uniqueFilename.startsWith(Persistency.path + "level1"), "Unique filename should start with the original name");
    }
}