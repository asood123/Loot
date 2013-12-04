package com.game.loot;

import java.util.List;

public class LootEngine {
	List<Player> players;
	LootEngine(List<Player> players) {
		this.players = players;
	}
	
	void play() {
		GameState gameState = new GameState(0, null);
		// Create Deck
		// Deal Cards
		
		// Loop until game end condition
		for (Player player : players) {
			Move nextMove = player.getNextMove(gameState);
			// Update Gamestate with Move
		}
	}
}
