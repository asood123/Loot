package com.game.loot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VirtualGame {
	public static void main(String[] args){
		// get number of games to be played
		int numGames = 1;
		if (args.length > 0) {
			numGames = Integer.parseInt(args[0]);
		}
		
		Player p1 = new TriggerHappyAI("Aseem");
		Player p2 = new TriggerHappyAI("Derek");
		//Player p2 = new PhysicalPlayer("Derek", true);
		Player p3 = new TriggerHappyAI("Henry");
		
		List<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		
		for (int i = 0; i < numGames; i++) {
			for (Player player : players) player.setupForNewGame();
			GameState gameState = new GameState(players);
			CardSet deck = CardSet.addFullDeck();
			GamePlay gamePlay = new VirtualGamePlay(gameState, deck);
			LootEngine engine = new LootEngine(players, gamePlay, gameState);
			engine.play();
			
			//cycle order of players between rounds
			Collections.rotate(players, 1);
		}
		
		for (Player player : players) {
			System.out.println(player.getName() + " total wins:" + player.getNumWins() + ", points across rounds:" + player.getPointsAcrossRounds());
		}
		
	}
}

