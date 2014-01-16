package com.game.loot;

import java.util.List;
import java.util.Random;

public class VirtualPlayer extends Player {

	private CardSet hand;
	private CardSet unknownCards;
	private int deckSize;
	private boolean verbose;
	
	public VirtualPlayer(String name) {
		super(name);
		hand = new CardSet();
		unknownCards = CardSet.addFullDeck();
		deckSize = 100; 
		verbose = true;
	}

	@Override
	public int getHandCount() {
		return hand.getCount();
	}
	
	public CardSet getHand(){
		return hand;
	}
	
	public int getDeckSize(){
		return deckSize;
	}
	
	public int getId(){
		return super.getId();
	}
	
	public CardSet getUnknownCards(){
		return unknownCards;
	}
	
	public boolean getVerbosity(){
		return verbose;
	}
	
	public void setVerbosity(boolean v){
		verbose = v;
	}

	@Override
	public Move getNextMove(GameState gm) {
		// Always draw for now
		runPreMoveHelpers(gm);
		return new Move(ACTION.DRAW, null, null);
	}

	@Override
	public void addCard(Card card) {
		hand.addCard(card);
		if (verbose) {
			System.out.println("I'm " + getName() + " and I just got card: " + card + "  and my hand is now:  " + hand);
		}
		newCardSeen(card);
	}

	@Override
	public void removeCard(Card card) {
		hand.removeCard(card);
		if (verbose) {
			System.out.println("I'm " + getName() + " and I just removed card: " + card + "  and my hand is now:  " + hand);
		}
	}
	
	@Override
	public int getFinalPoints(){
		return getPoints() - hand.sumMShips();
	}
	
	// helper functions
	
	public void runPreMoveHelpers(GameState gm){
		// update known and unknown card sets
		updateUnknownCards(gm.getPlayers());
		updateDeckSize(gm.getPlayers());
	}
	
	public void updateUnknownCards(List<Player> players){
		for (Player p: players) {
			if (p.getId() != getId() && p.getLastMove() != null  && p.getLastMove().getAction() != ACTION.DRAW) {
				newCardSeen(p.getLastMove().getCard());
			}
		}
	}
	
	public void updateDeckSize(List<Player> players){
		if (deckSize == 100) {
			// initialize it
			deckSize = 78-(players.size())*6;
		}
		for (Player p: players) {
			if (p.getLastMove() != null){
				if (p.getLastMove().getAction() == ACTION.DRAW) {
					deckSize--;
				}
			}
		}
	}
	
	public void newCardSeen(Card c) {
		
		unknownCards.removeCard(c);
	}

	@Override
	public void init(GameState gm) {
	}
}
