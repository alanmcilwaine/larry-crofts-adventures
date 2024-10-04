package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.MovableBox;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

import java.util.ArrayList;
import java.util.List;

public record GameState(List<List<Tile<Item>>> board,
                        Player player,
                        List<Robot> robots,
                        List<MovableBox> boxes,
                        int timeLeft,
                        int level,
                        int width,
                        int height,
                        int totalTreasure) {


}
