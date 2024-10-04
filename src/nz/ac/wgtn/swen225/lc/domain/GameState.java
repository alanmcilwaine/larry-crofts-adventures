package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Game state API
 * @param board board in 2D list
 * @param player player
 * @param robots list of robots
 * @param timeLeft time
 * @param level game level
 * @param width board width
 * @param height board height
 * @param totalTreasure total treasure on the game board
 * @author Yee Li
 */
public record GameState(List<List<Tile<Item>>> board,
                        Player player,
                        List<Robot> robots,
                        int timeLeft,
                        int level,
                        int width,
                        int height,
                        int totalTreasure) {
}
