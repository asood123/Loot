package com.game.loot;

public class Attacker {
	private CardSet cards;
	private int playerId;
	private int order; // for tracking when trumps are played.
	
	// initializers
	public Attacker(Card c, int id, int o){
		if ((c instanceof PirateShip) ||
				(c instanceof Trump)){
			cards = new CardSet();
			cards.addCard(c);
			playerId = id;
			order = o;
		}
	}
	
	// getters
	
	public CardSet getCardSet(){
		return cards;
	}
	
	public int getPlayerId(){
		return playerId;
	}
	
	public int getOrder(){
		return order;
	}
	
	// other functions
	public void addCard(Card c, int o) {
		if ((c instanceof PirateShip) || (c instanceof Trump)){
			cards.addCard(c);
			order = o;
		}
		else {
			System.err.println("Add card in Attacker failed.");
		}
	}
	
	public String toString(){
		String s = new String();
		s += "P" + playerId + ":" + cards.toString();
		return s;
	}
}