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

    public GameRecorder(App app){
        this.app = app;
        timer = new Timer(App.TICK_RATE,(unused) -> update());
    }

    protected void update(){
        _redo();
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


    Command nextCommand(){
        assert currentTick < commands.size() : "Tried to get a command that is bigger than the commands size: " + currentTick;
        //Increment the current tick, then get the command
        return commands.get(++currentTick);
    }

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
    protected void _redo(){
        assert currentTick < commands.size() : "Should never be bigger than commands";

        if(currentTick == commands.size()-1) return;//We have already redo'd as much as possible

        _pause();
        app.giveInput(nextCommand());
        app.updateGraphics();
    }
    private void _play(){
        timer.start();
    }
    private void _pause(){
        timer.stop();
    }
    protected void _takeControl(){
        //Delete all actions after this point.
        commands = commands.stream().limit(currentTick+1).collect(Collectors.toCollection(ArrayList::new));
        _pause();
    }
}
