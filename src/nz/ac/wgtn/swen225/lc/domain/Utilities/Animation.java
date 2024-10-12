package nz.ac.wgtn.swen225.lc.domain.Utilities;

public class Animation {
  Animation(String name, int length, int ticksPerFrame){
    this.name = name;
    this.length = length*ticksPerFrame;
    this.ticksPerFrame = ticksPerFrame;
  }

  private final int length;
  private final String name;
  private final int ticksPerFrame;
  private int frame;

  void tick(){
    frame++;
    if(frame > length) frame = 0;
  }

  public String toString(){
    return name + (frame/ticksPerFrame);
  }
}