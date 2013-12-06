package com.game.loot;

import java.util.ArrayList;
import java.util.List;

public class PhysicalGame {
	public static void main(String[] args){
		Player p1 = new PhysicalPlayer("Derek", false);
		Player p2 = new VirtualPlayer("Aseem");
		
		List<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		GameState gameState = new GameState(players);
		
		CardSet deck = CardSet.addFullDeck();
		
		GamePlay gamePlay = new PhysicalGamePlay(gameState, deck);
		
		LootEngine engine = new LootEngine(players, gamePlay, gameState);
		engine.play();
	}
}