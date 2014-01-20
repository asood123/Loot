package com.game.loot;

import java.util.ArrayList;
import java.util.List;

public class VirtualGame {
	public static void main(String[] args){
		Player p1 = new RandomAI("Stats");
		//Player p2 = new RandomAI("Derek");
		Player p2 = new PhysicalPlayer("Derek", true);
		Player p3 = new TriggerHappyAI("Trigger");
		
		List<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		CardSet deck = CardSet.addFullDeck();
		
		GameState gameState = new GameState(players, deck);
		
		GamePlay gamePlay = new VirtualGamePlay(gameState);
		
		LootEngine engine = new LootEngine(players, gamePlay, gameState);
		engine.play();
	}
}

