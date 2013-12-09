package com.game.loot;

import java.util.List;

public class VirtualPlayer extends Player {

	private CardSet hand;
	private CardSet unknownCards;
	
	public VirtualPlayer(String name) {
		super(name);
		hand = new CardSet();
		unknownCards = CardSet.addFullDeck();
	}

	@Override
	public int getHandCount() {
		return hand.getCount();
	}
	
	public CardSet getHand(){
		return hand;
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
		System.out.println("I'm " + getName() + " and I just got card: " + card + "  and my hand is now:  " + hand);
		newCardSeen(card);
	}

	@Override
	public void removeCard(Card card) {
		hand.removeCard(card);
		System.out.println("I'm " + getName() + " and I just removed card: " + card + "  and my hand is now:  " + hand);
	}
	
	// helper functions
	
	public void runPreMoveHelpers(GameState gm){
		// update known and unknown card sets
		updateUnknownCards(gm.getPlayers());
		
		// will probably have other things eventually
	}
	
	public void updateUnknownCards(List<Player> players){
		for (Player p: players) {
			if (p.getLastMove() != null){
				newCardSeen(p.getLastMove().getCard());
			}
		}
	}
	
	public void newCardSeen(Card c) {
		unknownCards.removeCard(c);
	}
	
}
