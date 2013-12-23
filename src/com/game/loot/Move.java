package com.game.loot;

enum ACTION {
	DRAW,
	PLAY_MERCHANT_SHIP,
	PLAY_ATTACK,
	DISCARD;
	
	@Override
	public String toString(){
		String value = Card.ANSI_CYAN;
		switch (ordinal()) {
		case 0: 
			value += "Draw";
			break;
		case 1: 
			value += "Play";
			break;
		case 2: 
			value += "Attack";
			break;
		case 3: 
			value += "Discard";
			break;
		default: 
			value += "Error";
			break;
		}
		value += Card.ANSI_RESET;
		return value;
	}
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
	
	public String toString() {
		String s = action.toString();
		
		if (card != null) {
			s+= " " + card.toString();
		}
		if (battle != null) {
			s+= " | " + battle.toString();
		}
		
		return s;
	}
}
