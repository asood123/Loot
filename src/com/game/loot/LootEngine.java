package com.game.loot;

import java.util.List;

public class LootEngine {
	List<Player> players;
	GamePlay gamePlay;
	GameState gameState;
	final String ANSI_CLS = "\u001b[2J";
    final String ANSI_HOME = "\u001b[H";	
	
	public LootEngine(List<Player> players, GamePlay gamePlay, GameState gameState) {
		this.players = players;
		this.gamePlay = gamePlay;
		this.gameState = gameState;
	}
	
	public void endGame() {
		System.out.println("Game Ended!");
		
		for (Player player : players){
			System.out.println(player.getName() + ":" + player.getFinalPoints());
		}
	}
	
	public void printBoard() {
		for (Player player : players) {
			System.out.println(player.getName() + "'s points: \t" + player.getPoints() + " | Cards in hand: " + player.getHandCount());
		}
		System.out.println("--- Last Moves: ");
		
		//System.out.println("Last moves:");
		for (Player player : players) {
			if (player.getLastMove() != null) {
				System.out.println(player.getName() + ": " + player.getLastMove().toString());
			}
		}
		System.out.println("--- Battles: ");
		for (Battle battle: gameState.getBattleList()) {
			System.out.println(battle);
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
		
		// Initialize all players now that they get to see their hands
		for (Player player : players) {
			player.init(gameState);
		}
		
		// Loop until game end condition
		while (!gamePlay.isEndOfGame()) {
			for (Player player : players) {
				if (!gamePlay.isEndOfGame()) {
					// clear screen
					//if (player instanceof PhysicalPlayer) {
						//System.out.print(ANSI_CLS + ANSI_HOME);
						//System.out.flush();
					//}
					//else {
						System.out.println("");
					//}
					gamePlay.collectMerchantShips(player);
					
					System.out.println("*** " + player.getName() + "'s Move");
					printBoard();
					System.out.println();
					

					boolean validMove = false;
					while (!validMove) {
						Move nextMove = player.getNextMove(gameState);
						System.out.println("Proposed Move: " + nextMove.toString());
						validMove = gamePlay.executeMove(player, nextMove);
						if (!validMove) {
							System.out.println("Invalid move, please try again");
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
