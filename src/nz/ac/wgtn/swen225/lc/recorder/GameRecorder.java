package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nz.ac.wgtn.swen225.lc.recorder.App;
import nz.ac.wgtn.swen225.lc.app.Command;

class GameRecorder implements Recorder{

    App app; Timer timer;
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
    public GameRecorder(App app){
        this.app = app;
        timer = new Timer(App.TICK_RATE,(unused) -> _redo());
    }


    @Override
    public void setCommands(List<Command> commands) {
        assert commands != null;
        assert !commands.isEmpty();

        this.commands = new ArrayList<>(commands);
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

        if(currentTick == -1) return;

        _pause();
        int current = currentTick;
        currentTick = -1;
        app.initialStateRevert();

        IntStream.range(0, current).forEach(i -> {app.giveInput(nextCommand());});

        app.updateGraphics();

        //Should have moved backwards 1 tick
        assert currentTick == current -1 : "expected " + (current -1) +", was " + currentTick;
    }

    /**
     * Plays the next move. (If currentTick == 0, commands.get(1) will play, as command 0 was already used)
     */
    protected void _redo(){
        assert currentTick < commands.size() : "Should never be bigger than commands";

        if(currentTick == commands.size()-1) return;//We have already redo'd as much as possible

        _pause();
        app.giveInput(nextCommand());
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
    }

    /**
     * Discards all commands after the current tick.
     * Then allows the player to move around again, recording further commands
     */
    protected void _takeControl(){
        //Delete all actions after this point.
        commands = commands.stream().limit(currentTick+1).collect(Collectors.toCollection(ArrayList::new));
        _pause();
    }
}
