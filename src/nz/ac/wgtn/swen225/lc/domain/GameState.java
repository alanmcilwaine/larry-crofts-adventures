package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.GameActor.Player;
import nz.ac.wgtn.swen225.lc.domain.GameActor.Robot;
import nz.ac.wgtn.swen225.lc.domain.Interface.Item;

import java.util.ArrayList;
import java.util.List;

public record GameState(List<List<Tile<Item>>> board,
                        Player player,
                        List<Robot> robots,
                        int timeLeft,
                        int level) {


  /**
   * Gives a deep copy of a given gameState
   * @param original gameState
   * @return new deep copy of gameState
   */
  public GameState copyOf(GameState original) {
    return new GameState(new ArrayList<>(this.board),
                          new Player(this.player().getLocation()),
                          new ArrayList<>(this.robots),
                          this.timeLeft,
                          this.level);
  }

}
