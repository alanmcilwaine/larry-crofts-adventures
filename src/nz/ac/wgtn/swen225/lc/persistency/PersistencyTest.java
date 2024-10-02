package nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.GameBoardBuilder;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistencyTest {
    public static void main(String[] args) throws IOException {
        GameBoard gameState = testReadJSON();
        testLoadGameState(gameState);
        testUniqueFilename();
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
        
        //Treasure
        board.get(3).get(2).item = new Treasure();

        //Locked Exit
        board.get(3).get(4).item = new LockedExit();

        Player player = new Player(new Location(1, 1));
        List<Robot> robots = new ArrayList<>();
        robots.add(new KillerRobot(2, 3));
        
        return new GameBoardBuilder().addBoard(board).addBoardSize(5, 5).addTimeLeft(120).addTreasure(1).addLevel(1).addPlayer(player).addRobots(robots).build();
    }

    private static GameBoard testReadJSON() throws IOException{
        // Create a sample GameState
        System.out.println("Reading JSON:");

        GameBoard originalState = createSampleGameState();

        ObjectMapper mapper = new ObjectMapper();

        // Convert GameState to JSON
        String json = mapper.saveLeveltoFile(originalState);
        System.out.println("JSON Representation of GameBoard:\n" + json);

        // Convert JSON back to GameBoard
        GameBoard convertedBoard = mapper.convertJSONtoGameBoard(json);

        // Compare original and converted GameState
        System.out.println("Level number matches: " + (originalState.getGameState().level() == convertedBoard.getGameState().level()));
        System.out.println("Time left matches: " + (originalState.getGameState().timeLeft() == convertedBoard.getGameState().timeLeft()));
        System.out.println("Player location matches: " + originalState.getGameState().player().getLocation().equals(convertedBoard.getGameState().player().getLocation()));
        System.out.println("Number of robots matches: " + (originalState.getGameState().robots().size() == convertedBoard.getGameState().robots().size()));
        System.out.println("Robot location matches: " + originalState.getGameState().robots().get(0).getLocation().equals(convertedBoard.getGameState().robots().get(0).getLocation()));
        System.out.println("Number of treasures matches: " + (originalState.getGameState().totalTreasure() == convertedBoard.getGameState().totalTreasure()));

        // Compare board
        boolean boardMatches = true;
        for (int y = 0; y < originalState.getGameState().board().size(); y++) {
            for (int x = 0; x < originalState.getGameState().board().get(y).size(); x++) {
                Item originalItem = originalState.getGameState().board().get(y).get(x).item;
                Item convertedItem = convertedBoard.getGameState().board().get(y).get(x).item;
                if (!originalItem.getClass().equals(convertedItem.getClass())) {
                    boardMatches = false;
                    break;
                }
            }
        }
        System.out.println("Board matches: " + boardMatches);

        //Save the converted GameState to a file
        Persistency.saveGameBoard(convertedBoard);
        return convertedBoard;
    }

    //Test loading a GameState from a file
    private static void testLoadGameState(GameBoard original) throws IOException{
        System.out.println("Loading GameState from file:--------------------------------------");
        GameBoard loadedState = Persistency.loadGameBoard(1);
        //Check if the loaded GameState is the same as the original GameState
        System.out.println("Level number matches: " + (original.getGameState().level() == loadedState.getGameState().level()));
        System.out.println("Time left matches: " + (original.getGameState().timeLeft() == loadedState.getGameState().timeLeft()));
        System.out.println("Player location matches: " + original.getGameState().player().getLocation().equals(loadedState.getGameState().player().getLocation()));
        System.out.println("Number of robots matches: " + (original.getGameState().robots().size() == loadedState.getGameState().robots().size()));
        System.out.println("Robot location matches: " + original.getGameState().robots().get(0).getLocation().equals(loadedState.getGameState().robots().get(0).getLocation()));
        System.out.println("Number of treasures matches: " + (original.getGameState().totalTreasure() == loadedState.getGameState().totalTreasure()));
        
        // Compare board
        boolean boardMatches = true;
        for (int y = 0; y < original.getGameState().board().size(); y++) {
            for (int x = 0; x < original.getGameState().board().get(y).size(); x++) {
                Item originalItem = original.getGameState().board().get(y).get(x).item;
                Item loadedItem = loadedState.getGameState().board().get(y).get(x).item;
                if (!originalItem.getClass().equals(loadedItem.getClass())) {
                    boardMatches = false;
                    break;
                }
            }
        }
        System.out.println("Board matches: " + boardMatches);
    }

    //Test Unique filename generation
    private static void testUniqueFilename(){
        System.out.println("Testing unique filename generation:");
        String filename = Persistency.path + "level1.json";
        System.out.println("Original filename: " + filename);
        filename = Persistency.uniqueFilename(filename);
        System.out.println("Unique filename: " + filename);
    }

}