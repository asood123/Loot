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
	
	public Color getAttackerColor(){
		if (attackCards.getCards().size() > 0) {
			Card c = attackCards.getCards().get(0);
			if (c instanceof PirateShip) {
				return ((PirateShip) c).getColor();
			}
			else if (c instanceof Trump) {
				return ((Trump) c).getColor();
			}
		}
		return null;
	}
	
	public int getScore(){
		int score = 0;
		if (attackCards.hasTrump()) {
			return Integer.MAX_VALUE;
		}
		for (Card c: attackCards.getCards()) {
			score += c.getValue();
		}
		return score;
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