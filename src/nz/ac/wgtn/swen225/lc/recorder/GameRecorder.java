package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nz.ac.wgtn.swen225.lc.app.App;
import nz.ac.wgtn.swen225.lc.app.Command;

class GameRecorder implements Recorder{

    App app; Timer timer;
    /**
     * A list of all the actions the player did and were recorded every frame
     */
    List<Command> commands = new ArrayList<>();
    /**
     * When replaying shows where we are currently looking
     */
    int currentTick = 0;

    public GameRecorder(App app){
        this.app = app;
        timer = new Timer(App.TICK_RATE,(unused) -> update());
    }

    private void update(){
        _redo();
    }
    @Override
    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void tick(Command commandToSave) {
        commands.add(commandToSave);
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


    Command nextCommand(){return commands.get(currentTick++);}

    private void _undo(){
        _pause();
        int current = currentTick;
        currentTick = 0;
        app.initialStateRevert();
        IntStream.range(0, current-1).forEach(i -> app.giveInput(nextCommand()));

        //Should have moved backwards 1 tick
        assert currentTick == current -1;
    }
    private void _redo(){
        _pause();
        app.giveInput(nextCommand());
    }
    private void _play(){
        timer.start();
    }
    private void _pause(){
        timer.stop();
    }
    private void _takeControl(){
        //Delete all actions after this point.
        commands = commands.stream().limit(currentTick+1).collect(Collectors.toList());
        _pause();
    }
}
