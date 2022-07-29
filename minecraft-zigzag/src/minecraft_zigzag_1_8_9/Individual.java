package minecraft_zigzag_1_8_9;

public class Individual {
  public int[] mouseMovements;
  public Player player;
  public double scoreCache;
  public Individual(int[] mouseMovements,Player player,double scoreCache){
    this.mouseMovements=mouseMovements;
    this.player=player;
    this.scoreCache=scoreCache;
  }
}
