package com.game.loot;

public class Attacker {
	private CardSet attackCards;
	private Player player;
	private int lastMoveNum;
	
	public Attacker(Player player, int moveNum) {
		attackCards = new CardSet();
		this.player = player;
		this.lastMoveNum = moveNum;
	}
	
	public Attacker(Player player, Card card, int moveNum) {
		attackCards = new CardSet();
		attackCards.addCard(card);
		this.player = player;
		this.lastMoveNum = moveNum;
	}
	
	// getters
	public CardSet getAttackCards() {
		return attackCards;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getLastMoveNum() {
		return lastMoveNum;
	}
	
	// other functions
	public void addCard(AttackCard c, int moveNum) {
		attackCards.addCard(c);
		lastMoveNum = moveNum;
	}
	
	public String toString() {
		String s = new String();
		s += "P" + player.getId() + ":" + attackCards.toString();
		return s;
	}
}