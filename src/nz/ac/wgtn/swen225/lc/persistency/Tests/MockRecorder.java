package nz.ac.wgtn.swen225.lc.persistency.Tests;

import nz.ac.wgtn.swen225.lc.app.Inputs.Command;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;

import javax.swing.*;
import java.util.List;

public class MockRecorder implements Recorder {
    List<Command> commands;
    @Override
    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void setPlaybackSpeed(int tickTime) {

    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void tick(Command commandToSave) {

    }

    @Override
    public Action undo() {
        return null;
    }

    @Override
    public Action redo() {
        return null;
    }

    @Override
    public void redoAll() {/*DO NOTHING*/ }

    @Override
    public Action play() {
        return null;
    }

    @Override
    public void pause() {}

    @Override
    public void takeControl() {}

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public boolean canRedo() {
        return false;
    }
}
