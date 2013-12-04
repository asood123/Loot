package com.game.loot;

import java.util.List;
import java.util.Set;

public interface PlayerInterface {
	Set<Card> getCurrentHand();
	
	Set<Card> setCurrentHand();
	
	void addCardToHand(Card card);
	
	List<Battle> getActiveBattles();
	
	void addBattle(Battle battle);
	
	void removeBattle(Battle battle);
	
	boolean isPartOfBattle(Battle battle);
	
	int getPointCount();
	
	void setPointCount();
	
	int getId();
	
	String getName();
	
	void setName(String name);
}
