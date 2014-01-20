package com.game.loot;

import java.util.ArrayList;

public class PhysicalGamePlay extends GamePlay {

	PhysicalGamePlay(GameState gameState) {
		super(gameState);
	}

	@Override
	public void playerDrawsCard(Player player, CardSet deck) {
		// --deck size  we don't actually care about the card since we don't know what it was
		ArrayList<Card> cards = deck.getCards();
		deck.removeCard(cards.get(0));
		
		Card card = null;
		if (player instanceof VirtualPlayer) {
			// if player is virtual, ask which card was drawn for real and tell it
			System.out.println("Hi, what card did I get?");
			// TODO: Ask for card
			throw new RuntimeException();
		}
		player.addCard(card);
	}

}
