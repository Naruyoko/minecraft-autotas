package minecraft_zigzag_1_8_9;

public interface IJudge {
  public Player getStartingState();
  public boolean isValid(Player lastPlayer,Player currentPlayer);
  public double score(Player player);
}
