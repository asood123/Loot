package com.game.loot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/*
 * Stores current game state. 
 * TODO: Add a function to verify current gamestate is legit (mainly no duplicate cards and no missing cards)
 */
public class GameState {
	private CardSet discardPile;
	
	private List<Player> players;
	private Iterator<Player> playerIterator;
	private Player currentPlayer; // tracking turn

	private List<Battle> battles;
	
	private List<Move> moveHistory;
	
	// initializers
	
	public GameState(List<Player> players) {
		this.players = players;
		
		// initiate cards
		discardPile = new CardSet();
		
		playerIterator = players.iterator(); // first player's turn
		currentPlayer = playerIterator.next();
		
		// initiate battles
		battles = new ArrayList<Battle>();
		moveHistory = new ArrayList<Move>();
		
	}

	// Getters
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Battle> getBattleList() {
		return battles;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void addMoveToHistory(Move move) {
		moveHistory.add(move);
	}
	
	public List<Move> getMoveHistory() {
		return moveHistory;
	}
	
	public int getMoveCount() {
		return moveHistory.size();
	}

	// Setters

	
	// game functions
	public CardSet getDiscardCards() {
		return discardPile;
	}

	public Player findPlayerById(int pId) {
		for (Player player : players) {
			if (player.getId() == pId) {
				return player;
			}
		}
		return null;
	}

	public Battle findBattleById(int bId) {
		for (Battle battle : battles){
			if (battle.getId() == bId){
				return battle;
			}
		}
		return null;
	}

	public List<Battle> findBattlesOwnedByPlayer(Player player) {
		int pId = player.getId();
		List<Battle> battleList = new ArrayList<Battle>();
		
		for (Battle battle : battles) {
			if (battle.getOwnerPlayerId() == pId) {
				battleList.add(battle);
			}
		}
		return battleList;
	}
/*
	@Override
	public List<Battle> findBattlesStartedbyPlayer(Player player) {
		int pId = player.getId();
		List<Battle> battleList = new ArrayList<Battle>();
		
		for (Battle battle : battles) {
			if (battle.getOwnerPlayerId() == pId) {
				battleList.add(battle);
			}
		}
		return battleList;
	}
	*/
	// test/debug functions
	
	public boolean verifyGameState(){
		// check that sum of player's hands + mShips collected + discard pile + decksize + knowncards = 78
		int handCount = 0;
		int points = 0;
		for (Player player : players) {
			handCount += player.getHandCount();
			points += player.getPoints();
		}
		/*
		if (hCount + mCount + discardPile.getCount() + deckSize + knownCards.getCount() != 78){
			System.err.println("Verify test 1 failed");
			System.out.println("All cards in players' hands: " + handCount);
			System.out.println("mShips collected: " + mCount);
			System.out.println("discardPile: " + discardPile.getCount());
			System.out.println("deckSize: " + deckSize);
			System.out.println("knownCards: " + knownCards.getCount());
			return false;
		}
		*/
		return true;
	}
}
