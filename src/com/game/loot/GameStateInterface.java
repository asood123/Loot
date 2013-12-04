package com.game.loot;

import java.util.List;
import java.util.Set;

public interface GameStateInterface {
	public List<Player> getPlayers();
	
	public Set<Card> getAllCards();
	
	public Set<Card> getUndrawnCards();
	
	public Set<Card> getDiscardCards();
	
	public List<Battle> getBattleList();
	
	public Player getCurrentPlayer();
	
	public Player findPlayer(int pId);
	
	public Battle findBattle(int bId);
	
	public List<Battle> findBattlesbyPlayer(int pId);
	
	public List<Battle> findBattlesStartedbyPlayer(int pId);
}
