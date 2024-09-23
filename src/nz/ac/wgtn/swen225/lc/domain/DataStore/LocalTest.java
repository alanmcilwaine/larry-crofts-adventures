package nz.ac.wgtn.swen225.lc.domain.DataStore;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.wgtn.swen225.lc.domain.DomainTest.Mock;
import nz.ac.wgtn.swen225.lc.domain.GameBoard;

import java.io.File;
import java.io.IOException;

public class LocalTest {
    public static void mapBoardToJson() {
        var g = Mock.getGameBoard();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("./src/nz/ac/wgtn/swen225/lc/domain/DataStore/board.json"), g);
            System.out.println("JSON file created: person.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void mapJsonToBoard() {
//        GameBoard g = Mock.getGameBoard();
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            GameBoard board = mapper.readValue(new File("./src/nz/ac/wgtn/swen225/lc/domain/DataStore/board.json"), GameBoard.class);
//            System.out.println(board);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] s) {
        mapBoardToJson();
//        mapJsonToBoard();
    }
}
