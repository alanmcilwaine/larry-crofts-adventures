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

        simpleTest();
        testManual();
        IntStream.range(0,1000).forEach( i -> cornerCaseRedoZeroCommands());
        fuzzActions();
    }
    static void simpleTest(){
        System.out.println("_______________SIMPLE__________");
        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        int com = 1;
        recorder.setCommands(List.of(Command.Up));

        String[][] s1 = deepclone(app.state);

        recorder.redo();
        recorder.undo();

        assertArrays(app.state, s1);
    }
    static void cornerCaseRedoZeroCommands(){
        System.out.println("_______________ZERO COMMAND__________");
        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        int com = 50;
        recorder.setCommands(randomCommands(com));
        System.out.println(recorder.commands+"\n");
        //-1 since has not moved yet
        assert recorder.currentTick == -1 : "Expected -1, was " + recorder.currentTick;

        recorder.takeControl();

        assert recorder.commands.size() == 0 : "Instead of 0, had : " + recorder.commands.size();

        //Should do nothing and not throw errors
        recorder.undo();
        recorder.redo();

        //==============

        //Now test for recording new stuff!

        String[][] s1 = deepclone(app.state);
        System.out.println("S1:\n" + toPrint(app.state));

        recorder.tick(randomCommands(1).get(0));
        String[][] s2 = deepclone(app.state);
        assert recorder.currentTick == 0;
        System.out.println("S2:" + recorder.commands.get(recorder.currentTick) + "\n" + toPrint(app.state));

        recorder.tick(randomCommands(1).get(0));
        String[][] s3 = deepclone(app.state);
        assert recorder.currentTick == 1;
        System.out.println("S3:" + recorder.commands.get(recorder.currentTick) + "\n" + toPrint(app.state));

        recorder.tick(randomCommands(1).get(0));
        String[][] s4 = deepclone(app.state);
        assert recorder.currentTick == 2;
        System.out.println("S4:" + recorder.commands.get(recorder.currentTick) + "\n" + toPrint(app.state));

        assert recorder.commands.size() == 3;

        //SEE IF IT BEHAVES AS EXPECTED WHILE UNDOING AND REDOING
        recorder.undo();
        assert recorder.currentTick == 1; assertArrays(app.state,s3);

        recorder.undo();
        assert recorder.currentTick == 0; assertArrays(app.state,s2);

        recorder.undo();
        assert recorder.currentTick == -1; assertArrays(app.state,s1);
        //======
        recorder.redo();
        assert recorder.currentTick == 0; assertArrays(app.state,s2);

        recorder.redo();
        assert recorder.currentTick == 1; assertArrays(app.state,s3);

        recorder.redo();
        assert recorder.currentTick == 2; assertArrays(app.state,s4);

    }
    static void fuzzActions(){
        System.out.println("_______________FUZZ__________");
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
        assert recorder.currentTick == 5 : "Tick was " + recorder.currentTick;
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
        assert recorder.currentTick == 4 : "Tick was " + recorder.currentTick;
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
            System.out.println("\n===REVERT\n");
        }
        public void movePlayer(int x, int y){
            System.out.println("\nx " + x + ", y " + y + "\n" + toPrint(state) + "\n");
            if(yp+y < 0 || yp+y >= state.length || xp+x < 0 || xp+x >= state[0].length){
                System.out.println("OUT OF BOUNDS\n");
                return;
            }
            state[yp][xp] = "_";
            xp+=x; yp+=y;
            state[yp][xp] = "P";
        }
    }

    static GameRecorder mockRecorder(App app){
        return new GameRecorder(app){
            protected void update(){
                if(currentTick < commands.size()-1) {
                    _redo(); update();
                }
            }
            public Action undo(){System.out.println("UNDO");_undo(); return null;}
            public Action redo(){System.out.println("REDO");_redo(); return null;}
            public void tick(Command commandToSave){super.tick(commandToSave); app.giveInput(commandToSave);}
            public Action takeControl(){System.out.println("TAKE CONTROL");_takeControl(); return null;}
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
