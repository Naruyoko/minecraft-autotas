package minecraft_zigzag_1_8_9;

import minecraft_simulator.v1_8_9.util.MathHelper;
import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;

public class SmallZigzagJudge2 extends SmallZigzagJudge {
  public double score(SprintingClearStoneXZPlayer player) {
    final double xBiasMult=MathHelper.clamp_double(Math.abs((player.posZ+3.5)%4.0-2.0)-1.0,-0.2,0.2);
    final double xBiasBase=xBiasMult<0?-Math.max(player.posX+1.5,-0.2):-Math.min(player.posX+1.5,0.2);
    return player.posZ-startingState.posZ+xBiasBase*xBiasMult*0.25;
  }
}
