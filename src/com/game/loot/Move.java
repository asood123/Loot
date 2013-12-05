package com.game.loot;

enum ACTION {
	DRAW,
	PLAY_MERCHANT_SHIP,
	PLAY_ATTACK
};

public class Move {
	private ACTION action;
	private Card card;
	
	// UGLY:  Might want to move to subclasses of Move that calls the constructor
	private MerchantShip merchantShip;
	
	Move(ACTION action, Card card, MerchantShip merchantShip) {
		this.action = action;
		this.card = card;
		this.merchantShip = merchantShip;
	}
	
	public ACTION getAction() {
		return action;
	}
	
	public Card getCard() {
		return card;
	}
	
	public MerchantShip getMerchantShip() {
		return merchantShip;
	}
}
