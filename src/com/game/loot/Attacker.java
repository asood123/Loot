package com.game.loot;

public class Attacker {
	private CardSet attackCards;
	private Player player;
	
	public Attacker(Player player, Card card) {
		if (!((card instanceof PirateShip) ||
				(card instanceof Trump))) {
			throw new RuntimeException("Can't attack with something other than PirateShip or Trump");
		}
		attackCards = new CardSet();
		attackCards.addCard(card);
		this.player = player;
	}
	
	// getters
	public CardSet getAttackCards() {
		return attackCards;
	}
	
	public Player getPlayer() {
		return player;
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
		s += "P" + player.getId() + ":" + attackCards.toString();
		return s;
	}
}