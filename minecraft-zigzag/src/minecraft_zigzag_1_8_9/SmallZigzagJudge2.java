package minecraft_zigzag_1_8_9;

public class SmallZigzagJudge2 extends SmallZigzagJudge {
  public double score(Player player) {
    final double xBiasMult=MathHelper.clamp_double(Math.abs((player.posZ+3.5)%4.0-2.0)-1.0,-0.2,0.2);
    final double xBiasBase=xBiasMult<0?-Math.max(player.posX+1.5,-0.2):-Math.min(player.posX+1.5,0.2);
    return player.posZ-startingState.posZ+xBiasBase*xBiasMult+0.25;
  }
}
