package com.game.loot;

public class Attacker {
	private CardSet attackCards;
	private int playerId;
	
	public Attacker(int playerId, Card card) {
		if (!((card instanceof PirateShip) ||
				(card instanceof Trump))) {
			throw new RuntimeException("Can't attack with something other than PirateShip or Trump");
		}
		attackCards = new CardSet();
		attackCards.addCard(card);
		this.playerId = playerId;
	}
	
	// getters
	public CardSet getAttackCards() {
		return attackCards;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	// other functions
	public void addCard(Card c) {
		if ((c instanceof PirateShip) || (c instanceof Trump)) {
			attackCards.addCard(c);
		}
		else {
			System.err.println("Add card in Attacker failed.");
		}
	}
	
	public String toString() {
		String s = new String();
		s += "P" + playerId + ":" + attackCards.toString();
		return s;
	}
}