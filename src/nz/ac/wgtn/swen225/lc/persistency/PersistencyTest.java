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
        // Create a sample GameState
        GameState sampleGameState = createSampleGameState();

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert GameState to JSON string
            String jsonGameState = objectMapper.saveLeveltoFile(sampleGameState);
            System.out.println("GameState JSON:");
            System.out.println(jsonGameState);

        } catch (IOException e) {
            System.err.println("Error occurred while saving to JSON: " + e.getMessage());
            e.printStackTrace();
        }

        // Test reading JSON to GameState
        testReadJSON();
    }

    private static GameState createSampleGameState() {
        // Create a simple 3x3 board
        List<List<Tile<Item>>> board = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            List<Tile<Item>> row = new ArrayList<>();
            for (int x = 0; x < 3; x++) {
                row.add(new Tile<>(new NoItem(), new Location(x, y)));
            }
            board.add(row);
        }

        // Add some items to the board
        board.get(0).get(1).item = new LockedDoor(ItemColor.RED);
        board.get(1).get(0).item = new Wall();
        board.get(1).get(1).item = new Info("Hi");

        // Create player
        Player player = new Player(new Location(0, 0));

        // Create a robot
        List<Robot> robots = new ArrayList<>();
        robots.add(new KillerRobot(new Location(2, 2)));
        robots.add(new KillerRobot(new Location(1, 2)));

        // Create GameState
        return new GameState(board, player, robots, 60, 1);
    }

    private static void testReadJSON() throws IOException{
        // Create a sample GameState
        System.out.println("Reading JSON:");
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
        board.get(2).get(2).item = new Info("Hello");

        Player player = new Player(new Location(1, 1));
        List<Robot> robots = new ArrayList<>();
        robots.add(new KillerRobot(new Location(3, 3)));
        
        GameState originalState = new GameState(board, player, robots, 60, 1);

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

        //Info
        System.out.println("Info matches: " + originalState.board().get(2).get(2).item.equals(convertedState.board().get(2).get(2).item));
    }

}