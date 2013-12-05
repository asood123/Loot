package com.game.loot;

import java.util.List;
import java.util.Set;

public interface GameStateInterface {
	public List<Player> getPlayers();
	
	public Set<Card> getAllCards();
	
	public Set<Card> getUndrawnCards();
	
	public Set<Card> getDiscardCards();
	
	public List<Battle> getBattleList();
	
	public Player findPlayerById(int id);
	
	public Battle findBattleById(int id);
	
	public List<Battle> findBattlesbyPlayer(Player player);
	
	public List<Battle> findBattlesStartedbyPlayer(Player player);
}
