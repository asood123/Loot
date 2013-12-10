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
	
	public void endGame() {
		System.out.println("Game Ended!");
		
		for (Player player : players){
			System.out.println(player.getName() + ":" + player.getPoints());
		}
	}
	
	public void printBoard() {
		for (Player player : players) {
			List<Battle> battles = gameState.findBattlesOwnedByPlayer(player);
			
			if (battles.size() > 0) {
				System.out.println("Battles in front of " + player.getName());
				for (Battle battle : battles) {
					System.out.println(battle);
				}
			}
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
				if (!gamePlay.isEndOfGame()) {
					System.out.println();
					System.out.println("*** " + player.getName() + "'s Move");
					printBoard();
					System.out.println();
					gamePlay.collectMerchantShips(player);

					boolean validMove = false;
					while (!validMove) {
						Move nextMove = player.getNextMove(gameState);
						System.out.println("Proposed Move: " + nextMove.toString());
						validMove = gamePlay.executeMove(player, nextMove);
						if (!validMove) {
							System.out.println("Invalid move, please try again");
							return; // added only for debugging reasons, TODO: remove eventually
						} else {
							gameState.addMoveToHistory(nextMove);
							player.addMove(nextMove); 
						}
					}
				}
			}
		}
		endGame();
	}
}
