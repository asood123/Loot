package com.game.loot;

enum ACTION {
	DRAW,
	PLAY_MERCHANT_SHIP,
	PLAY_PIRATE_SHIP
};

public class Move {
	ACTION action;
	
	Card card;
	
	Move(ACTION action, Card card) {
		this.action = action;
		this.card = card;
	}
}
