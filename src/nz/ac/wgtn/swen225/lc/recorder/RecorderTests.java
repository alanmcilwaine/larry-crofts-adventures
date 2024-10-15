
package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.Inputs.Command;
import nz.ac.wgtn.swen225.lc.app.AppInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Holds manual tests that make sure individual methods are working as well as random tests,
 * that randomize operations and make sure nothing breaks.
 *
 * @author John Rais 300654627
 * @version 2.5
 */
public class RecorderTests {

    //Use to generate random actions
    final static Random random = new Random();

    @Test void undoTest(){
        var lis = List.of(Command.Up,Command.Left,Command.None,Command.Right);

        GameRecorder rq = new GameRecorder(mockApp());
        rq.setCommands(lis);
        //First command in the list is the first one we want to action
        Assertions.assertEquals( rq.undone.peek(), Command.Up);

        rq.redoFrame();
        rq.redoFrame();
        rq.redoFrame();
        rq.redoFrame();
        rq.redoFrame();

        assert rq.undone.isEmpty();

        Assertions.assertEquals(rq.completed.peek(), Command.Right);

        Assertions.assertEquals( rq.getCommands(),lis);

        //Now we can start undoing
        rq._undo();

        Assertions.assertEquals(rq.undone.peek(), Command.Right);
        Assertions.assertEquals( rq.getCommands(),lis);

        rq._undo();
        Assertions.assertEquals(rq.undone.peek(), Command.Left);

        Assertions.assertEquals( rq.getCommands(),lis);
    }
    /**
     * First element in the list should be the top of the undo stack
     */
    @Test void redoTest(){
        var lis = List.of(Command.Up,Command.Left,Command.None,Command.Right);

        GameRecorder rq = new GameRecorder(mockApp());
        rq.setCommands(lis);
        //First command in the list is the first one we want to action
        Assertions.assertEquals( rq.undone.peek(), Command.Up);

        rq.redoFrame();
        assert rq.undone.peek() == Command.Left;

        assert rq.completed.peek() == Command.Up;

        assert rq.getCommands().equals(lis);
    }
    @Test void redoMultipleTest(){
        var lis = List.of(Command.None,Command.Left,Command.Down,Command.Right);

        GameRecorder rq = new GameRecorder(mockApp());
        rq.setCommands(lis);
        //First command in the list is the first one we want to action
        Assertions.assertEquals( rq.undone.peek(), Command.None);

        rq._redo();
        Assertions.assertEquals( rq.undone.peek(), Command.Down);

        Assertions.assertEquals( rq.completed.peek(), Command.Left);

        Assertions.assertEquals( rq.getCommands(),lis);
    }
    @Test void undoAllTest(){
        var lis = List.of(Command.Up,Command.Left,Command.None,Command.Right);

        GameRecorder rq = new GameRecorder(mockApp());
        rq.setCommands(lis);

        Assertions.assertEquals(rq.undone.peek(), Command.Up);

        rq.redoFrame();
        rq.redoFrame();
        rq.redoFrame();
        rq.redoFrame();

        Assertions.assertEquals(rq.completed.size(), lis.size());
        Assertions.assertTrue(rq.undone.isEmpty());

        rq.undoAll();

        Assertions.assertEquals(rq.getCommands().size(),lis.size());
        Assertions.assertTrue(rq.completed.isEmpty());
        Assertions.assertEquals(rq.undone.size(),  lis.size());
        Assertions.assertEquals(rq.undone.peek(), Command.Up);
    }
    @Test void getCommandsTest(){
        var lis = List.of(Command.Up,Command.Left,Command.None,Command.Right, Command.Down);

        GameRecorder rq = new GameRecorder(mockApp());

        rq.setCommands(lis);

        rq.completed.push(rq.undone.pop());

        Assertions.assertEquals( lis,rq.getCommands());

        rq.completed.push(rq.undone.pop());

        Assertions.assertEquals( lis,rq.getCommands());
    }

