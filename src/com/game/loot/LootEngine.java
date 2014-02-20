package com.game.loot;

import java.util.List;

import com.game.loot.Move.ACTION;
import com.game.loot.learning.FeatureLogger;

public class LootEngine {
	List<Player> players;
	GamePlay gamePlay;
	GameState gameState;
	final String ANSI_CLS = "\u001b[2J";
    final String ANSI_HOME = "\u001b[H";
    boolean verbose;
    FeatureLogger logger;
	
	public LootEngine(List<Player> players, GamePlay gamePlay, GameState gameState) {
		this.players = players;
		this.gamePlay = gamePlay;
		this.gameState = gameState;
		this.verbose = true;
		this.logger = null;
	}
	
	public void setVerbosity(boolean v) {
		this.verbose = v;
	}
	
	public void setLogger(FeatureLogger logger) {
		this.logger = logger;
	}
	
	public void endGame() {
		if (verbose) {
			System.out.println("Game Ended!");
		
			for (Player player : players){
				System.out.println(player.getName() + ":" + player.getFinalPoints());
			}
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
		
		if (verbose) {
			System.out.println("Starting Game!");
		
			System.out.println("Dealing cards...");
		}
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

					if (verbose) {
						System.out.println("");
					}
					gamePlay.collectMerchantShips(player);
					
					if (verbose) {
						System.out.println("*** " + player.getName() + "'s Move");
						printBoard();
						System.out.println();
					}
					

					boolean validMove = false;
					while (!validMove) {
						Move nextMove = player.getNextMove(gameState);
						if (verbose) {
							System.out.println("Proposed Move: " + nextMove.toString());
						}
						
						// This assumes that the nextMove is valid...if not then we're logging extra data
						if (logger != null) {
							logger.logMove(gameState, nextMove, player);
						}
						
						validMove = gamePlay.executeMove(player, nextMove);
						if (!validMove) {
							System.err.println("Invalid move, please try again");
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
