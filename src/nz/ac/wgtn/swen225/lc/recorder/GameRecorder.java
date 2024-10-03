package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nz.ac.wgtn.swen225.lc.app.AppInterface;
import nz.ac.wgtn.swen225.lc.app.Command;

class GameRecorder implements Recorder{

    AppInterface app; Timer timer;
    /**
     * A list of all the actions the player did and were recorded every frame
     */
    List<Command> commands = new ArrayList<>();
    /**
     * When replaying shows where we are currently looking
     * -1 -> means no commands have been executed. 0 -> commands.get(0) has been executed
     */
    int currentTick = -1;

    /**
     * Create a timer that will call _redo every App.TICK_RATE
     * @param app save the app provided to a field
     */
    public GameRecorder(AppInterface app){
        this.app = app;
        timer = new PlaybackTimer(this::redoFrame);
    }


    @Override
    public void setCommands(List<Command> commands) {
        assert commands != null;
        assert !commands.isEmpty();

        this.commands = new ArrayList<>(commands);
    }

    @Override
    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public void tick(Command commandToSave) {
        commands.add(commandToSave);
        currentTick = commands.size()-1;
    }

    @Override
    public Action undo() {return (RecorderAction)(e) -> _undo();}
    @Override
    public Action redo() { return (RecorderAction) (e) -> _redo();}
    @Override
    public Action play() {return (RecorderAction) (e) -> _play();}
    @Override
    public Action pause() {return (RecorderAction) (e) -> _pause();}
    @Override
    public Action takeControl() {return (RecorderAction) (e) -> _takeControl();}


    /**
     * Increments currents ticks then returns that command
     * (If currentTick == 0, commands.get(1) will play, as command 0 was already used)
     * @return The next command. Throws assertion error if it is out of bounds.
     */
    Command nextCommand(){
        assert currentTick < commands.size() : "Tried to get a command that is bigger than the commands size: " + currentTick;
        //Increment the current tick, then get the command
        return commands.get(++currentTick);
    }

    /**
     * Resets the game state back to initial game state by calling app.initialStateRevert()
     * Then calls app.giveInput() until we are 1 move before the previous move.
     * Finally redraws the graphics
     */
    protected void _undo(){
        assert currentTick >= -1 : "Should never be less than zero";

        int current = currentTick;
        int lastMove = lastActualMove(current);

        _pause();
        currentTick = -1;
        app.initialStateRevert();

        //If no valid moves were made since the start then we do not have to redo anything.
        if(lastMove != -1)
            IntStream.range(0, lastMove+1).forEach(i -> {app.giveInput(nextCommand());});

        app.updateGraphics();

        //Should have moved backwards at least 1 tick
        assert currentTick <= lastMove : "expected <= " + (lastMove) + ", was " + currentTick;
    }

    /**
     * For redoing finds the index of the last ACTUAL move, that way you don't have to spam the undo button
     * @return index of last move +1, so you can use it as range
     */
    private int lastActualMove(int current) {
        return IntStream.range(0, current)
                .boxed()
                .sorted(Collections.reverseOrder())
                .filter(i -> commands.get(i) != Command.None)
                .findFirst()
                .orElse(-1);
    }
    /**
     * Plays the next move. (If currentTick == 0, commands.get(1) will play, as command 0 was already used)
     */
    protected void _redo(){

        assert currentTick < commands.size() : "Should never be bigger than commands";

        if(currentTick == commands.size()-1) return;//We have already redo'd as much as possible

        Command next = nextCommand();

        _pause();
        app.giveInput(next);

        //call it recursively until we find a move
        if(next == Command.None) _redo();
        else  app.updateGraphics();
    }
    /**
     * Plays the next frame, (compared to the next move)
     */
    protected void redoFrame(){

        assert currentTick < commands.size() : "Should never be bigger than commands";

        if(currentTick == commands.size()-1) return;//We have already redo'd as much as possible

        Command next = nextCommand();

        app.giveInput(next);

        app.updateGraphics();
    }

    /**
     * Start the timer that every tick calls _redo()
     */
    private void _play(){
        timer.start();
    }

    /**
     * Stop the timer
     */
    private void _pause(){
        timer.stop();
        app.pauseTimer(true);
    }

    /**
     * Discards all commands after the current tick.
     * Then allows the player to move around again, recording further commands
     */
    protected void _takeControl(){
        //Delete all actions after this point.
        commands = commands.stream().limit(currentTick+1).collect(Collectors.toCollection(ArrayList::new));
        timer.stop();
        app.pauseTimer(false);
    }
}
