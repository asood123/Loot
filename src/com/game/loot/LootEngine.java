package com.game.loot;

import java.util.List;

public class LootEngine {
	List<Player> players;
	GamePlay gamePlay;
	GameState gameState;
	
	
	public LootEngine(List<Player> players, GamePlay gamePlay, GameState gameState) {
		this.players = players;
		this.gamePlay = gamePlay;
		this.gameState = gameState;
	}
	
	public void endGame(){
		System.out.println("Game Ended!");
		
		for (Player player : players){
			System.out.println(player.getName() + ":" + player.getPoints());
		}
	}
	
	void play() {
		System.out.println("Starting Game!");
		System.out.println("Dealing cards...");
		for (int x = 0; x < 6; x++) {
			for (Player player : players) {
				gamePlay.executeMove(player, new Move(ACTION.DRAW, null, null));
			}
		}
		
		// Loop until game end condition
		while (!gamePlay.isEndOfGame()) {
			for (Player player : players) {
				System.out.println("Next move: " + player.getName());
				gamePlay.collectMerchantShips(player);
				Move nextMove = player.getNextMove(gameState);
				gamePlay.executeMove(player, nextMove);
			}
		}
		endGame();
	}
}
