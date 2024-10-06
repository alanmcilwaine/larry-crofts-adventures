package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.Command;

class Frame {



    private Command command;
    private Frame(Command command){
        this.command = command;
    }

    static Frame of(Command command){
        assert command != null;
        return new Frame(command);
    }


    public Command command() {
        return command;
    }
}
