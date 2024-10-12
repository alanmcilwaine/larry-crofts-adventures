package nz.ac.wgtn.swen225.lc.persistency.Tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.*;
import nz.ac.wgtn.swen225.lc.persistency.ObjectMapper;
import nz.ac.wgtn.swen225.lc.persistency.Persistency;

import static nz.ac.wgtn.swen225.lc.persistency.Persistency.path;

public class PersistencyPrint {
    public static void main(String[] args) throws IOException {
        GameBoard gameBoard = testReadJSON();
        testLoadGameBoard(gameBoard);
        testUniqueFilename();
        testCommands();
    }
    /**
     * Create a sample GameBoard object
     * @author zhoudavi1 300652444
     * @return The GameBoard object.
     * @throws IOException
     */
    private static GameBoard createSampleGameBoard() {
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
        //board.get(2).get(2).item = new LaserSource(Direction.UP, true, 2, 2);
        
        //Treasure
        board.get(3).get(2).item = new Treasure();

        //Locked Exit
        board.get(3).get(4).item = new LockedExit();

        Player player = new Player(new Location(1, 1));
        //player.addTreasure(new Key(ItemColor.RED));
        //player.addTreasure(new Key(ItemColor.BLUE));
        List<Robot> robots = new ArrayList<>();
        KillerRobot r = new KillerRobot(2, 3);
        r.setActorPath(ActorPath.UPDOWN);
        robots.add(r);

        List<MovableBox> boxes = new ArrayList<>();
        boxes.add(new MovableBox(2, 1));
        boxes.add(new Mirror(Orientation.TWO, 2,4));
        
        return new GameBoardBuilder().addBoard(board).addBoardSize(5, 5).addTimeLeft(120).addTreasure(1).addLevel(1).addPlayer(player).addRobots(robots).addBoxes(boxes).build();
    }

    /**
     * Create a sample GameBoard object and test the conversion to JSON and back.
     * @author zhoudavi1 300652444
     * @return The converted GameBoard object.
     * @throws IOException
     */
    private static GameBoard testReadJSON() throws IOException{
        // Create a sample GameState
        System.out.println("Reading JSON:");

        GameBoard originalState = createSampleGameBoard();

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
        System.out.println("Number of boxes matches: " + (originalState.getGameState().boxes().size() == convertedBoard.getGameState().boxes().size()));

        // Compare Player Inventory
        System.out.println("Original Player Inventory: " + originalState.getGameState().player().getTreasure());
        System.out.println("Converted Player Inventory: " + convertedBoard.getGameState().player().getTreasure());
        boolean inventoryMatches = true;
        for (int i = 0; i < originalState.getGameState().player().getTreasure().size(); i++) {
            Item originalItem = originalState.getGameState().player().getTreasure().get(i);
            Item convertedItem = convertedBoard.getGameState().player().getTreasure().get(i);
            if (!originalItem.getClass().equals(convertedItem.getClass())) {
                inventoryMatches = false;
                break;
            }
        }

        System.out.println("Player inventory matches: " + inventoryMatches);

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

    /**
     * Test loading a GameBoard object from a file and compare it with the original GameBoard object.
     * @author zhoudavi1 300652444
     * @param original The original GameBoard object.
     * @throws IOException
     */
    private static void testLoadGameBoard(GameBoard original) throws IOException{
        System.out.println("Loading GameState from file:--------------------------------------");
        GameBoard loadedState = Persistency.loadGameBoard(1);
        //Check if the loaded GameState is the same as the original GameState
        System.out.println("Level number matches: " + (original.getGameState().level() == loadedState.getGameState().level()));
        System.out.println("Time left matches: " + (original.getGameState().timeLeft() == loadedState.getGameState().timeLeft()));
        System.out.println("Player location matches: " + original.getGameState().player().getLocation().equals(loadedState.getGameState().player().getLocation()));
        System.out.println("Number of robots matches: " + (original.getGameState().robots().size() == loadedState.getGameState().robots().size()));
        System.out.println("Robot location matches: " + original.getGameState().robots().get(0).getLocation().equals(loadedState.getGameState().robots().get(0).getLocation()));
        System.out.println("Number of treasures matches: " + (original.getGameState().totalTreasure() == loadedState.getGameState().totalTreasure()));
        System.out.println("Number of boxes matches: " + (original.getGameState().boxes().size() == loadedState.getGameState().boxes().size()));

        // Compare Player Inventory
        boolean inventoryMatches = true;
        for (int i = 0; i < original.getGameState().player().getTreasure().size(); i++) {
            Item originalItem = original.getGameState().player().getTreasure().get(i);
            Item loadedItem = loadedState.getGameState().player().getTreasure().get(i);
            if (!originalItem.getClass().equals(loadedItem.getClass())) {
                inventoryMatches = false;
                break;
            }
        }

        System.out.println("Player inventory matches: " + inventoryMatches);

        //Compare board
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

    /**
     * Test the generation of a unique filename for a file path.
     * @autor zhoudavi1 300652444
     */
    private static void testUniqueFilename(){
        System.out.println("Testing unique filename generation:");
        String filename = path + "level1.json";
        System.out.println("Original filename: " + filename);
        filename = Persistency.uniqueFilename(filename);
        System.out.println("Unique filename: " + filename);
    }

    /**
     * Test saving and loading commands.
     * @autor zhoudavi1 300652444
     */
    private static void testCommands(){
        List<Command> actions = new ArrayList<>();
        actions.add(Command.generate("Right"));
        actions.add(Command.generate("Left"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Down"));
        actions.add(Command.generate("Up"));
        testSaveCommands(actions);
        testLoadCommands(actions);
    }

    /**
     * Test saving commands
     * @autor zhoudavi1 300652444
     * @param actions The list of commands to be saved.
     */
    private static void testSaveCommands(List<Command> actions){
        System.out.println("Testing saving commands---------------");
        GameBoard gameBoard = createSampleGameBoard();
        Persistency.saveCommands(actions, 1);
    }

    /**
     * Test loading commands
     * @autor zhoudavi1 300652444
     * @param actions The list of commands to be loaded.
     */
    private static void testLoadCommands(List<Command> actions){
        System.out.println("Testing loading commands---------------");
        MockRecorder recorder = new MockRecorder();
        GameBoard b = Persistency.loadRecording(recorder ,path + "1_commands.json");
        //Check commands same
        List<Command> loadedactions = recorder.getCommands();
        System.out.println("Number of commands should match: " + (actions.size() == loadedactions.size()));
        for (int i = 0; i < actions.size(); i++) {
            System.out.println("Direction should match: " + (actions.get(i).direction().equals(loadedactions.get(i).direction())));
        }
    }
}