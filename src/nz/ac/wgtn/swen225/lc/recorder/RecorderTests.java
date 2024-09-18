package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.Command;
import nz.ac.wgtn.swen225.lc.recorder.App;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RecorderTests {

    public static void main(String[] args) {
        testRecorder();
    }

    static void testRecorder(){
        //Make sure assertions are on
        try{
            assert false;
            throw new Error("Assertions disabled");
        }catch(AssertionError ignored){}

        testManual();
        fuzzActions();
        IntStream.range(0,1000).forEach( i -> cornerCaseRedoZeroCommands());
    }
    static void cornerCaseRedoZeroCommands(){

        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        int com = 50;
        recorder.setCommands(randomCommands(com));


        assert recorder.currentTick == 0 : "Expected 0, was " + recorder.currentTick;

        recorder.takeControl();

        assert recorder.commands.size() == 1 : "Instead of 1, had : " + recorder.commands.size();

        recorder.undo();
        recorder.redo();

        //Now test for recording new stuff!
        recorder.tick(randomCommands(1).get(0));
        recorder.tick(randomCommands(1).get(0));
        recorder.tick(randomCommands(1).get(0));

        String[][] save = deepclone(app.state);
        assert recorder.currentTick == 3;
        assert recorder.commands.size() == 4;

        recorder.undo();
        recorder.undo();
        recorder.undo();

        recorder.redo();
        recorder.redo();
        recorder.redo();

        assertArrays(app.state,save);
        assert recorder.currentTick == 5;
        assert recorder.commands.size() == 4;
    }
    static void fuzzActions(){
        for(int trys = 0; trys < 1000; trys++) {
            MockApp app = mockApp();
            GameRecorder recorder = mockRecorder(app);
            int com = 50;
            recorder.setCommands(randomCommands(com));

            IntStream.range(0, (int)(Math.random()*60)).forEach(i -> recorder.redo());
            recorder.redo();//Make sure it is not a place 0

            String[][] save = deepclone(app.state);
            recorder.undo();
            recorder.redo();
            assertArrays(app.state,save);

            recorder.undo();
            save = deepclone(app.state);
            recorder.redo();
            recorder.undo();
            assertArrays(app.state,save);
        }
    }
    static void testManual(){
        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        recorder.setCommands(List.of(Command.Up,Command.Down,Command.Up,Command.Left,Command.None,Command.Up));

        recorder.update();
        assert recorder.currentTick == 6 : "Tick was " + recorder.currentTick;
        assertArrays(app.state,new String[][]{
                {"P", "_", "_"},
                {"_", "_", "_"},
                {"_", "_", "_"}});

        recorder.undo();

        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"P", "_", "_"},
                {"_", "_", "_"}});

        recorder.undo();
        recorder.redo();

        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"P", "_", "_"},
                {"_", "_", "_"}});

        recorder.undo();
        recorder.redo();
        assert recorder.currentTick == 5 : "Tick was " + recorder.currentTick;
        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"P", "_", "_"},
                {"_", "_", "_"}});
    }




    //HELPER FUNCTIONS AND CLASSES

    /**
     * Returns a list of random commands
     * @param num the ammount of commands in the list
     * @return A list of commands, Up, Down, Left, Right, None
     */
    static List<Command> randomCommands(int num){
        return IntStream.range(0,num)
                .mapToObj(i -> Command.values()[(int)Math.round(Math.random()*4)])
                .toList();
    }
    static MockApp mockApp(){
        return new MockApp(1, 2, new String[][]{{"_","_","_"},
                                          {"_","_","_"},
                                          {"_","P","_"}});
    }
    static class MockApp implements App{
        MockApp(int x, int y, String[][] in){
            init = in;
            state = deepclone(in);
            graphics = deepclone(in);
            xp = x; yp = y;
            xpo = x; ypo = y;
        }
        String[][] state;
        String[][] init;
        int xpo, ypo, xp, yp;
        String[][] graphics;
        String log = "";
        @Override
        public void updateGraphics() {
            graphics = state;
            log += "updatedGraphics.\n";
        }
        @Override
        public void giveInput(Command input) {
            switch(input){
                case Up -> movePlayer(0,-1);
                case Down -> movePlayer(0,1);
                case Left -> movePlayer(-1,0);
                case Right -> movePlayer(1,0);
            }
        }
        @Override
        public void initialStateRevert() {
            xp = xpo; yp = ypo;
            state = deepclone(init);
        }
        public void movePlayer(int x, int y){
            if(yp+y < 0 || yp+y >= state.length || xp+x < 0 || xp+x >= state[0].length) return;

            state[yp][xp] = "_";
            xp+=x; yp+=y;
            state[yp][xp] = "P";
        }
    }

    static GameRecorder mockRecorder(App app){
        return new GameRecorder(app){
            protected void update(){
                if(currentTick < commands.size()) {
                    _redo(); update();
                }
            }
            public Action undo(){_undo(); return null;}
            public Action redo(){_redo(); return null;}
            public Action takeControl(){_takeControl(); return null;}
        };
    }
    /**
     * Prints a 2D string array nicely
     */
    static String toPrint(String[][] array){
        return Arrays.stream(array)
                .map(a -> String.join(",", a))
                .collect(Collectors.joining("\n"));
    }

    static void assertArrays(String[][] a,String[][] shouldBe){
        assert Arrays.deepEquals(a,shouldBe)
                : "Was: \n" + toPrint(a) +"\nInstead of: \n" + toPrint(shouldBe);
    }
    static String[][] deepclone(String[][] array){
        String[][] nw = new String[array.length][array[0].length];

        for(int y = 0; y < array.length; y++){
            for(int x = 0; x < array[0].length; x++){
                nw[y][x] = array[y][x];
            }
        }
        return nw;
    }
}
