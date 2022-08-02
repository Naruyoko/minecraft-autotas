package minecraft_zigzag_1_8_9;

import minecraft_simulator.v1_8_9.Blocks;
import minecraft_simulator.v1_8_9.MathHelper;
import minecraft_simulator.v1_8_9.SprintingClearStoneXZPlayer;

public class LongSmallZigzagJudge extends SmallZigzagJudge {
  public static final boolean[] pathShape=new boolean[]{
    true,false,false,true,true,true,
    true,false,false,true,false,false,
    true,true,true,true,false,false
  };
  boolean canStandOnBlock(SprintingClearStoneXZPlayer player,int x,int z){
    // Calculate path shape
    if (x>=0||x<-3||z<0||!pathShape[(-1-x)*6+z%6]) return false;
    return Blocks.fullBlock.hasAnyCollidingBoundingBoxes(x, z, player.boundingBox);
  }
  public double score(SprintingClearStoneXZPlayer player) {
    final double xBiasMult=MathHelper.clamp_double(Math.abs((player.posZ+5.5)%6.0-3.0)-1.5,-0.7,0.7);
    final double xBiasBase=xBiasMult<0.0?-Math.min(player.posX+1.5,0.2):-Math.max(player.posX+1.5,-0.2);
    return player.posZ-startingState.posZ+xBiasBase*xBiasMult*0.8;
  }
}