    @Test void lastActualMoveTest(){
        var deque = new ArrayDeque<Command>(); deque.push((Command.Up));deque.push((Command.Left));deque.push((Command.None));
        assert GameRecorder.lastActualMove(deque) == 1 : GameRecorder.lastActualMove(deque);

        deque = new ArrayDeque<>(); deque.push((Command.Up));deque.push((Command.Left));deque.push((Command.Down));
        assert GameRecorder.lastActualMove(deque) == 2 : GameRecorder.lastActualMove(deque);

        deque = new ArrayDeque<>(); deque.push((Command.Up));deque.push((Command.None));deque.push((Command.None));
        assert GameRecorder.lastActualMove(deque) == 0 : GameRecorder.lastActualMove(deque);

        deque = new ArrayDeque<>(); deque.push((Command.None));deque.push((Command.None));deque.push((Command.None));
        assert GameRecorder.lastActualMove(deque) == 0 : GameRecorder.lastActualMove(deque);
    }
   @Test
   void testRecorder(){
        IntStream.range(0,1000).forEach( i -> cornerCaseRedoZeroCommands());
    }
    @Test void simpleTest(){
        System.out.println("_______________SIMPLE__________");
        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        recorder.setCommands(List.of(Command.Up));

        String[][] s1 = deepclone(app.state);

        recorder.redo();
        recorder.undo();

        assertArrays(app.state, s1);
    }
    void cornerCaseRedoZeroCommands(){
        System.out.println("_______________ZERO COMMAND__________");
        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        int com = 50;
        recorder.setCommands(randomCommands(com));
        System.out.println(recorder.getCommands()+"\n");
        //-1 since has not moved yet
        assert (recorder.completed.size()-1) == -1 : "Expected -1, was " + (recorder.completed.size()-1);

        recorder.takeControl();

        assert recorder.getCommands().size() == 0 : "Instead of 0, had : " + recorder.getCommands().size();

        //Should do nothing and not throw errors
        recorder.undo();
        recorder.redo();

        //==============

        //Now test for recording new stuff!

        String[][] s1 = deepclone(app.state);
        System.out.println("S1:\n" + toPrint(app.state));

        recorder.tick(randomCommands(1).get(0));
        String[][] s2 = deepclone(app.state);
        assert (recorder.completed.size()-1) == 0;

        recorder.tick(randomCommands(1).get(0));
        String[][] s3 = deepclone(app.state);
        assert (recorder.completed.size()-1) == 1;

        recorder.tick(randomCommands(1).get(0));
        String[][] s4 = deepclone(app.state);
        assert (recorder.completed.size()-1) == 2;

        assert recorder.getCommands().size() == 3;

        //SEE IF IT BEHAVES AS EXPECTED WHILE UNDOING AND REDOING
        recorder.undo();
        assert (recorder.completed.size()-1) == 1; assertArrays(app.state,s3);

        recorder.undo();
        assert (recorder.completed.size()-1) == 0; assertArrays(app.state,s2);

        recorder.undo();
        assert (recorder.completed.size()-1) == -1; assertArrays(app.state,s1);
        //======
        recorder.redo();
        assert (recorder.completed.size()-1) == 0; assertArrays(app.state,s2);

        recorder.redo();
        assert (recorder.completed.size()-1) == 1; assertArrays(app.state,s3);

        recorder.redo();
        assert (recorder.completed.size()-1) == 2; assertArrays(app.state,s4);

    }
    @Test void fuzzActions(){
        System.out.println("_______________FUZZ__________");
        for(int trys = 0; trys < 1000; trys++) {
            MockApp app = mockApp();
            GameRecorder recorder = mockRecorder(app);
            int com = 50;
            recorder.setCommands(randomCommands(com));

            IntStream.range(0, random.nextInt(0,60)).forEach(i -> recorder.redo());
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
    @Test void testManual(){
        MockApp app = mockApp();
        GameRecorder recorder = mockRecorder(app);
        recorder.setCommands(List.of(Command.Left,Command.Right,Command.Up,Command.Down,Command.Up,Command.Down));
        //Go to the last command
        IntStream.range(0,6).forEach(i ->recorder.redo());

        assert (recorder.completed.size()-1) == 5 : "Tick was " + (recorder.completed.size()-1);
        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"_", "_", "_"},
                {"_", "P", "_"}});

        recorder.undo();
        assert (recorder.completed.size()-1) == 4 : "Tick was " + (recorder.completed.size()-1);
        assert (recorder.undone.size()) == 1 : "Undone was " + (recorder.undone.size());

        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"_", "P", "_"},
                {"_", "_", "_"}});

        recorder.undo();
        recorder.redo();

        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"_", "P", "_"},
                {"_", "_", "_"}});

        recorder.undo();
        recorder.redo();
        assert (recorder.completed.size()) == 5 : "Tick was " + (recorder.completed.size());
        assertArrays(app.state,new String[][]{
                {"_", "_", "_"},
                {"_", "P", "_"},
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
                .mapToObj(i -> Command.values()[(int)Math.round(Math.random()*3)])
                .toList();
    }
    static MockApp mockApp(){
        return new MockApp(new String[][]{{"_","_","_"},
                                          {"_","_","_"},
                                          {"_","P","_"}});
    }

    /**
     * A totally fake App, that gives us all the functionality we need for testing.
     */
    static class MockApp implements AppInterface{
        MockApp(String[][] in){
            init = in;
            state = deepclone(in);
            xp = 1; yp = 2;
            xpo = 1; ypo = 2;
        }
        String[][] state;
        final String[][] init;
        final int xpo, ypo;
        int xp, yp;
        @Override
        public void updateGraphics() {
        }


        @Override
        public void giveInput(Command input) {
            switch(input){
                case Up -> movePlayer(0,-1);
                case Down -> movePlayer(0,1);
                case Left -> movePlayer(-1,0);
                default -> movePlayer(1,0);//Right
            }
        }
        @Override
        public void initialStateRevert() {
            xp = xpo; yp = ypo;
            state = deepclone(init);
            System.out.println("\n===REVERT\n");
        }

        @Override
        public void pauseTimer(boolean state) {

        }

        @Override
        public String openFile() {
            return null;
        }

        @Override
        public String saveFile() {
            return null;
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

    /**
     * Returns a modified Recorder that allows us to easier test.
     */
    static GameRecorder mockRecorder(AppInterface app1){

        return new GameRecorder(app1){
            public Action undo(){System.out.println("UNDO");_undo(); return null;}
            public Action redo(){System.out.println("REDO");_redo(); return null;}
            public void tick(Command commandToSave){super.tick(commandToSave); app1.giveInput(commandToSave);}
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

    /**
     * Verify that two arrays are identical
     */
    static void assertArrays(String[][] a,String[][] shouldBe){
        assert Arrays.deepEquals(a,shouldBe)
                : "Was: \n" + toPrint(a) +"\nInstead of: \n" + toPrint(shouldBe);
    }

    /**
     * Handy function to completely clone an array of Strings
     */
    static String[][] deepclone(String[][] array){
        String[][] nw = new String[array.length][array[0].length];

        for(int y = 0; y < array.length; y++){
            System.arraycopy(array[y], 0, nw[y], 0, array[0].length);
        }
        return nw;
    }
}
