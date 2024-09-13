package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import nz.ac.wgtn.swen225.lc.app.Command;

public class GameRecorder implements Recorder{

    Playback playback;
    List<Command> commands = new ArrayList<>();

    GameRecorder(){playback = new Playback();}

    @Override
    public void saveRecording() {

    }

    @Override
    public void loadRecording(String filename) throws FileNotFoundException {

    }

    @Override
    public void tick(Command commandToSave) {

    }

    @Override
    public Action undo() {
        return (RecorderAction)(e) -> playback.undo();
    }

    @Override
    public Action redo() {
        return (RecorderAction)(e) -> playback.redo();
    }

    @Override
    public Action play() {
        return (RecorderAction)(e) -> playback.play();
    }

    @Override
    public Action pause() {
        return (RecorderAction)(e) -> playback.pause();
    }



    public class Playback{

        void undo(){}

        void redo(){}

        void pause(){}

        void play(){}
    }

}
