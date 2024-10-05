package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.Action;
import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FuzzKeyChooser {

    List<List<Float>> board;
    App app;

    FuzzKeyChooser(App app){
        this.app = app;
        board = createZeroMatrix(app);
    }

    Action nextKey() {
        //Assign the value to each action respectively
        var actions = List.of(Action.Up, Action.Down, Action.Left, Action.Right)
                .stream()
                .collect(Collectors.toMap(
                        a -> a,                // Key extractor (the Action itself)
                        a -> 10/valueOfMove(a) // inversely proportional to value of space
                ));

        //Get a random action based on the probability
        double ran = Math.random() * actions.values().stream().mapToDouble(d -> d).sum();
        double cumulative = 0;
        for(var entry : actions.entrySet()){
            cumulative += entry.getValue();
            if(cumulative >= ran){
                increaseValue(entry.getKey(), .1f);
                return entry.getKey();
            }
        }

        throw new Error("Should have gotten a action if the code above is correct");
    }

    /**
     * Have to do it this long way to not import Location from Domain
     * @param action the direction to move
     * @return the value of the space we are considering moving to
     */
    float valueOfMove(Action action){
        return value(app.domain.getGameState().player().getLocation().x() + move(action).x(),
                app.domain.getGameState().player().getLocation().y() + move(action).y());
    }
    void increaseValue(Action action, float amount){
        int y = app.domain.getGameState().player().getLocation().y() + move(action).y();
        int x = app.domain.getGameState().player().getLocation().x() + move(action).x();
        board.get(y)
                .set(x, value(x,y) + amount);
    }
    float value(int x, int y){
        return board.get(y).get(x);
    }
    Location move(Action a){
        return Command.generate(a.toString()).direction().act(new Location(0,0));
    }

    /**
     * Create a list of list of floats to store how well tested each space on our board is.
     * @param app the application, which we can get the board from
     * @return the list<list<float>> everything set to 0.0f, or wall set to 10 since they should never be gone on
     */
    public static List<List<Float>> createZeroMatrix(App app) {
        var boardRows = app.domain.getBoard();
        int x = boardRows.size();
        int y = boardRows.get(0).size();

        return Stream.generate(() -> Stream.generate(() -> 0.0f)
                        .limit(y)
                        .collect(Collectors.toList()))
                .limit(x)
                .collect(Collectors.toList());
    }
}
