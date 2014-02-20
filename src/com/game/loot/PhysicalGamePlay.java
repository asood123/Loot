package com.game.loot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		String input = null;
		boolean done = false;
		int num = 0;
		Color c = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		if (player instanceof VirtualPlayer) {
			// if player is virtual, ask which card was drawn for real and tell it
			while (!done) {
				try {
					System.out.println("Hi, I'm " + player.getName() + ". What card did I get?");
					input = br.readLine();
					card = Card.stringToCard(input);
					if (card != null) {
						done = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					System.err.println("Input unclear. Try again!");
				}
				if (!done) {
					System.out.println("Incorrect entry. Try again.");
				}
			}
		}
		player.addCard(card);
	}

}
