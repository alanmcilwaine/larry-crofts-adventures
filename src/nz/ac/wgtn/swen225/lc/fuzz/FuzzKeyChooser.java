package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.Inputs.Action;
import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Inputs.Command;
import nz.ac.wgtn.swen225.lc.domain.Utilities.Location;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stores info so that it can generate the best inputs for testing
 *
 * @author John Rais 30065627
 * @version 2.0
 */
public class FuzzKeyChooser {

    final List<List<Float>> board;
    final App app;

    FuzzKeyChooser(App app){
        this.app = app;
        board = createZeroMatrix(app);
    }

    /**
     * Uses a smart algorithm to get a slightly random next move.
     * It will prioritize spaces that have not yet been moved to.
     * It will prioritize spaces that the player can move to, that are not blocked.
     *
     * @return An Action representing our desired move
     */
    Action nextKey() {
        //Assign the value to each action respectively
        var actions = Stream.of(Action.Up, Action.Down, Action.Left, Action.Right)
                .collect(Collectors.toMap(
                        a -> a,                // Key extractor (the Action itself)
                        a -> 10/valueOfMove(a)  // inversely proportional to value of space
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

        int y = playerLoc().y() + move(action).y();
        int x = playerLoc().x() + move(action).x();

        if(notValid(x,y)) return 1;

        //Spend less time attempting to move onto walls
        boolean canStepOn = x < 0 || y < 0 || app.domain.getBoard().get(y).get(x).canStepOn(app.domain.getGameState().player());

        return value(x,y) + (canStepOn ? 0 : 1);
    }

    /**
     * Increase the value of the space we are attempting to move to, to show that we have already attempted that
     * @param action The move we are attempting
     * @param amount the amount to increase its value
     */
    void increaseValue(Action action, float amount){

        int y = playerLoc().y() + move(action).y();
        int x = playerLoc().x() + move(action).x();

        if(notValid(x,y)) return;

        board.get(y).set(x, value(x,y) + amount);
    }
    float value(int x, int y){
        if(x < 0 || y < 0) return 0.5f;
        return board.get(y).get(x);
    }
    Location move(Action a){
        return Command.generate(a.toString()).direction().act(new Location(0,0));
    }

    /**
     * Get the players location
     */
    Location playerLoc(){
        return app.domain.getGameState().player().getLocation();
    }
    boolean notValid(int x, int y){
        return y >= board.size() || x >= board.get(0).size() || x < 0 || y < 0;
    }
    /**
     * Create a list of floats to store how well tested each space on our board is.
     * @param app the application, which we can get the board from
     * @return the grid of floats everything set to 0.0f, or wall set to 10 since they should never be gone on
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
