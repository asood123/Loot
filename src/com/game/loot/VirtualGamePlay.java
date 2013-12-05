package com.game.loot;

import java.util.ArrayList;

public class VirtualGamePlay extends GamePlay {

	VirtualGamePlay(GameState gameState, CardSet deck) {
		super(gameState, deck);
	}

	@Override
	public void playerDrawsCard(Player player, CardSet deck) {
		// Actually draw a card from the virtual decksize
		ArrayList<Card> cards = deck.getCards();
		Card card = cards.get(0);
		deck.removeCard(card);
		
		player.addCard(card);
	}

}
