package com.game.loot;

import java.util.ArrayList;
import java.util.Iterator;


/*
 * Stores current game state. 
 * TODO: Add a function to verify current gamestate is legit (mainly no duplicate cards and no missing cards)
 */
public class GameState {

	private static CardSet allCards; // every card in deck for reference
	private CardSet unknownCards; // not seen yet
	private CardSet discardPile;
	private CardSet knownCards; // not discarded and not in main deck
	private int deckSize; // count of remaining cards
	
	private ArrayList<Player> players;
	private Iterator<Player> playerIterator; // tracking turn;
	private Player currentPlayer; // tracking turn

	private ArrayList<Battle> battles;
	
	private int move = 1;
	
	
	
	// initializers
	
	public GameState(int numPlayers, String[] playerNames){
		
		// initiate cards
		allCards = CardSet.addFullDeck();
		unknownCards = CardSet.addFullDeck();
		discardPile = new CardSet();
		knownCards = new CardSet();
		deckSize = 78-6*numPlayers; // everything but what's dealt to players
		
		// initiate players
		players = new ArrayList<Player>();
		for (int i = 0; i<numPlayers; i++){
			if ((playerNames == null) || playerNames.length < i + 1 || playerNames[i] == null){
				players.add(new Player());
			}
			else {
				players.add(new Player(playerNames[i])); 
			}
		}
		playerIterator = players.iterator(); // first player's turn
		currentPlayer = playerIterator.next();
		
		// initiate battles
		battles = new ArrayList<Battle>();
	}

	// Getters
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public CardSet getAllCards(){
		return allCards;
	}
	
	public CardSet getUnknownCards(){
		return unknownCards;
	}
	
	public ArrayList<Battle> getBattleList(){
		return battles;
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	// Setters

	
	// game functions
	
	
	public void collectMerchantShips(){
		// checks if the current player can collect any merchant ships
		ArrayList<Battle> doneBattles = new ArrayList<Battle>();
		for (Battle b: battles) {
			if (b.calcCurrentWinner() == currentPlayer.getId()){
				// add merchant ship to currentplayer
				currentPlayer.addMShip(b.getMShip());
				knownCards.removeCard(b.getMShip());

				// add rest of cards to discardpile
				CardSet cs = b.allCards();
				cs.removeCard(b.getMShip());
				discardPile.addCardSet(cs);
				knownCards.removeCardSet(cs);
				
				// remove battle from each player who was involved
				for (Player p: players) {
					if (p.getActiveBattles().contains(b)){
						p.removeBattle(b);
					}
				}
				
				// remove battle from gamestate and system
				doneBattles.add(b);
			}
		}
		for (Battle b: doneBattles) {
			battles.remove(b);
		}		
	}
	
	public boolean drawCard(int playerId){
		if ((currentPlayer.getId() == playerId) && 
				deckSize > 0) {
			currentPlayer.insertCardInHand();
			deckSize--;
			return true;
		}
		else {
			System.err.println("Error: Not this player's turn or deckSize is zero");
			return false;
		}
	}
	
	public void playCard(Card c, int playerId, int battleId) {
		if (currentPlayer.getId() == playerId){	
			if (c instanceof MerchantShip){
				currentPlayer.removeCardFromHand();
				knownCards.addCard(c);
				Battle b = new Battle(((MerchantShip)c), playerId);
				battles.add(b);
				currentPlayer.addBattle(b);
			}
			else {
				if (battleId == 0){
					// means card is discarded; check that decksize = 0;
					if (deckSize == 0) {
						currentPlayer.removeCardFromHand();
						discardPile.addCard(c);
					}
					else {
						System.err.println("Invalid move: Can't discard card when deck has remaining cards");
					}
				}
				else {
					// legit attack
					Battle b = findBattle(battleId);
					b.addCard(c, playerId);
					if (!currentPlayer.isPartOfBattle(battleId)) {
						currentPlayer.addBattle(b);
					}
					currentPlayer.removeCardFromHand();
					knownCards.addCard(c);
				}
			}
			unknownCards.removeCard(c);
		}
	}

	public boolean hasGameEnded(){
		if (deckSize == 0){ // if decksize is 0
			// and any player is down to zero cards
			for (Player p: players) {
				if (p.getHandCount() == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void nextTurn(){
		move++;
		System.out.println("Move: " + move);
		if (playerIterator.hasNext()) {
			currentPlayer = playerIterator.next();
		}
		else {
			playerIterator = players.iterator();
			currentPlayer = playerIterator.next();
		}
		collectMerchantShips();
		
	}
	
	public void endGame(){
		System.out.println("Game Ended!");
		for (Player p: players){
			System.out.println(p.getName() + ":" + p.getMShips().sumMShips());
		}
	}

	
	// search functions
	
	public Player findPlayer(int pId){
		for (Player p: players){
			if (p.getId() == pId){
				return p;
			}
		}
		return null;
	}
	
	public Battle findBattle(int bId){
		for (Battle b: battles){
			if (b.getId() == bId){
				return b;
			}
		}
		return null;
	}
	
	public ArrayList<Battle> findBattlesbyPlayer(int pId){
		ArrayList<Battle> bList = new ArrayList<Battle>();
		
		for (Battle b: battles) {
			if (b.getMShipPlayerId() == pId) {
				bList.add(b);
			}
			else {
				for (Attacker a: b.getPShips()){
					if (a.getPlayerId() == pId){
						bList.add(b);
					}
				}
			}
		}
		return bList;
	}
	
	public ArrayList<Battle> findBattlesStartedbyPlayer(int pId){
		ArrayList<Battle> bList = new ArrayList<Battle>();
		
		for (Battle b: battles) {
			if (b.getMShipPlayerId() == pId) {
				bList.add(b);
			}
		}
		return bList;
	}
	
	// test/debug functions
	
	public boolean verifyGameState(){
		// check that sum of player's hands + mShips collected + discard pile + decksize + knowncards = 78
		int hCount = 0;
		int mCount = 0;
		for (Player p: players) {
			hCount += p.getHandCount();
			mCount += p.getMShips().getCount();
		}
		if (hCount + mCount + discardPile.getCount() + deckSize + knownCards.getCount() != 78){
			System.err.println("Verify test 1 failed");
			System.out.println("All cards in players' hands: " + hCount);
			System.out.println("mShips collected: " + mCount);
			System.out.println("discardPile: " + discardPile.getCount());
			System.out.println("deckSize: " + deckSize);
			System.out.println("knownCards: " + knownCards.getCount());
			return false;
		}
		
		return true;
	}

}
