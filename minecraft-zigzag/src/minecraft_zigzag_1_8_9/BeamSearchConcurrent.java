package minecraft_zigzag_1_8_9;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BeamSearchConcurrent {
  public static final int maximumMovementPerTick=1500;
  public static final int maximumMovementPerFrame=900;
  private final int solutionLength;
  private final int beamWidth;
  private final int angleStep;
  private final IJudge judge;
  private final ArrayBlockingQueue<Individual> lastBeam;
  private final PriorityQueue<Individual> beam;
  private final CalculationService calculationService;
  private Individual bestIndividual;
  private double bestScore;
  private double bestLosingScore;
  private long lastNanoTime;
  public BeamSearchConcurrent(int solutionLength,int beamWidth,int angleStep,IJudge judge){
    this.solutionLength=solutionLength;
    this.beamWidth=beamWidth;
    this.angleStep=angleStep;
    this.judge=judge;
    Comparator<Individual> comparator=new Comparator<Individual>(){
      public int compare(Individual o1, Individual o2) {
        return Double.compare(o1.scoreCache,o2.scoreCache);
      }
    };
    lastBeam=new ArrayBlockingQueue<Individual>(beamWidth);
    beam=new PriorityQueue<Individual>(comparator);
    calculationService=new CalculationService(this);
  }
  public int getSolutionLength(){
    return this.solutionLength;
  }
  public int getBeamWidth(){
    return this.beamWidth;
  }
  public int getAngleStep(){
    return this.angleStep;
  }
  public IJudge getJudge(){
    return this.judge;
  }
  public ArrayBlockingQueue<Individual> getLastBeam(){
    return this.lastBeam;
  }
  public Individual getBestIndividual(){
    return this.bestIndividual;
  }
  public double getBestScore(){
    return this.bestIndividual.scoreCache;
  }
  public static void moveCameraWithDivision(Player player,int pixels){
    if (pixels<-maximumMovementPerFrame){
      player.moveCamera(-maximumMovementPerFrame);
      player.moveCamera(pixels+maximumMovementPerFrame);
    }else if (pixels>maximumMovementPerFrame){
      player.moveCamera(maximumMovementPerFrame);
      player.moveCamera(pixels-maximumMovementPerFrame);
    }else{
      player.moveCamera(pixels);
    }
  }
  public static void stepPlayer(Player player) {
    player.step(-1.0F,1.0F);
  }
  public synchronized double offerBeam(Individual newIndividual){
    final double score=newIndividual.scoreCache;
    if (score<=bestLosingScore) return bestLosingScore;
    beam.add(newIndividual);
    double loserScore=Double.NEGATIVE_INFINITY;
    if (beam.size()>beamWidth){
      Individual loser=beam.poll();
      if (loser.scoreCache>bestLosingScore) loserScore=bestLosingScore=loser.scoreCache;
    }
    if (score>bestScore){
      bestIndividual=newIndividual;
      bestScore=score;
    }
    return loserScore;
  }
  public void init(boolean consoleOut){
    Player workingPlayer=judge.getStartingState();
    lastBeam.clear();
    beam.clear();
    beam.add(new Individual(new int[solutionLength],workingPlayer.clone(),judge.score(workingPlayer)));
    lastNanoTime=System.nanoTime();
  }
  class CalculationService implements Runnable{
    private final BeamSearchConcurrent searcher;
    private ExecutorService threadPool;
    private int t;
    private final int NTHREAD=10;
    public CalculationService(BeamSearchConcurrent searcher) {
      this.searcher=searcher;
    }
    public void run(){
      threadPool=Executors.newCachedThreadPool();
      for (int i=0;i<NTHREAD;i++){
        threadPool.submit(new Handler(searcher,t));
      }
      threadPool.shutdown();
    }
    public void run(int t){
      this.t=t;
      run();
    }
    public boolean awaitTermination(long timeout,TimeUnit unit) throws InterruptedException{
      return threadPool.awaitTermination(timeout, unit);
    }
  }
  class Handler implements Runnable{
    private final BeamSearchConcurrent searcher;
    private final int t;
    private final Player workingPlayer;
    private double bestLosingScoreCopy;
    public Handler(BeamSearchConcurrent searcher,int t){
      this.searcher=searcher;
      this.t=t;
      this.workingPlayer=new Player();
      this.bestLosingScoreCopy=Double.NEGATIVE_INFINITY;
    }
    public void run() {
      IJudge judge=searcher.getJudge();
      ArrayBlockingQueue<Individual> lastBeam=searcher.getLastBeam();
      while (true){
        Individual individual=lastBeam.poll();
        if (individual==null) break;
        for (int x=Math.max(-maximumMovementPerTick,(int)((-135F-(individual.player.yaw+45F))/(Player.mouseMult*0.15D)));x<=maximumMovementPerTick;x+=angleStep){
          Player.copy(workingPlayer,individual.player);
          moveCameraWithDivision(workingPlayer,x);
          if (workingPlayer.yaw+45F>135F) break;
          stepPlayer(workingPlayer);
          double score=judge.score(workingPlayer);
          if (score<=bestLosingScoreCopy) continue;
          if (!judge.isValid(individual.player,workingPlayer)) continue;
          Individual newIndividual=new Individual(individual.mouseMovements.clone(),workingPlayer.clone(),score);
          newIndividual.mouseMovements[t]=x;
          double newBestLosingScore=offerBeam(newIndividual);
          if (newBestLosingScore>bestLosingScoreCopy) bestLosingScoreCopy=newBestLosingScore;
        }
      }
    }
  }
  public Individual searchNextLevel(int t,boolean consoleOut) throws InterruptedException{
    lastBeam.clear();
    lastBeam.addAll(beam);
    bestIndividual=null;
    bestScore=Double.NEGATIVE_INFINITY;
    bestLosingScore=Double.NEGATIVE_INFINITY;
    long nanoTime=System.nanoTime();
    if (consoleOut) System.out.println(String.format("Tick %d, last beam size %d %d",t+1,lastBeam.size(),nanoTime-lastNanoTime));
    lastNanoTime=nanoTime;
    calculationService.run(t);
    while (!calculationService.awaitTermination(10,TimeUnit.SECONDS)){
      if (consoleOut) System.out.println(String.format("Best: %s %s (Remaining: %d)",Double.toString(bestScore),Arrays.toString(bestIndividual.mouseMovements),lastBeam.size()));
    }
    if (consoleOut) System.out.println(String.format("Best: %s %s",Double.toString(bestScore),Arrays.toString(bestIndividual.mouseMovements)));
    return bestIndividual;
  }
  public Individual search(boolean consoleOut){
    init(consoleOut);
    try {
      for (int t=0;t<solutionLength;t++) searchNextLevel(t, consoleOut);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return bestIndividual;
  }
  public static void main(String[] args) {
    Date date=new Date();
    BeamSearchConcurrent searcher=new BeamSearchConcurrent(100, 1000000, 100, new SmallZigzagJudge2());
    Individual result=searcher.search(true);
    try {
      DateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
      String formattedDate=dateFormat.format(date);
      BufferedWriter writer1=new BufferedWriter(new FileWriter(String.format("result-%s.txt",formattedDate),false));
      BufferedWriter writer2=new BufferedWriter(new FileWriter(String.format("result-%s.mcsim",formattedDate),false));
      writer1.write(Arrays.toString(result.mouseMovements));
      writer1.newLine();
      writer1.newLine();
      Player player=searcher.judge.getStartingState();
      writer2.write(
        "!property"+System.lineSeparator()+
        String.format("startPosition=%f,%f,%f",player.posX,100.0,player.posZ)+System.lineSeparator()+
        String.format("startMotion=%f,%f,%f",player.velX,0.0,player.velZ)+System.lineSeparator()+
        "startInvulnerabilityFrames=0"+System.lineSeparator()+
        "startGametype=NOT_SET"+System.lineSeparator()+
        String.format("mouseSensitivity=%f",Player.mouseSensitivity)+System.lineSeparator()+
        String.format("mouseMaxSafeMovement=%d",maximumMovementPerFrame)+System.lineSeparator()+
        String.format("tickLength=%d",searcher.getSolutionLength()+20)+System.lineSeparator()+
        "rerecords=0"+System.lineSeparator()+
        "predictionRerecords=0"+System.lineSeparator()+
        "totalRerecords=0"+System.lineSeparator()+
        "fileFormatVersion=2"+System.lineSeparator()+
        "editorVersion=0.0.6"+System.lineSeparator()+
        ""+System.lineSeparator()+
        "!input"+System.lineSeparator()+
        "###|WASD_+^|Yaw,Pitch|Flags"+System.lineSeparator());
      for (int t=-1;t<searcher.getSolutionLength();t++){
        if (t>=0){
          moveCameraWithDivision(player, result.mouseMovements[t]);
          stepPlayer(player);
        }
        writer1.write(String.format("%3d %5s %-22s %-10s %-22s %-22s %-22s %-22s",t+1,t>=0?result.mouseMovements[t]:"-",Utility.padSignDouble(searcher.judge.score(player)),Utility.padSignFloat(player.yaw),Utility.padSignDouble(player.posX),Utility.padSignDouble(player.posZ),Utility.padSignDouble(player.velX),Utility.padSignDouble(player.velZ)));
        writer1.newLine();
        if (t>=0){
          writer2.write(String.format("%d|W..D..^|%s,%s|",t+1,Double.toString((double)player.yaw),Double.toString(0.0D)));
          writer2.newLine();
        }
      }
      writer1.close();
      writer2.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
