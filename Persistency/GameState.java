
public class GameState {
    private String name;
    private int timeLimit;

    public GameState(String name, int timeLimit) {
        this.name = name;
        this.timeLimit = timeLimit;
    }

    public String getName() {
        return name;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
}
