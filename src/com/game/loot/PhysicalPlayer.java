package com.game.loot;

public class PhysicalPlayer extends Player {

	private int handCount;
	
	public PhysicalPlayer(String name) {
		super(name);
		this.handCount = 6;
	}

	@Override
	public int getHandCount() {
		return handCount;
	}

	@Override
	public Move getNextMove(GameState gm) {
		// TODO: Ask player for move
		return null;
	}

	@Override
	public void addCard(Card card) {
		handCount++;
	}

	@Override
	public void removeCard(Card card) {
		handCount--;
	}
}
