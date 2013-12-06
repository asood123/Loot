package com.game.loot;

enum ACTION {
	DRAW,
	PLAY_MERCHANT_SHIP,
	PLAY_ATTACK,
	DISCARD
};

public class Move {
	private ACTION action;
	private Card card;
	
	// UGLY:  Might want to move to subclasses of Move that calls the constructor
	private Battle battle;
	
	Move(ACTION action, Card card, Battle battle) {
		this.action = action;
		this.card = card;
		this.battle = battle;
	}
	
	public ACTION getAction() {
		return action;
	}
	
	public Card getCard() {
		return card;
	}
	
	public Battle getBattle() {
		return battle;
	}
}
