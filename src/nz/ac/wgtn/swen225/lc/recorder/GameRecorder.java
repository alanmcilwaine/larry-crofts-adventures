package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import nz.ac.wgtn.swen225.lc.app.AppInterface;
import nz.ac.wgtn.swen225.lc.app.Command;

class GameRecorder implements Recorder{

    AppInterface app; Timer timer;

    protected static int tickTime = AppInterface.TICK_RATE;

    protected final ArrayDeque<Frame> completed = new ArrayDeque<>();
    protected ArrayDeque<Frame> undone = new ArrayDeque<>();
    /**
     * Create a timer that will call _redo every App.TICK_RATE
     * @param app save the app provided to a field
     */
    public GameRecorder(AppInterface app){
        this.app = app;
        setPlaybackSpeed(tickTime);
    }

    /**
     * First element in the list will be the top of the undo stack
     */
    @Override
    public void setCommands(List<Command> commands) {
        assert commands != null;
        assert !commands.isEmpty();

        completed.clear();
        //First element in the list will be the top of the stack
        undone = commands.stream().map(Frame::of).collect(Collectors.toCollection(ArrayDeque::new));
    }

    @Override
    public void setPlaybackSpeed(int tickTime) {
        assert tickTime > 0 : "Illegal value for tick time: " + tickTime;

        GameRecorder.tickTime = tickTime;
        timer = new PlaybackTimer(this::redoFrame);
    }

    @Override
    public List<Command> getCommands() {
        var copy = new ArrayList<>(completed);
        Collections.reverse(copy);
        return Stream.concat(copy.stream(),undone.stream())
                .map(Frame::command)
                .toList();
    }

    @Override
    public void tick(Command commandToSave) {
        assert commandToSave != null;

        completed.push(Frame.of(commandToSave));
    }



    /**
     * Switch the top element of undone onto the top element of completed
     *
     * @return The next command of the undone dequeue
     */
    Command nextCommand(){
        Frame next = undone.pop();
        completed.push(next);

        return next.command();
    }

    /**
     * Resets the game state back to initial game state by calling app.initialStateRevert()
     * Then calls app.giveInput() until we are 1 move before the previous move.
     * Finally, redraws the graphics
     */
    protected void _undo(){
        if(completed.isEmpty()) return;

        undoAll(); //Puts us back to initial state of the game

        IntStream.range(0, lastActualMove(completed))
                .forEach(i -> app.giveInput(nextCommand()));
    }

    /**
     * Put all completed commands onto the undone stack, put the game state back to initial game state
     */
    protected void undoAll(){
        completed.iterator().forEachRemaining(undone::push);
        completed.clear();
        app.initialStateRevert();
    }
    /**
     * Plays the next move. (If currentTick == 0, commands.get(1) will play, as command 0 was already used)
     */
    protected void _redo(){

        redoFrame();

        //call it recursively until we find a move
        if(completed.peek().command() == Command.None) _redo();
    }
    /**
     * Plays the next frame, (compared to the next move which might be multiple frames)
     */
    protected void redoFrame(){

        if(undone.isEmpty()) return;//We have already redo'd as much as possible

        Command next = nextCommand();

        app.giveInput(next);
    }

    /**
     * For redoing finds the index of the last ACTUAL move, that way you don't have to spam the undo button
     * @return amount of moves to redo
     */
    protected static int lastActualMove(ArrayDeque<Frame> completed) {
        ArrayDeque<Frame> copy = completed.clone();
        while(!copy.isEmpty()){
            if(copy.pop().command() != Command.None) break;
        }
        return copy.size();
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
        undone.clear();
        timer.stop();
        app.pauseTimer(false);
    }


    //SIMPLE OVERRIDES TO DELEGATE
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
}
