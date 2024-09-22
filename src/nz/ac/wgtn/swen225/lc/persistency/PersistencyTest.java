package nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.*;
import nz.ac.wgtn.swen225.lc.domain.GameActor.*;
import nz.ac.wgtn.swen225.lc.domain.GameItem.*;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;
import nz.ac.wgtn.swen225.lc.domain.Utilities.ItemColor;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistencyTest {
    public static void main(String[] args) throws IOException {
        GameState gameState = testReadJSON();
        testLoadGameState(gameState);
    }

    private static GameState createSampleGameState() {
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
        board.get(2).get(2).item = new Info("Hello, chap!");

        Player player = new Player(new Location(1, 1));
        List<Robot> robots = new ArrayList<>();
        robots.add(new KillerRobot(new Location(2, 3)));
        
        return new GameState(board, player, robots, 180, 1);
    }

    private static GameState testReadJSON() throws IOException{
        // Create a sample GameState
        System.out.println("Reading JSON:");

        GameState originalState = createSampleGameState();

        ObjectMapper mapper = new ObjectMapper();

        // Convert GameState to JSON
        String json = mapper.saveLeveltoFile(originalState);
        System.out.println("JSON Representation of GameState:\n" + json);

        // Convert JSON back to GameState
        GameState convertedState = mapper.convertJSONtoGameState(json);

        // Compare original and converted GameState
        System.out.println("Level number matches: " + (originalState.level() == convertedState.level()));
        System.out.println("Time left matches: " + (originalState.timeLeft() == convertedState.timeLeft()));
        System.out.println("Player location matches: " + originalState.player().getLocation().equals(convertedState.player().getLocation()));
        System.out.println("Number of robots matches: " + (originalState.robots().size() == convertedState.robots().size()));
        System.out.println("Robot location matches: " + originalState.robots().get(0).getLocation().equals(convertedState.robots().get(0).getLocation()));

        // Compare board
        boolean boardMatches = true;
        for (int y = 0; y < originalState.board().size(); y++) {
            for (int x = 0; x < originalState.board().get(y).size(); x++) {
                Item originalItem = originalState.board().get(y).get(x).item;
                Item convertedItem = convertedState.board().get(y).get(x).item;
                if (!originalItem.getClass().equals(convertedItem.getClass())) {
                    boardMatches = false;
                    break;
                }
            }
        }
        System.out.println("Board matches: " + boardMatches);

        //Save the converted GameState to a file
        Persistency.saveGameState("src/nz/ac/wgtn/swen225/lc/persistency/levels/level1.json", convertedState);
        return convertedState;
    }

    //Test loading a GameState from a file
    private static void testLoadGameState(GameState original) throws IOException{
        System.out.println("Loading GameState from file:--------------------------------------");
        GameState loadedState = Persistency.loadGameState("src/nz/ac/wgtn/swen225/lc/persistency/levels/level1.json");
        //Check if the loaded GameState is the same as the original GameState
        System.out.println("Level number matches: " + (original.level() == loadedState.level()));
        System.out.println("Time left matches: " + (original.timeLeft() == loadedState.timeLeft()));
        System.out.println("Player location matches: " + original.player().getLocation().equals(loadedState.player().getLocation()));
        System.out.println("Number of robots matches: " + (original.robots().size() == loadedState.robots().size()));
        System.out.println("Robot location matches: " + original.robots().get(0).getLocation().equals(loadedState.robots().get(0).getLocation()));

        // Compare board
        boolean boardMatches = true;
        for (int y = 0; y < original.board().size(); y++) {
            for (int x = 0; x < original.board().get(y).size(); x++) {
                Item originalItem = original.board().get(y).get(x).item;
                Item loadedItem = loadedState.board().get(y).get(x).item;
                if (!originalItem.getClass().equals(loadedItem.getClass())) {
                    boardMatches = false;
                    break;
                }
            }
        }
        System.out.println("Board matches: " + boardMatches);
    }

}