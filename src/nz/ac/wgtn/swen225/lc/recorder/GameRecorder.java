package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import nz.ac.wgtn.swen225.lc.app.AppInterface;
import nz.ac.wgtn.swen225.lc.app.Command;

class GameRecorder implements Recorder{
    private final AppInterface app;
    private Timer timer;
    protected static int tickTime = AppInterface.TICK_RATE;
    protected final ArrayDeque<Command> completed = new ArrayDeque<>();
    protected ArrayDeque<Command> undone = new ArrayDeque<>();

    /**
     * Create a timer that will call _redo every App.TICK_RATE
     * @param app save the app provided to a field
     */
    protected GameRecorder(AppInterface app){
        this.app = app;
        setPlaybackSpeed(tickTime);
    }

    /**
     * Resets the game state back to initial game state by calling app.initialStateRevert()
     * Then calls app.giveInput() until we are 1 move before the previous move.
     * Finally, redraws the graphics
     */
    protected void _undo(){

        if(completed.isEmpty()) return;

        IntStream.range(0, undoAll())//The amount of move to redo after going back to the start
                .forEach(i -> app.giveInput(nextCommand()));

        _pause();
    }

    /**
     * Plays the next move. Call redoFrame recursively until we find a ACTUAL move
     */
    protected void _redo(){
        if(!redoFrame()) _redo();
        _pause();
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

    //================= HELPER METHODS
    /**
     * Put all completed commands onto the undone stack, put the game state back to initial game state
     *
     * @return The last actual move
     */
    protected int undoAll(){
        int redoNum = lastActualMove(completed);
        completed.iterator().forEachRemaining(undone::push);
        completed.clear();
        app.initialStateRevert();
        return redoNum;
    }

    /**
     * Plays the next Command, compared to the playing the next move, which might be multiple Commands
     *
     * @return Is this an actual move? (Or was it Command.None)
     */
    protected boolean redoFrame(){
        if(undone.isEmpty()) return true;//We have already redo'd as much as possible, true means we are done

        app.giveInput(nextCommand());

        return completed.peek() != Command.None;//Return if this was a successful move
    }

    /**
     * Switch the top element of undone onto the top element of completed
     *
     * @return The next command of the undone dequeue
     */
    Command nextCommand(){
        Command next = undone.pop();
        completed.push(next);

        return next;
    }

    /**
     * Tells us how many moves we need to redo to be the equivalent of undoing one ACTUAL move
     * @return number of moves that should be redone to reach the last actual move we want to undo to
     */
    protected static int lastActualMove(ArrayDeque<Command> completed) {
        ArrayDeque<Command> copy = completed.clone();
        while(!copy.isEmpty()){
            if(copy.pop() != Command.None) break;
        }
        return copy.size();
    }

    //================== DEFAULT LOGIC
    @Override
    public List<Command> getCommands() {
        var copy = new ArrayList<>(completed);
        Collections.reverse(copy);
        return Stream.concat(copy.stream(),undone.stream())
                .toList();
    }

    @Override
    public void setCommands(List<Command> commands) {
        assert commands != null;
        assert !commands.isEmpty();

        completed.clear();
        //First element in the list will be the top of the stack
        undone = new ArrayDeque<>(commands);
    }

    @Override
    public void setPlaybackSpeed(int tickTime) {
        GameRecorder.tickTime = tickTime;
        timer = new PlaybackTimer(this::redoFrame);
    }

    @Override
    public void tick(Command commandToSave) {
        completed.push(commandToSave);
    }
    //================== DELEGATE ACTIONS
    @Override
    public Action undo() {return RecorderAction.of(this::_undo);}
    @Override
    public Action redo() { return RecorderAction.of(this::_redo);}
    @Override
    public Action play() {return RecorderAction.of(timer::start);}
    @Override
    public Action pause() {return RecorderAction.of(this::_pause);}
    @Override
    public Action takeControl() {return RecorderAction.of(this::_takeControl);}

}
