package nz.ac.wgtn.swen225.lc.Domain;

import nz.ac.wgtn.swen225.lc.Domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.Domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.Domain.Interface.Item;

import java.util.List;

public record GameState(List<List<Tile<Item>>> board, Player player, List<Robot> robots, int timeLeft, int level) {
}
