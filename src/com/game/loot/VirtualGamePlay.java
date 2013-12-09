package com.game.loot;

import java.util.ArrayList;
import java.util.Random;

public class VirtualGamePlay extends GamePlay {
	private Random rand;
	VirtualGamePlay(GameState gameState, CardSet deck) {
		super(gameState, deck);
		rand =  new Random();
	}

	@Override
	public void playerDrawsCard(Player player, CardSet deck) {
		// Actually draw a card from the virtual decksize
		ArrayList<Card> cards = deck.getCards();
		
		//TODO: add a check for running out of the deck

		int  n = rand.nextInt(deck.getCount());
		Card card = cards.get(n);
		deck.removeCard(card);
		
		player.addCard(card);
	}

}
